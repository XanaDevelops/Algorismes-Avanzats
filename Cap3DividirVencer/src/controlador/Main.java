package controlador;


import model.Dades;
import model.TipoPunt;
import model.calculs.Calcul;
import model.calculs.ParellaPropera_dv;
import model.calculs.ParellaPropera_fb;
import model.generadors.*;
import model.generadors.GeneradorExponencial;
import model.generadors.GeneradorGaussia;
import model.generadors.GeneradorUniforme;
import model.punts.Punt;
import vista.*;


import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {
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
            "Parella propera Dividir i vèncer", ParellaPropera_dv.class
//                "Parella llunyana Força Bruta"
    );

    public static void main(String[] args) {
        (new Main()).init();

    }

    private void init() {
        dades = new Dades();
        punts = new ArrayList<>();
        processos = new ArrayList<>();

        executor.execute(() -> {
            finestra = new Finestra(this, dades);
        });
    }

    @Override
    public void comunicar(String s) {

        String[] parts = s.split(":");
        switch (parts[0]) {

            case "generar":
                System.out.println(s);
                String[] res = s.split(":");
                int num = Integer.parseInt(res[1]);
                Random r = new Random();
                punts.clear();


                //generar:num:distribucio:dimensio: algorisme: parella


                try {
                    executar(GENERADORS.get(parts[2]), num, TipoPunt.valueOf(parts[3]), ALGORISMES.get(parts[4] + " " + parts[5] ));
                    finestra.comunicar("dibuixPunts");
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         ClassNotFoundException | IllegalAccessException e) {
                    System.out.println(e.getMessage());
                }

                break;


            case "aturar":
                for (Comunicar proces : processos) {
                    proces.comunicar("aturar");
                }
                break;
            case "esborrar":
                this.comunicar("aturar");
               //esborrar els punts
                dades.clearPunts();


                finestra.comunicar("pintar");
                break;
        }

    }

    public Dades getDades() {
        return dades;
    }

    public void setDades(Dades dades) {
        this.dades = dades;
    }

    private  void executar(Class<? extends Generador> classe, int num, TipoPunt tp, Class<? extends Calcul> algorisme)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {


        dades.setTp(tp);


        String metodoGeneracion = tp == TipoPunt.p2D ? "genera2D" : "genera3D";

        Object generador =  classe.getConstructor(int.class, int.class, int.class)
                .newInstance(num, 0, 100000);



       Object o =  generador.getClass().getMethod(metodoGeneracion).invoke(generador);
        if (o instanceof List<?>) {

            dades.setPunts((List<Punt>) o);
            //cridar a l'algorisme per calcular la distància
            Calcul calcul = algorisme.getConstructor(Dades.class).newInstance(dades);

            calcul.run();
            System.out.println(dades.getForcaBruta().toString());;



        } else {
            System.out.println("Error a l'hora de generar la llista de punts.");
        }
    }

//    private void executar(Class<? extends Comunicar> clase, TipoPunt tp, Distribucio distribucio) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
//        for (Comunicar enmarxa : processos) {
//            enmarxa.comunicar("aturar");
//        }
//
//        processos.clear();
//
//
//        Comunicar proces = (Comunicar) clase.getConstructor(Main.class, Dades.class).newInstance(this, dades);
//        processos.add(proces);
//        executor.execute((Runnable) proces);
//    }



}