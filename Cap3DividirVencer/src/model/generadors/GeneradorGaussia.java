package model.generadors;

import model.punts.Punt;
import model.punts.Punt2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorGaussia extends Generador {

    private static final Random rand = new Random();
    private final double mitjana;
    private final double desviacio;

    public GeneradorGaussia(int n, int min, int max, double mitjana, double desviacio) {
        super(n, min, max);
        this.mitjana = mitjana;
        this.desviacio = desviacio;
    }

    @Override
    public List<Punt> genera() {
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            int y = limita((int) (rand.nextGaussian() * desviacio + mitjana));
            punts.add(new Punt2D(x, y));
        }
        return punts;
    }

    private int limita(int valor) {
        return Math.max(min, Math.min(max, valor));
    }
}
