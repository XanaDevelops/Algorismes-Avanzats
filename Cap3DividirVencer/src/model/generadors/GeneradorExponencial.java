package model.generadors;

import model.punts.Punt;
import model.punts.Punt2D;
import model.punts.Punt3D;

import java.util.ArrayList;
import java.util.List;

public class GeneradorExponencial extends Generador {

    private final double lambda;  // Paràmetre de la distribució exponencial

    public GeneradorExponencial(int n, int min, int max, double lambda) {
        super(n, min, max);
        this.lambda = lambda;
    }

//    public GeneradorExponencial(int n, int min, int max) {
//        super(n, min, max);
//        this.lambda = Math.E;
//    }

    private double exponencial(double lambda) {
        return -Math.log(1 - rand.nextDouble()) / lambda;
    }

    private int limita(int valor) {
        return Math.max(min, Math.min(max, valor));
    }

    @Override
    public List<Punt> genera2D() {
        // Generació 2D amb exponencial
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = limita((int) exponencial(lambda));
            int y = limita((int) exponencial(lambda));
            punts.add(new Punt2D(x, y));
        }
        return punts;
    }

    @Override
    public List<Punt> genera3D() {
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = limita((int) exponencial(lambda));
            int y = limita((int) exponencial(lambda));
            int z = limita((int) exponencial(lambda));
            punts.add(new Punt3D(x, y, z));
        }
        return punts;
    }

}





