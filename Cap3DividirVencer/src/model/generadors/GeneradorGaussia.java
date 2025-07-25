package model.generadors;

import controlador.Main;
import model.punts.Punt;
import model.punts.Punt2D;
import model.punts.Punt3D;
import vista.Distribucio;

import java.util.ArrayList;
import java.util.List;

public class GeneradorGaussia extends Generador {

    private final double mitjana;
    private final double desviacio;

    public GeneradorGaussia(int n, int min, int max, double mitjana, double desviacio) {
        super(n, min, max);
        this.mitjana = mitjana;
        this.desviacio = desviacio;

        Main.instance.getDades().setDist(Distribucio.Gaussiana);

    }

    public GeneradorGaussia(int n, int min, int max) {
        super(n, min, max);
        this.mitjana = 0;
        this.desviacio = 1;

        Main.instance.getDades().setDist(Distribucio.Gaussiana);

    }

    private int limita(int valor) {
        return Math.max(min, Math.min(max, valor));
    }

    @Override
    public List<Punt> genera2D(int numPunts) {
        // Generació 2D amb gaussiana
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < numPunts; i++) {
            int x = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            int y = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            punts.add(new Punt2D(x, y));
        }
        return punts;
    }

    @Override
    public List<Punt> genera3D(int numPunts) {
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < numPunts; i++) {
            int x = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            int y = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            int z = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            punts.add(new Punt3D(x, y, z));
        }
        return punts;
    }

}
