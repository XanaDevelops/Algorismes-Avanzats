package model.solvers;

import model.Dades;
import model.Tipus;

import principal.Comunicar;
import principal.Main;

import java.util.Arrays;

/**
 * Classe que implementa un solucionador del problema dels Trominos utilitzant
 * el mètode divideix i venceràs. Implementa Runnable per permetre
 * l'execució en fils.
 */
public class TrominoSolver implements Runnable, Comunicar {
    private Main p; // Referència a l'objecte principal
    private Dades data; // Objecte que conté les dades del problema
    private static int numActual; // Número del tromino actual
    private static final int RETJOLA = -1; // Valor que representa el forat al tauler
    private boolean stop; // Variable per controlar si el procés s'ha d'aturar

    /**
     * Constructor de la classe.
     * @param p Instància de Main que s'encarrega de gestionar la comunicació entre components.
     * @param data Objecte que conté la informació del tauler i les dades necessàries per generar el fractal.
     */
    public TrominoSolver(Main p, Dades data) {
        this.p = p;
        this.data = data;

        // Estableix el tipus de fractal que es generarà
        data.setTipus(Tipus.TROMINO);

        // Inicialitza el tauler amb la mida adequada
        data.setTauler(new int[data.getProfunditat()][data.getProfunditat()]);
        numActual = 1;

        // Omple tota la matriu amb el valor 0 (caselles buides)
        for (int[] fila : data.getTauler()) {
            Arrays.fill(fila, 0);
        }

        data.setValor(data.getIniciTromino()[0],  data.getIniciTromino()[1], RETJOLA);
    }

    /**
     * Mètode recursiu per resoldre el problema dels trominos.
     * @param mida Mida de la submatriu actual
     * @param topx Coordenada X superior esquerra
     * @param topy Coordenada Y superior esquerra
     */
    private void trominoRec(int mida, int topx, int topy) {
        if(!stop) {
            if (mida == 2) {
                // Cas base: si la mida és 2x2, omplim el tromino restant
                omplirTromino(topx, topy, mida);
                numActual++;
            } else {
                // Troba la posició del forat en el subtauler
                int[] forat = trobarForat(topx, topy, mida);

                // Utilitzem l'enum Mode per determinar la ubicació del forat i procedir segons correspongui
                Mode mode = determinarMode(forat[0], forat[1], topx, topy, mida);

                // Omple el tromino central depenent del mode
                omplirTrominoCentral(mode, topx, topy, mida);

                // Recursió per als quatre quadrants
                trominoRec(mida / 2, topx, topy);
                trominoRec(mida / 2, topx, topy + mida / 2);
                trominoRec(mida / 2, topx + mida / 2, topy);
                trominoRec(mida / 2, topx + mida / 2, topy + mida / 2);
            }
        }
    }

    /**
     * Troba la posició del forat dins del subtauler actual.
     */
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
                if (data.getValor(topx + i, topy + j) == 0) {
                    data.setValor(topx + i, topy + j, numActual);
                    p.comunicar("pintar");
                }
            }
        }
    }

    @Override
    public void run() {
        stop = false;

        // Inicia el comptador de temps en nanosegons
        long startTime = System.nanoTime();

        trominoRec(data.getTauler().length, 0, 0);

        // Calcula el temps real en nanosegons
        long elapsedTime = System.nanoTime() - startTime;

        // Converteix a segons (double)
        double tempsReal = elapsedTime / 1_000_000_000.0;

        // Calcula la constant multiplicativa
        double profunditatExp = Math.pow(2, data.getProfunditat());
        double constantMultiplicativa = tempsReal / profunditatExp;

        // Desa la constant multiplicativa
        data.setConstantMultiplicativa(constantMultiplicativa);

        // Calcula el temps esperat d'execució en segons
        double tempsEsperat = constantMultiplicativa * profunditatExp;

        // Mostra els resultats
        System.out.printf("Temps esperat: %.8f segons%n", tempsEsperat);
        p.comunicar("tempsEsperat");

        System.out.printf("Temps real: %.8f segons%n", tempsReal);
        p.comunicar("tempsReal");

        // Notifica que el procés ha finalitzat
        p.comunicar("aturar");

        //prevenir tornar a aturar
        if(!stop) aturar();
    }

    /**
     * Mètode per rebre missatges de comunicació.
     * Si es rep el missatge "aturar", es deté l'execució del fractal.
     *
     * @param s Missatge rebut per comunicar ordres al solver.
     */
    @Override
    public void comunicar(String s) {
        if (s.contentEquals("aturar")) {
            aturar();
        }
    }

    /**
     * Mètode per aturar l'execució del Solver.
     * Estableix la variable stop a true per indicar que el fil ha de finalitzar.
     */
    private void aturar() { stop = true; }
}

