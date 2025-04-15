package controlador;


import model.Dades;
import model.TipoPunt;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.calculs.maxim.ParellaLlunyana_CH;
import model.calculs.maxim.ParellaLlunyana_fb;
import model.calculs.maxim.ParellaMaximaUniforme;
import model.calculs.minim.ParellaPropera_dv;
import model.calculs.minim.ParellaPropera_fb;
import model.calculs.minim.kdTree.Parella_Propera_kd;
import model.generadors.Generador;
import model.generadors.GeneradorExponencial;
import model.generadors.GeneradorGaussia;
import model.generadors.GeneradorUniforme;
import model.punts.Punt;
import vista.Finestra;

import javax.crypto.spec.PSource;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

public class Main implements Comunicar {
    public static Main instance; //singleton

    Comunicar finestra;
    Dades dades;
    private List<Punt> punts;
    private final static int LIMIT_PUNTS = 100000;


    private boolean modeConcurrentOn = false;
    private ArrayList<Comunicar> processos = null;

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);

    private static final Map<String, Class<? extends Generador>> GENERADORS = Map.of(
            "Uniforme", GeneradorUniforme.class,
            "Gaussiana", GeneradorGaussia.class,
            "Exponencial", GeneradorExponencial.class
    );
    private static final Map<String, Class<? extends Calcul>> ALGORISMES = Map.of(
            "Parella propera Força Bruta", ParellaPropera_fb.class,
            "Parella propera Dividir i vèncer", ParellaPropera_dv.class,
            "Parella propera Kd-Arbre", Parella_Propera_kd.class,

            "Parella llunyana Força Bruta", ParellaLlunyana_fb.class,
            "Parella llunyana Uniforme", ParellaMaximaUniforme.class,
            "Parella llunyana Convex Hull", ParellaLlunyana_CH.class
    );

    public static void main(String[] args) {
        (new Main()).init();


    }

    private void init() {
        instance = this;
        dades = new Dades();
        punts = new ArrayList<>();
        processos = new ArrayList<>();

        System.out.println("MODE CONCURRENT (dins calcul): " + modeConcurrentOn);

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
                    int n = Integer.parseInt(parts[1]);
                    modeConcurrentOn = n >= LIMIT_PUNTS;
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
                        params.add(tp == TipoPunt.p2D ? max / 2.0 : 0.0);

                        params.add(max / rn.nextDouble(5.1, 7.3));

                    } else if (distribucio.equalsIgnoreCase("Exponencial")) {
                        // Per a la distribució Exponencial s'espera un paràmetre extra: lambda

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
                dades.clearResultats();
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

    public boolean isModeConcurrentOn() {
        return modeConcurrentOn;
    }

    // Mètode per generar els punts mitjançant el generador especificat
    private void generarPunts(Class<? extends Generador> generadorClass, TipoPunt tp, List<Object> params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ExecutionException, InterruptedException {
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
        List<Future<List<Punt>>> futures = new ArrayList<>();
        int numPunts = (int) params.getFirst();

        int subConjunt = numPunts / (executor.getCorePoolSize() - executor.getActiveCount());


        for (int i = 0; i < 4; i++) {
            futures.add(executor.submit(() -> {
                Generador generador = generadorClass
                        .getConstructor(paramTypes)
                        .newInstance(params.toArray());
                return tp == TipoPunt.p2D ? generador.genera2D(subConjunt) : generador.genera3D(subConjunt);
            }));
        }
        List<Punt> totalPunts = new ArrayList<>();
        for (Future<List<Punt>> future : futures) {
            totalPunts.addAll(future.get()); // .get() espera a que el hilo termine
        }
        dades.setPunts(totalPunts);

    }

    // Mètode per executar l'algorisme de càlcul
    private void calcularAlgorisme(Class<? extends Calcul> calculClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        executor.execute(() -> {
            try {
                Calcul calcul = calculClass.getConstructor().newInstance();

                if (calcul instanceof Comunicar) {
                    synchronized (processos) {
                        processos.add((Comunicar) calcul);
                    }
                }
                calcul.run();
//                System.out.println("Resultat FB "+ dades.getResultats().get(TipusCalcul.FB_MAX));
//                System.out.println("Resultat uni "+ dades.getResultats().get(TipusCalcul.UNI_MAX));


            } catch (Exception e) {
                System.err.println("Error en el càlcul: " + e.getMessage());
            }
            finestra.comunicar("dibuixDistancia");
            finestra.comunicar("pintaElement");
        });
    }
}
