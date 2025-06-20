package model;

import controlador.Main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

public class Xarxa implements Serializable {

    private final int nEntrades;
    private final int[] config;
    private final int nSortides;

    private double[][] capesOcultes;
    private double[][][] pesosOcultes;
    private double[][] pesosSortida;

    private double errorTotal = 0;

    private final double deltaEntrenament;
    private boolean stop = false;

    public Xarxa(int nEntrades, int[] config, int nSortides, double deltaEntrenament) {
        this.nEntrades = nEntrades;
        this.config = config;
        this.nSortides = nSortides;
        this.deltaEntrenament = deltaEntrenament;

        Random r = new Random();

        capesOcultes = new double[config.length][];
        for (int i = 0; i < capesOcultes.length; i++) {
            capesOcultes[i] = new double[config[i]];
        }

        pesosOcultes = new double[config.length][][];
        pesosOcultes[0] = new double[config[0]][nEntrades];
        for (int i = 0; i < config[0]; i++) {
            for (int j = 0; j < nEntrades; j++) {
                pesosOcultes[0][i][j] = r.nextDouble() * 2 - 1;
            }
        }

        for (int i = 1; i < config.length; i++) {
            pesosOcultes[i] = new double[config[i]][config[i - 1]];
            for (int j = 0; j < config[i]; j++) {
                for (int k = 0; k < config[i - 1]; k++) {
                    pesosOcultes[i][j][k] = r.nextDouble() * 2 - 1;
                }
            }
        }

        // Pesos de la última capa oculta a la salida
        pesosSortida = new double[nSortides][config[config.length - 1]];
        for (int i = 0; i < nSortides; i++) {
            for (int j = 0; j < config[config.length - 1]; j++) {
                pesosSortida[i][j] = r.nextDouble() * 2 - 1;
            }
        }
    }

    public Xarxa(int nEntrades, int[] config, int nSortides) {
        this(nEntrades, config, nSortides, 0.01);
    }

    public double[] predecir(double[] entrada) {
        assert entrada.length == nEntrades;

        for (int neurona = 0; neurona < capesOcultes[0].length; neurona++) {
            double suma = 0;
            for (int pes = 0; pes < nEntrades; pes++) {
                suma += pesosOcultes[0][neurona][pes] * entrada[pes];
            }
            capesOcultes[0][neurona] = sigmoide(suma);
        }

        for (int capa = 1; capa < capesOcultes.length; capa++) {
            for (int neurona = 0; neurona < capesOcultes[capa].length; neurona++) {
                double suma = 0;
                for (int pes = 0; pes < capesOcultes[capa - 1].length; pes++) {
                    suma += pesosOcultes[capa][neurona][pes] * capesOcultes[capa - 1][pes];
                }
                capesOcultes[capa][neurona] = sigmoide(suma);
            }
        }

        double[] sumesSortida = new double[nSortides];
        for (int nSortida = 0; nSortida < nSortides; nSortida++) {
            double suma = 0;
            for (int pes = 0; pes < capesOcultes[capesOcultes.length - 1].length; pes++) {
                suma += pesosSortida[nSortida][pes] * capesOcultes[capesOcultes.length - 1][pes];
            }
            sumesSortida[nSortida] = sigmoide(suma);
        }

        return sumesSortida;
    }

    /**
     * Entrena la xarxa
     * @param entrades
     * @param sortides
     * @param epoc -1 activa el modo auto
     */
    public void entrenar(double[][] entrades, double[][] sortides, int epoc) {
        assert entrades[0].length == nEntrades;
        boolean auto = false;
        if(epoc <=0){
            Main.getInstance().logText("MODO AUTO");
            epoc = Integer.MAX_VALUE;
            auto = true;
        }
        double lastError = Double.MAX_VALUE;
        stop = false;
        for (int _i = 0; _i < epoc && !stop; _i++) {
            errorTotal = 0;
            for (int nEntrada = 0; nEntrada < entrades.length; nEntrada++) {
                double[] entrada = entrades[nEntrada];
                double[] sortidaEsperada = sortides[nEntrada];

                double[] sortida = predecir(entrada);

                double[] errorSortida = new double[nSortides];
                for (int i = 0; i < nSortides; i++) {
                    errorSortida[i] = sortidaEsperada[i] - sortida[i];
                }

                double[] deltaSortida = new double[nSortides];
                for (int i = 0; i < nSortides; i++) {
                    deltaSortida[i] = errorSortida[i] * derivadaSigmoide(sortida[i]);
                }

                // deltes
                double[][] deltasOcultes = new double[capesOcultes.length][];
                int lastCapa = capesOcultes.length - 1;

                //oculta --> sortida
                deltasOcultes[lastCapa] = new double[capesOcultes[lastCapa].length];
                for (int j = 0; j < capesOcultes[lastCapa].length; j++) {
                    double suma = 0;
                    for (int k = 0; k < nSortides; k++) {
                        suma += deltaSortida[k] * pesosSortida[k][j];
                    }
                    deltasOcultes[lastCapa][j] = suma * derivadaSigmoide(capesOcultes[lastCapa][j]);
                }

                // delta ocultes
                for (int capa = lastCapa - 1; capa >= 0; capa--) {
                    deltasOcultes[capa] = new double[capesOcultes[capa].length];
                    for (int j = 0; j < capesOcultes[capa].length; j++) {
                        double suma = 0;
                        for (int k = 0; k < capesOcultes[capa + 1].length; k++) {
                            suma += deltasOcultes[capa + 1][k] * pesosOcultes[capa + 1][k][j];
                        }
                        deltasOcultes[capa][j] = suma * derivadaSigmoide(capesOcultes[capa][j]);
                    }
                }

                //pesos sortida
                for (int i = 0; i < nSortides; i++) {
                    for (int j = 0; j < pesosSortida[i].length; j++) {
                        pesosSortida[i][j] += deltaEntrenament * deltaSortida[i] * capesOcultes[lastCapa][j];
                    }
                }

                // backprop capes ocultes
                for (int capa = capesOcultes.length - 1; capa > 0; capa--) {
                    for (int i = 0; i < capesOcultes[capa].length; i++) {
                        for (int j = 0; j < pesosOcultes[capa][i].length; j++) {
                            pesosOcultes[capa][i][j] += deltaEntrenament * deltasOcultes[capa][i] * capesOcultes[capa - 1][j];
                        }
                    }
                }
                // entrada -> oculta[0]
                for (int i = 0; i < capesOcultes[0].length; i++) {
                    for (int j = 0; j < pesosOcultes[0][i].length; j++) {
                        pesosOcultes[0][i][j] += deltaEntrenament * deltasOcultes[0][i] * entrada[j];
                    }
                }

                for (double e : errorSortida) {
                    errorTotal += Math.abs(e);
                }
            }
            if (_i % 50000 == 0) {
                Main.getInstance().logText("Època " + _i + " - Error: " + errorTotal);
            }
            if(auto){
                if(Math.abs(lastError-errorTotal) < 0.00000001){
                    Main.getInstance().logText("Època " + _i + " - Error: " + errorTotal);
                    Main.getInstance().logText("Delta Error: " + (lastError-errorTotal));
                    break;
                }
                lastError = errorTotal;
            }
        }
        if(stop){
            Main.getInstance().logText("ATURAT");
        }

        //guardar
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("res/xarxa.bin"))){
            oos.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public double getErrorTotal(){
        return errorTotal;
    }

    public void aturar(){
        stop = true;
    }
}
