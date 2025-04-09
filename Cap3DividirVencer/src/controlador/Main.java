package controlador;


import model.Dades;
import model.TipoPunt;
import model.generadors.*;
import model.generadors.GeneradorExponencial;
import model.generadors.GeneradorGaussia;
import model.generadors.GeneradorUniforme;
import model.punts.Punt;
import model.punts.Punt2D;
import vista.*;


import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {
    Comunicar finestra;
    Dades dades;
    private List<Punt> punts;

    private ArrayList<Comunicar> processos = null;

    private final ExecutorService executor = Executors.newFixedThreadPool(16);

    private Generador generador;
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

                String[] res = s.split(":");
                int num = Integer.parseInt(res[1]);
                Random r = new Random();
                punts.clear();

                //generar:num:distribucio:dimensio

                TipoPunt tp = TipoPunt.valueOf(parts[3]);
                switch(parts[2]){

                    case "Uniforme":
                        try {
//                            System.out.println("Generando..."+ parts[2]+ " "+ num + " " + tp);
                            executar(GeneradorUniforme.class, num,  tp );
                            finestra.comunicar("dibuixPunts");
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                 ClassNotFoundException | IllegalAccessException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                        case "Gaussiana":
                            try {
//                            System.out.println("Generando..."+ parts[2]+ " "+ num + " " + tp);
                                executar(GeneradorGaussia.class, num,  tp );
                                finestra.comunicar("dibuixPunts");
                            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                     ClassNotFoundException | IllegalAccessException e) {
                                System.out.println(e.getMessage());
                            }
                            break;

                            case "Exponencial":

                                try {
//                            System.out.println("Generando..."+ parts[2]+ " "+ num + " " + tp);
                                    executar(GeneradorExponencial.class, num,  tp );
                                    finestra.comunicar("dibuixPunts");
                                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                                         ClassNotFoundException | IllegalAccessException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                }



                break;

            case "classic":
                break;

            case "optimitzat":break;

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

    private  void executar(Class<? extends Generador> classe, int num, TipoPunt tp )
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {


        dades.setTp(tp);


        String metodoGeneracion = tp == TipoPunt.p2D ? "genera2D" : "genera3D";

        Object generador =  classe.getConstructor(int.class, int.class, int.class)
                .newInstance(num, 0, 100000);



       Object o =  generador.getClass().getMethod(metodoGeneracion).invoke(generador);
        if (o instanceof List<?>) {
            List<?> listaPuntos = (List<?>) o;
            System.out.println("Puntos generados:" + listaPuntos.toString());

            // Asignar los puntos generados a dades
            dades.setPunts((List<Punt>) o);
        } else {
            System.out.println("El método no devolvió una lista de puntos.");
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