package model.calculs.minim;

import controlador.Main;
import model.Resultat;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class ParellaPropera_fb extends Calcul {

        public ParellaPropera_fb() {
            super();
        }

        @Override
        public void run() {
                Resultat res = calc((ArrayList<Punt>) punts);
                dades.afegeixResultat(punts.size(), res.getP1(), res.getP2(), res.getDistancia(), res.getTempsNano(), TipusCalcul.FB_MIN);
        }

        public static Resultat calc(ArrayList<Punt> punts){

                int n = punts.size();

                List<Future<Resultat>> futures = new ArrayList<>();
                long t = System.nanoTime();

                for (int i = 0; i < n; i++) {
                        final int ii = i;
                        futures.add(Main.instance.getExecutor().submit(() -> {
                                double min = Double.MAX_VALUE;
                                Punt p1 = null, p2 = null;
                                for (int j = ii + 1; j < n; j++) {
                                        double d = punts.get(ii).distancia(punts.get(j));
                                        if (d < min) {
                                                min = d;
                                                p1 = punts.get(ii);
                                                p2 = punts.get(j);
                                        }
                                }

                                return new Resultat(n, p1, p2, min,0 );
                        }));
                }
                t = System.nanoTime() - t;

                Resultat millor = futures.stream()
                        .map(f -> {
                                try {
                                        return f.get();
                                } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                        return null;
                                }
                        })
                        .filter(Objects::nonNull)
                        .min(Comparator.comparingDouble(Resultat::getDistancia))
                        .orElseThrow();

                //el temps és un atribut final, ca crear un nou resultat per assignar-li el temps total
                return new Resultat(
                        millor.getN(),
                        millor.getP1(),
                        millor.getP2(),
                        millor.getDistancia(),
                        t
                );

        }


}
