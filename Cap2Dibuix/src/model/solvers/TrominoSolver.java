package model.solvers;

import model.Dades;
import model.Tipus;

import principal.Comunicar;
import principal.Main;

import java.util.Arrays;

public class TrominoSolver implements Runnable, Comunicar {
    private Main p;
    private Dades data;
    private static int numActual;
    private static final int RETJOLA = -1;
    /**
     * Variable booleana per poder aturar el fil d'execució.
     */
    private volatile boolean stop;

    public TrominoSolver(Main p, Dades data) {
        this.p = p;
        this.data = data;
        data.setTipus(Tipus.TROMINO);


        data.setTauler(new int[data.getProfunditat()][data.getProfunditat()]);
        numActual = 1;

        // Inicialitzar la matriu amb totes les caselles buides
        for (int[] fila : data.getTauler()) {
            Arrays.fill(fila, 0);
        }


        int num1 = (int) (data.getProfunditat()* Math.random());
        int num2 = (int) (data.getProfunditat()* Math.random());

        data.setInici(num1 , num2);
    }

    private void trominoRec(int mida, int topx, int topy) {
        if(!stop) {
            // Cas base: mida 2x2, col·locar l'última casella
            if (mida == 2) {
                omplirTromino(topx, topy, mida);
                //            main.comunicar("omplicarTromino x"+ topx +" y"+ topy +" mida"+ mida );
                numActual++;
            } else {
                // Cas recursiu
                int[] forat = trobarForat(topx, topy, mida);

                // Utilitzem l'enum Mode per determinar la ubicació del forat i procedir segons correspongui
                Mode mode = determinarMode(forat[0], forat[1], topx, topy, mida);

                // Omplim el tromino central
                omplirTrominoCentral(mode, topx, topy, mida);
                //            main.comunicar("omplirTrominoCentral mode"+ mode + " x"+ topx +" y"+ topy +" mida"+ mida ); //algo de l'estil

                // Recursió per als quatre quadrants
                trominoRec(mida / 2, topx, topy);
                trominoRec(mida / 2, topx, topy + mida / 2);
                trominoRec(mida / 2, topx + mida / 2, topy);
                trominoRec(mida / 2, topx + mida / 2, topy + mida / 2);
            }
        }
    }

    // Troba la posició del forat dins un sub-tauler (quadrant).
    private int[] trobarForat(int topx, int topy, int mida) {
        int[] forat = new int[2];
        for (int x = topx; x < topx + mida; x++) {
            for (int y = topy; y < topy + mida; y++) {
                if (data.getValor(x, y) != 0) {
                    forat[0] = x;
                    forat[1] = y;
                }
            }
        }
        return forat;
    }

    // Determina el mode (quadrant) en què es troba el forat dins el sub-tauler.
    private Mode determinarMode(int foratX, int foratY, int topx, int topy, int mida) {
        if (foratX < topx + mida / 2 && foratY < topy + mida / 2) {
            return Mode.LU;
        } else if (foratX < topx + mida / 2 && foratY >= topy + mida / 2) {
            return Mode.RU;
        } else if (foratX >= topx + mida / 2 && foratY < topy + mida / 2) {
            return Mode.LD;
        } else {
            return Mode.RD;
        }
    }

    // Omple el tromino central segons el mode.
    private void omplirTrominoCentral(Mode mode, int topx, int topy, int mida) {
        switch (mode) {
            case LU:
                data.setValor(topx + mida / 2, topy + mida / 2 - 1, numActual);
                data.setValor(topx + mida / 2, topy + mida / 2, numActual);
                data.setValor(topx + mida / 2 - 1, topy + mida / 2, numActual);
                break;
            case RU:
                data.setValor(topx + mida / 2, topy + mida / 2 - 1, numActual);
                data.setValor(topx + mida / 2, topy + mida / 2, numActual);
                data.setValor(topx + mida / 2 - 1, topy + mida / 2 - 1, numActual);
                break;
            case LD:
                data.setValor(topx + mida / 2 - 1, topy + mida / 2, numActual);
                data.setValor(topx + mida / 2, topy + mida / 2, numActual);
                data.setValor(topx + mida / 2 - 1, topy + mida / 2 - 1, numActual);
                break;
            case RD:
                data.setValor(topx + mida / 2 - 1, topy + mida / 2, numActual);
                data.setValor(topx + mida / 2, topy + mida / 2 - 1, numActual);
                data.setValor(topx + mida / 2 - 1, topy + mida / 2 - 1, numActual);
                break;
        }
        numActual++;
    }


    // Omple un subtauler de mida x mida amb el tromino actual.
    private void omplirTromino(int topx, int topy, int mida) {
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if (data.getValor(topx + 1, topy + j) == 0) {
                    data.setValor(topx + i, topy + j, numActual);
                    p.comunicar("pintar");
                }
            }
        }
    }

    @Override
    public void run() {

        stop = false;
        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(2, data.getProfunditat());
        System.out.println("Temps esperat " + tempsEsperat  + " segons");
        p.comunicar("tempsEsperat");

        long time = System.currentTimeMillis();
        trominoRec(data.getProfunditat(), 0, 0);


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

