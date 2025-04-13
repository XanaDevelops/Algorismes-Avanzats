package controlador;


import model.Dades;
import model.TipoPunt;
import model.calculs.Calcul;
import model.calculs.ParellaPropera_dv;
import model.calculs.ParellaPropera_fb;
import model.calculs.kdTree.Parella_Propera_kd;
import model.generadors.Generador;
import model.generadors.GeneradorExponencial;
import model.generadors.GeneradorGaussia;
import model.generadors.GeneradorUniforme;
import model.calculs.kdTree.KdArbre;
import model.punts.Punt;
import model.punts.Punt2D;
import model.punts.Punt3D;
import vista.Finestra;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {
    public static Main instance; //singleton

    Comunicar finestra;
    Dades dades;
    private List<Punt> punts;

    private ArrayList<Comunicar> processos = null;

    private final ExecutorService executor = Executors.newFixedThreadPool(16);

    private static final Map<String, Class<? extends Generador>> GENERADORS = Map.of(
            "Uniforme", GeneradorUniforme.class,
            "Gaussiana", GeneradorGaussia.class,
            "Exponencial", GeneradorExponencial.class
    );
    private static final Map<String, Class<? extends Calcul>> ALGORISMES = Map.of(
            "Parella propera Força Bruta", ParellaPropera_fb.class,
            "Parella propera Dividir i vèncer", ParellaPropera_dv.class,
            "Parella propera Kd-Arbre", Parella_Propera_kd.class
//                "Parella llunyana Força Bruta"
    );

    public static void main(String[] args) {
        (new Main()).init();

    }

    private void init() {
        instance = this;
        dades = new Dades();
        punts = new ArrayList<>();
        processos = new ArrayList<>();

//        executor.execute(() -> {
//            finestra = new Finestra();
//        });
        SwingUtilities.invokeLater(() -> finestra = new Finestra());

    }

    @Override
    public void comunicar(String msg) {
        // Dividim el missatge pels caràcters ":" per determinar la comanda
        String[] parts = msg.split(":");
        switch (parts[0]) {
            // --------------------------
            // Format esperat: generar:<num>:<distribucio>:<dimensio>:<min>:<max>[:<extra1>:<extra2>...]
            case "generar":
                punts.clear();

                try {
                    List<Object> params = new ArrayList<>();
                    TipoPunt tp = parts[3].equalsIgnoreCase("p3D") ? TipoPunt.p3D : TipoPunt.p2D;

                    params.add(Integer.parseInt(parts[1]));
                    Random rn = new Random();
                    int max = Dades.RANG_PUNT;
                    int min = tp == TipoPunt.p2D ? 0 : -max;
                    params.add(min);
                    params.add(max);
                    String distribucio = parts[2];  // "Uniforme", "Gaussiana", o "Exponencial"



                    // Capturar els possibles paràmetres extra per generadors com Gaussiana o Exponencial

                    if (distribucio.equalsIgnoreCase("Gaussiana")) {
                        // Per a la distribució Gaussiana s'esperen dos paràmetres extra: mitjana i desviació estàndard

                        params.add((min + max) / 2.0);

//                        params.add(Math.max(1.0, (max - min) / 4.0));
                        params.add((min + max) / rn.nextDouble(5.1, 7.3));

                    } else if (distribucio.equalsIgnoreCase("Exponencial")) {
                        // Per a la distribució Exponencial s'espera un paràmetre extra: lambda

//                        params.add((double) 2 *100000 /(max));
                        params.add(rn.nextDouble(0.1, 4.5));

                    }

                    generarPunts(GENERADORS.get(distribucio), tp, params);

                    // Actualitzar la vista per mostrar els punts generats
                    finestra.comunicar("dibuixPunts");
                } catch (NumberFormatException e) {
                    System.err.println("MAIN: paràmetres numèrics invàlids en 'generar': " + msg);
                } catch (Exception e) {
                    System.err.println("MAIN: error en generar punts: " + e.getMessage());
                }
                break;


            // Format esperat: calcular:<distancia>:<algorisme>
            case "calcular":
                try {
                    String alg = String.format("%s %s", parts[1], parts[2]);

                    Class<? extends Calcul> calculClass = ALGORISMES.get(alg);

                    calcularAlgorisme(calculClass);


                } catch (Exception e) {
                    System.err.println("MAIN: error en executar el càlcul: " + e.getMessage());
                }
                break;

            // Missatge per aturar els processos actius
            case "aturar":
                for (Comunicar proces : processos) {
                    proces.comunicar("aturar");
                }
                processos.clear();
                break;


            case "esborrar":
                this.comunicar("aturar");
                //esborrar els punts
                dades.clearPunts();
                dades.clearForcaBruta();
                dades.clearDividirVencer();
                finestra.comunicar("pintar");
                break;

            // Altres missatges (com ara "pintar", "color", etc.)
            default:
                // Propagar altres missatges directament a la GUI
                finestra.comunicar(msg);
                break;
        }

    }

    public Dades getDades() {
        return dades;
    }

    public void setDades(Dades dades) {
        this.dades = dades;
    }

    // Mètode per generar els punts mitjançant el generador especificat
    private void generarPunts(Class<? extends Generador> generadorClass, TipoPunt tp, List<Object> params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        dades.setTp(tp);
        String metodeGeneracio = tp == TipoPunt.p2D ? "genera2D" : "genera3D";

        Class<?>[] paramTypes = params.stream()
                .map(p -> {
                    if (p instanceof Integer) return int.class;
                    if (p instanceof Double) return double.class;

                    return p.getClass();
                })
                .toArray(Class<?>[]::new);

        // Instanciar generador

        Object generador = generadorClass.getConstructor(paramTypes).newInstance(params.toArray());

        Object res = generador.getClass().getMethod(metodeGeneracio).invoke(generador);

        if (res instanceof List<?>) {
            dades.setPunts((List<Punt>) res);
//            System.out.println( "punts generats" + res.toString());
        } else {
            System.err.println("Error en generar la llista de punts.");
        }
    }

    // Mètode per executar l'algorisme de càlcul
    private void calcularAlgorisme(Class<? extends Calcul> calculClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        executor.execute(() -> {
            try {
                Calcul calcul = calculClass.getConstructor(Dades.class).newInstance(dades);

                if (calcul instanceof Comunicar) {
                    synchronized (processos) {
                        processos.add((Comunicar) calcul);
                    }
                }
                calcul.run();
                System.out.println("Resultats de càlcul FB: " + dades.getForcaBruta().toString());
                System.out.println("Resultats de càlcul DV: " + dades.getDividirVencer().toString());
                System.out.println("Resultats de càlcul KD: " + dades.getKd().toString());
//                finestra.comunicar("pintar");
                finestra.comunicar("dibiuxDistancia");
                finestra.comunicar("pintaElement");

            } catch (Exception e) {
                System.err.println("Error en el càlcul: " + e.getMessage());
            }
        });
    }
}
