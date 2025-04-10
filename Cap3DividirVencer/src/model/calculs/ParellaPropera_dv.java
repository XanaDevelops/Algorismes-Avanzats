package model.calculs;

import model.Dades;
import model.Dades.Resultat;
import model.TipoPunt;
import model.punts.Punt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ParellaPropera_dv extends Calcul {

    public ParellaPropera_dv(Dades dades) {
        super(dades);
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        // Obtenim la llista de punts i els ordenem per la coordenada X.
        List<Punt> punts = new ArrayList<>(dades.getPunts());
        punts.sort(Comparator.comparingInt(Punt::getX));

        Resultat resultat = divideix(punts);

        long time = System.nanoTime() - start;
        dades.afegeixDividirVencer(punts.size(), resultat.p1, resultat.p2, resultat.distancia, time, "min");
    }

    private Resultat divideix(List<Punt> punts) {
        int n = punts.size();
        if (n <= 3) {
            return bruteForce(punts);
        }

        int mid = n / 2;
        List<Punt> left = new ArrayList<>(punts.subList(0, mid));
        List<Punt> right = new ArrayList<>(punts.subList(mid, n));

        Resultat leftResult = divideix(left);
        Resultat rightResult = divideix(right);
        Resultat bestResult = leftResult.distancia < rightResult.distancia ? leftResult : rightResult;


        double dmin = bestResult.distancia;
        int midX = punts.get(mid).getX();

        List<Punt> strip = new ArrayList<>();
        for (Punt p : punts) {
            if (Math.abs(p.getX() - midX) < dmin) {
                strip.add(p);
            }
        }
        strip.sort(Comparator.comparingInt(Punt::getY));

        int stripSize = strip.size();
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize && ((strip.get(j).getY() - strip.get(i).getY()) < dmin); j++) {
                if (tp == TipoPunt.p3D && (Math.abs(strip.get(j).getZ() - strip.get(i).getZ()) >= dmin)) {
                    continue;
                }
                double d = strip.get(i).distancia(strip.get(j));
                if (d < dmin) {
                    bestResult = new Resultat(strip.get(i), strip.get(j), d, 0);
                    dmin = d;
                }
            }
        }
        return bestResult;
    }

    private Resultat bruteForce(List<Punt> punts) {
        double min = Double.MAX_VALUE;
        Punt best1 = null, best2 = null;
        int n = punts.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d = punts.get(i).distancia(punts.get(j));
                if (d < min) {
                    min = d;
                    best1 = punts.get(i);
                    best2 = punts.get(j);
                }
            }
        }
        return new Resultat(best1, best2, min, 0);
    }


}
