package model.calculs;

import model.Dades;
import model.punts.Punt;

import java.util.ArrayList;


public class ParellaPropera_fb extends Calcul {

        public ParellaPropera_fb(Dades dades) {
            super(dades);
        }

        @Override
        public void run() {
                Dades.Resultat res = calc((ArrayList<Punt>) punts);
                dades.afegeixForcaBruta(punts.size(), res.p1, res.p2, res.distancia, res.tempsNano,"min");
        }

        public static Dades.Resultat calc(ArrayList<Punt> punts){
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
                return new Dades.Resultat(p1, p2, min, t, "min");
        }
}
