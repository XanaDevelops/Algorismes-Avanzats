package model.generadors;

import model.punts.Punt;
import model.punts.Punt2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorExponencial extends Generador {

    private static final Random rand = new Random();
    private final double lambda;  // Paràmetre de la distribució exponencial

    public GeneradorExponencial(int n, int min, int max, double lambda) {
        super(n, min, max);
        this.lambda = lambda;
    }

    @Override
    public List<Punt> genera() {
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = limita((int) exponencial(lambda));
            int y = limita((int) exponencial(lambda));
            punts.add(new Punt2D(x, y));
        }
        return punts;
    }

    private double exponencial(double lambda) {
        return -Math.log(1 - rand.nextDouble()) / lambda;
    }

    private int limita(int valor) {
        return Math.max(min, Math.min(max, valor));
    }
}

