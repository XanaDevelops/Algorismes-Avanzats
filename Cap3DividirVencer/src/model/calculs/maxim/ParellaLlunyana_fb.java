package model.calculs.maxim;

import model.Resultat;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;

import java.util.ArrayList;
import java.util.List;


public class ParellaLlunyana_fb extends Calcul {

        public ParellaLlunyana_fb() {
            super();
        }

        @Override
        public void run() {
                Resultat res = calc((ArrayList<Punt>) punts);
                dades.afegeixResultat(punts.size(), res.getP1(), res.getP2(), res.getDistancia(), res.getTempsNano(), TipusCalcul.FB_MAX);
        }

        public static Resultat calc(List<Punt> punts){
                double max = Double.MIN_VALUE;
                Punt p1 = null, p2 = null;
                long t = System.nanoTime();
                for (int i = 0; i < punts.size(); i++) {
                        for (int j = i + 1; j < punts.size(); j++) {

                                double d = punts.get(i).distancia(punts.get(j));

                                if (d > max && (!punts.get(j).equals(punts.get(i)))) { //per si hi ha punts duplicats
                                        max = d;
                                        p1 = punts.get(i);
                                        p2 = punts.get(j);
                                }
                        }
                }
                t = System.nanoTime() - t;
                return new Resultat(punts.size(), p1, p2, max, t);
        }


}
