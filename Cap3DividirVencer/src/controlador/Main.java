package controlador;


import model.Dades;
import model.punts.Punt;
import model.punts.Punt2D;
import vista.Finestra;

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

    public static void main(String[] args) {
        (new Main()).init();
    }

    private void init() {
        dades = new Dades();
        punts = new ArrayList<>();

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
                for (int i = 0; i < num; i++)
                    punts.add(new Punt2D(r.nextInt() * 600, r.nextInt() * 500));

                System.out.println(Arrays.toString(punts.toArray()));
                finestra.comunicar("dibuixPunts");
                break;
            case "classic":
                break;

            case "optimitzat":break;

            case "aturar":
                for (Comunicar proces : processos) {
                    proces.comunicar("aturar");
                }
                break;
            case "borar":
                this.comunicar("aturar");
               //esborrar els punts

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
    private void executar(Class<? extends Comunicar> clase, int profunditat) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Comunicar enmarxa : processos) {
            enmarxa.comunicar("aturar");
        }

        processos.clear();


        Comunicar proces = (Comunicar) clase.getConstructor(Main.class, Dades.class).newInstance(this, dades);
        processos.add(proces);
        executor.execute((Runnable) proces);
    }



}