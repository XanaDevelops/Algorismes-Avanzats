package model.generadors;

import controlador.Main;
import model.punts.Punt;
import model.punts.Punt2D;
import model.punts.Punt3D;
import vista.Distribucio;

import java.util.ArrayList;
import java.util.List;

public class GeneradorUniforme extends Generador {


    public GeneradorUniforme(int n, int min, int max) {
        super(n, min, max);
        Main.instance.getDades().setDist(Distribucio.Uniforme);

    }

    @Override
    public List<Punt> genera2D(int numPunts) {
        // Generació 2D amb uniforme
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < numPunts; i++) {
            int x = rand.nextInt(max - min + 1) + min;
            int y = rand.nextInt(max - min + 1) + min;
            punts.add(new Punt2D(x, y));
        }
        return punts;
    }

    @Override
    public List<Punt> genera3D(int numPunts) {
        List<Punt> punts = new ArrayList<>();
        for (int i = 0; i < numPunts; i++) {
            int x = rand.nextInt(max - min + 1) + min;
            int y = rand.nextInt(max - min + 1) + min;
            int z = rand.nextInt(max - min + 1) + min;
            punts.add(new Punt3D(x, y, z));
        }
        return punts;
    }
}
