package model.generadors;

import model.punts.Punt;
import model.punts.Punt2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorUniforme extends Generador {

    private static final Random rand = new Random();

    public GeneradorUniforme(int n, int min, int max) {
        super(n, min, max);
    }

    @Override
    public List<Punt> genera() {
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = rand.nextInt(max - min + 1) + min;
            int y = rand.nextInt(max - min + 1) + min;
            punts.add(new Punt2D(x, y));
        }
        return punts;
    }
}
