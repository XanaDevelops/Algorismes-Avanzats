package model.calculs;

import model.Dades;
import model.punts.Punt;


public class ParellaPropera_fb extends Calcul {

        public ParellaPropera_fb(Dades dades) {
            super(dades);
        }

        @Override
        public void run() {
                double min = Double.MAX_VALUE;
                Punt p1 = null, p2 = null;
                long t = System.nanoTime();
                for (int i = 0; i < punts.size(); i++) {
                        for (int j = i + 1; j < punts.size(); j++) {
                                double d = punts.get(i).distancia(punts.get(j));
                                if (d < min) {
                                        min = d;
                                        p1 = punts.get(i);
                                        p2 = punts.get(j);
                                }
                        }
                }
                t = System.nanoTime() - t;
                dades.afegeixForcaBruta(punts.size(), p1, p2, min,t,"min");
        }


}
