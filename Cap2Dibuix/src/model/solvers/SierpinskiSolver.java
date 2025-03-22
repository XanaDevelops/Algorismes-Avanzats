package model.solvers;

import model.Dades;
import model.Tipus;
import principal.Comunicar;
import principal.Main;

import java.util.Arrays;

public class SierpinskiSolver implements  Runnable, Comunicar {
    private Main p;
    private Dades data;
    /**
     * Variable booleana per poder aturar el fil d'execució.
     */
    private volatile boolean stop;

    public SierpinskiSolver(Main p, Dades data) {
        this.p = p;
        this.data = data;
        data.setTipus(Tipus.TRIANGLE);
        data.setTauler(new int[data.getProfunditat()][data.getProfunditat() * 2 - 1]);

        // Inicialitzar la matriu amb totes les caselles buides
        for (int[] fila : data.getTauler()) {
            Arrays.fill(fila, 0);
        }
    }

    private void generarSierpinski(int x, int y, int mida) {
        if(!stop) {
            if (mida == 1) {
                data.setValor(x, y, 1);
                p.comunicar("pintar");
                return;
            }

            int novaMida = mida / 2;
            generarSierpinski(x, y, novaMida); // Triangle superior
            generarSierpinski(x + novaMida, y - novaMida, novaMida); // Triangle inferior esquerre
            generarSierpinski(x + novaMida, y + novaMida, novaMida); // Triangle inferior dret
        }
    }

    @Override
    public void run() {
        stop = false;
        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(2, data.getProfunditat());
        System.out.println("Temps esperat " + tempsEsperat  + " segons");
        p.comunicar("tempsEsperat");

        long time = System.currentTimeMillis();
        // Cridar la funció recursiva
        generarSierpinski(0, data.getProfunditat() - 1, data.getProfunditat());

        p.comunicar("aturar");

        time = (System.currentTimeMillis() - time)/1000;
        System.out.println("Temps real " + time  + " segons");
        p.comunicar("tempsReal");

        //actualitzar la constant multiplicativa
        data.setConstantMultiplicativa(time/Math.pow(2, data.getProfunditat() ));

        //prevenir tornar a aturar
        if(!stop) aturar();
    }

    @Override
    public void comunicar(String s) {
        if (s.contentEquals("aturar")) {
            aturar();
        }
    }

    /**
     * Mètode per aturar el fil d'execució.
     */
    private void aturar() {
        stop = true;
    }
}
