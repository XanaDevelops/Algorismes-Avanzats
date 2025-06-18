package model;

import java.io.Serializable;
import java.util.Collections;

public class Xarxa extends Solver implements Serializable {

    private final int nEntrades;
    private final int[] config;
    private final int nSortides;

    private double[][] capesOcultes;
    private double[][][] pesosOcultes;
    private double[][] pesosSortida;


    private final double deltaEntrenament;

    public Xarxa(int nEntrades, int[] config, int nSortides, double deltaEntrenament) {
        this.nEntrades = nEntrades;
        this.config = config;
        this.nSortides = nSortides;
        this.deltaEntrenament = deltaEntrenament;

        capesOcultes = new double[config.length][];
        for (int i = 0; i < capesOcultes.length; i++) {
            capesOcultes[i] = new double[config[i]];
        }
        pesosOcultes = new double[config.length][][];
        pesosOcultes[0] = new double[config[0]][nEntrades];
        for (int i = 1; i < pesosOcultes.length; i++) {
            pesosOcultes[i] = new double[config[i - 1]][config[i]];
        }
        pesosSortida = new double[nSortides][config[config.length - 1]];

        //TODO: RANDOM!!!
    }

    public Xarxa(int nEntrades, int[] config, int nSortides) {
        this(nEntrades, config, nSortides, 0.01);
    }

    public double[] predecir(double[] entrada) {
        assert entrada.length == nEntrades;

        //entrada a capa oculta
        for (int neurona = 0; neurona < capesOcultes[0].length; neurona++) {
            double suma = 0;
            for (int pes = 0; pes < nEntrades; pes++) {
                suma += pesosOcultes[0][neurona][pes] * entrada[pes];
            }
            capesOcultes[0][neurona] = sigmoide(suma);
        }

        //capes ocultes
        for (int capa = 1; capa < pesosOcultes.length; capa++) {
            for (int neurona = 0; neurona < capesOcultes[capa].length; neurona++) {
                double suma = 0;
                for (int pes = 0; pes < capesOcultes[capa].length; pes++) {
                    suma += pesosOcultes[capa][neurona][pes] * capesOcultes[capa][pes];
                }
                capesOcultes[capa][neurona] = sigmoide(suma);
            }
        }

        double[] sumesSortida = new double[nSortides];
        for (int nSortida = 0; nSortida < sumesSortida.length; nSortida++) {
            double suma = 0;
            for (int pes = 0; pes < pesosSortida[nSortida].length; pes++) {
                suma += pesosSortida[nSortida][pes] * capesOcultes[capesOcultes.length - 1][pes];
            }
            sumesSortida[nSortida] = sigmoide(suma);
        }

        return sumesSortida;
    }

    public void entrenar(double[][] entrades, double[][] sortides, int epoc) {
        assert entrades[0].length == nEntrades;
        for (int _i = 0; _i < epoc; _i++) {
            double errorTotal = 0;
            for (int nEntrada = 0; nEntrada < entrades.length; nEntrada++) {
                double[] sortida = predecir(entrades[nEntrada]);
                double[] error = new double[nSortides];
                for (int sort = 0; sort < error.length; sort++) {
                    error[sort] = sortides[nEntrada][sort] - sortida[sort];
                }
                double[] deltaSortida = new double[nSortides];
                for (int sort = 0; sort < deltaSortida.length; sort++) {
                    deltaSortida[sort] = error[sort] * derivadaSigmoide(sortida[sort]);
                }

                double[][] deltasOcultes = new double[capesOcultes.length][];
                deltasOcultes[capesOcultes.length - 1] = deltaSortida;
                double[][] lastPesos = pesosSortida;

                for (int capa = capesOcultes.length - 1 - 1; 0 <= capa; capa--) {
                    double[] deltaOculta = new double[capesOcultes[capa].length];
                    for (int neurona = 0; neurona < deltaOculta.length; neurona++) {
                        for (int pes = 0; pes < lastPesos.length; pes++) {
                            deltaOculta[neurona] += deltasOcultes[capa + 1][pes] * lastPesos[neurona][pes] * derivadaSigmoide(capesOcultes[capa][pes]);
                        }

                    }
                    deltasOcultes[capa] = deltaOculta;
                }

                //pesos oculta -> sortides
                for (int sort = 0; sort < pesosSortida.length; sort++) {
                    for (int pes = 0; pes < pesosSortida[sort].length; pes++) {
                        pesosSortida[sort][pes] = deltaEntrenament * deltaSortida[sort] * capesOcultes[capesOcultes.length - 1][pes];
                    }
                }

                //pesos
                for (int capa = 1; capa < capesOcultes.length; capa++) {
                    for (int neurona = 0; neurona < capesOcultes[capa].length; neurona++) {
                        for (int pes = 0; pes < pesosOcultes[capa][neurona].length; pes++) {
                            pesosOcultes[capa][neurona][pes] += deltaEntrenament * deltasOcultes[capa][neurona] * capesOcultes[capa][neurona];
                        }
                    }

                }

                for (int neurona = 0; neurona < capesOcultes[0].length; neurona++) {
                    for (int pes = 0; pes < pesosOcultes[0][neurona].length; pes++) {
                        pesosOcultes[0][neurona][pes] += deltaEntrenament * deltasOcultes[0][neurona] * entrades[nEntrada][pes];
                    }
                }

                for (double e : error) {
                    errorTotal += Math.abs(e);
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
