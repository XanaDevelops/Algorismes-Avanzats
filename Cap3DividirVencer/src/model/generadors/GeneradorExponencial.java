package model.generadors;

import model.Dades;
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

    private double exponencial(int x) {
        double exp = -Math.log(1.0 - (double) x / Dades.RANG_PUNT);
        return exp * ((double) max / 2) / lambda;
    }


    private int limita(int valor) {
        int maxim = Math.max(min, Math.min(max, valor));
        if (maxim <0){
            maxim *= -1;
        }
        return maxim;
    }

    @Override
    public List<Punt> genera2D() {
        // Generació 2D amb exponencial
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < n; i++) {

            int x = limita(rand.nextInt(max / 2));

            int y = limita((int) exponencial(x));

            punts.add(new Punt2D(x, y));
        }
        return punts;
    }

//    @Override
//    public List<Punt> genera3D() {
//        List<Punt> punts = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            int x = (limita (rand.nextInt(max/2)));
//
//            int y =limita((int) exponencial(x ));
//            int z = limita((int) exponencial(y+x));
//
//
//            punts.add(new Punt3D(x, y, z));
//        }
//        return punts;
//    }


    @Override
    public List<Punt> genera3D() {
        List<Punt> punts = new ArrayList<>();
        int spread = max / rand.nextInt(4, 16); //parametre de dispersió
        System.out.println(spread);
        for (int i = 0; i < n; i++) {
            int x = limita(rand.nextInt(max ));

            int y = limita(limita((int) exponencial(x)) );
            int z = limita(x+ rand.nextInt(spread*2));
//
//
//            int z = limita(x + (rand.nextInt(2 * spread) - spread/2 ));

//            System.out.println("x: " + x + " y: " + y + " z: " + z);
            punts.add(new Punt3D(x, y, z));
        }
        return punts;
    }


}





