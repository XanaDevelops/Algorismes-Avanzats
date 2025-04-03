package controlador;


import model.punts.Punt;
import model.punts.Punt2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main implements Comunicar {

    private List<Punt> punts;

    public static void main(String[] args) {
        (new Main()).init();
    }

    private void init() {
        punts = new ArrayList<>();
    }

    @Override
    public void comunicar(String s) {
        if (s.startsWith("generar")) {
            String[] res = s.split(":");
            int num = Integer.parseInt(res[1]);
            Random r = new Random();
            punts.clear();
            for (int i = 0; i < num; i++)
                punts.add(new Punt2D(r.nextInt() * 600, r.nextInt() * 500));
        }
    }
}