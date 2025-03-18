package model;

import java.util.Arrays;

public class TrominoSolver2 implements Model {

    private int[][] tauler;
    private int numActual;

    public TrominoSolver2(int mida, int x, int y) {

        int midaActual = 1;
        while (midaActual < mida) midaActual *= 2;

        // Assegurem-nos que la mida del tauler sigui una potència de 2 perfecta.
        tauler = new int[midaActual][midaActual];
        numActual = 1;

        // Omplim el tauler amb totes les caselles buides.
        for (int i = 0; i < midaActual; i++) {
            Arrays.fill(tauler[i], 0);
        }

        // Aquesta casella representa el forat original en el tromino.
        tauler[x][y] = -1;
    }

    @Override
    public int[][] getMatriu() {
        return tauler;
    }

    // Crida per emmarcar el mètode recursiu.
    public void resol() {
        trominoRec(tauler.length, 0, 0);
    }

    private void trominoRec(int mida, int topx, int topy) {

        // Cas base: mida 2x2, col·locar l'última casella
        if (mida == 2) {
            omplirTromino(topx, topy, mida);
            numActual++;
        } else {
            // Cas recursiu
            int[] forat = trobarForat(topx, topy, mida);

            // Utilitzem l'enum Mode per determinar la ubicació del forat i procedir segons correspongui
            Mode mode = determinarMode(forat[0], forat[1], topx, topy, mida);

            // Omplim el tromino central
            omplirTrominoCentral(mode, topx, topy, mida);

            // Recursió per als quatre quadrants
            trominoRec(mida / 2, topx, topy);
            trominoRec(mida / 2, topx, topy + mida / 2);
            trominoRec(mida / 2, topx + mida / 2, topy);
            trominoRec(mida / 2, topx + mida / 2, topy + mida / 2);
        }
    }

    // Troba la posició del forat dins un sub-tauler (quadrant).
    private int[] trobarForat(int topx, int topy, int mida) {
        int[] forat = new int[2];
        for (int x = topx; x < topx + mida; x++) {
            for (int y = topy; y < topy + mida; y++) {
                if (tauler[x][y] != 0) {
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
                tauler[topx + mida / 2][topy + mida / 2 - 1] = numActual;
                tauler[topx + mida / 2][topy + mida / 2] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2] = numActual;
                break;
            case RU:
                tauler[topx + mida / 2][topy + mida / 2 - 1] = numActual;
                tauler[topx + mida / 2][topy + mida / 2] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2 - 1] = numActual;
                break;
            case LD:
                tauler[topx + mida / 2 - 1][topy + mida / 2] = numActual;
                tauler[topx + mida / 2][topy + mida / 2] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2 - 1] = numActual;
                break;
            case RD:
                tauler[topx + mida / 2 - 1][topy + mida / 2] = numActual;
                tauler[topx + mida / 2][topy + mida / 2 - 1] = numActual;
                tauler[topx + mida / 2 - 1][topy + mida / 2 - 1] = numActual;
                break;
        }
        numActual++;
    }

    // Omple un sub-tauler de mida x mida amb el tromino actual.
    private void omplirTromino(int topx, int topy, int mida) {
        for (int i = 0; i < mida; i++) {
            for (int j = 0; j < mida; j++) {
                if (tauler[topx + i][topy + j] == 0) {
                    tauler[topx + i][topy + j] = numActual;
                }
            }
        }
    }

    // Imprimeix l'objecte actual
    public void imprimir() {
        for (int[] fila : tauler) {
            for (int c : fila) {
                System.out.print("|" + c + "\t");
            }
            System.out.println("|\n");
        }
    }
}
