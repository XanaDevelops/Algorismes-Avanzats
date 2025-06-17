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

    public double[] predecir(double[] entrada){
        assert entrada.length == nEntrades;

        //entrada a capa oculta
        for (int i = 0; i < capesOcultes[0].length; i++) {
            double suma = 0;
            for (int j = 0; j < nEntrades; j++) {
                suma += pesosEntradaOculta[i][j] * entrada[j];
            }
            capesOcultes[0][i] = sigmoide(suma);
        }

        //capes ocultes
        for(int i = 0; i < pesosOcultes.length; i++){
            for (int j = 0; j < capesOcultes[i+1].length; j++) {
                double suma = 0;
                for (int k = 0; k < capesOcultes[i].length; k++) {
                    suma += pesosOcultes[i][j][k];
                }
                capesOcultes[i+1][j] = sigmoide(suma);
            }
        }

        double[] sumesSortida = new double[nSortides];
        for (int i = 0; i < sumesSortida.length; i++) {
            double suma = 0;
            for (int j = 0; j < pesosSortida[i].length; j++) {
                suma += pesosSortida[i][j] * capesOcultes[capesOcultes.length-1][j];
            }
            sumesSortida[i] = sigmoide(suma);
        }

        return sumesSortida;
    }

    public void entrenar(double[][] entrades, double[][] sortides, int epoc){
        assert entrades[0].length == nEntrades;
        for (int _i = 0; _i < epoc; _i++) {
            double errorTotal = 0;
            for (int i = 0; i < entrades.length; i++) {
                double[] sortida = predecir(entrades[i]);
                double[] error = new double[nSortides];
                for (int j = 0; j < error.length; j++) {
                    error[j] = sortides[i][j] - sortida[j];
                }
                double[] deltaSortida = new double[nSortides];
                for (int j = 0; j < deltaSortida.length; j++) {
                    deltaSortida[j] = error[j] * derivadaSigmoide(sortida[j]);
                }

                double[][] deltaOcultes = new double[pesosOcultes.length][];


                //backp oculta -> sortides
                for (int j = 0; j < pesosSortida.length; j++) {
                    for (int k = 0; k < pesosSortida[j].length; k++) {
                        pesosSortida[j][k] = deltaEntrenament * deltaSortida[j] * capesOcultes[capesOcultes.length-1][k];
                    }
                }


            }
        }
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
