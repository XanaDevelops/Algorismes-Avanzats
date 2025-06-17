package model;

import java.io.Serializable;

public class Xarxa extends Solver implements Serializable {

    private final int nEntrades;
    private final int[] config;
    private final int nSortides;

    private double[][] pesosEntradaOculta;
    private double[][] capesOcultes;
    private double[][][] pesosOcultes;
    private double[][] pesosSortida;


    private final double deltaEntrenament;

    public Xarxa(int nEntrades, int[] config, int nSortides, double deltaEntrenament) {
        this.nEntrades = nEntrades;
        this.config = config;
        this.nSortides = nSortides;
        this.deltaEntrenament = deltaEntrenament;

        pesosEntradaOculta = new double[config[0]][nEntrades];
        capesOcultes = new double[config.length][];
        for (int i = 0; i < capesOcultes.length; i++) {
            capesOcultes[i] = new double[config[i]];
        }
        pesosOcultes = new double[config.length-1][][];
        for (int i = 0; i < pesosOcultes.length; i++) {
            pesosOcultes[i] = new double[config[i+1]][config[i]];
        }
        pesosSortida = new double[nSortides][config[config.length-1]];
    }

    public Xarxa(int nEntrades, int[] config, int nSortides) {
        this(nEntrades, config, nSortides, 0.01);
    }


    // Función Sigmoide (para la salida)
    private double sigmoide(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    // Derivada de Sigmoide
    private double derivadaSigmoide(double x) {
        return x * (1 - x);
    }

    @Override
    public void run() {

    }

    @Override
    public void comunicar(String msg) {

    }
}
