package model.calculs.minim;

import model.Resultat;
import model.TipoPunt;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ParellaPropera_dv extends Calcul {

    public ParellaPropera_dv() {
        super();
    }

    @Override
    public void run() {
        long t = System.nanoTime();
        // Obtenim la llista de punts i els ordenem per la coordenada X.
        List<Punt> punts = new ArrayList<>(dades.getPunts());
        punts.sort(Comparator.comparingInt(Punt::getX));

        Resultat resultat = divideix(punts);


        long time = System.nanoTime() - t;
        dades.afegeixResultat(punts.size(), resultat.getP1(), resultat.getP2(), resultat.getDistancia(), time, TipusCalcul.DV_MIN);
    }

    private Resultat divideix(List<Punt> punts) {
        int n = punts.size();

        if (n <= 3) {
            return ParellaPropera_fb.calc((ArrayList<Punt>) punts);
        }

        int mid = n / 2;


        List<Punt> left = new ArrayList<>(punts.subList(0, mid));
        List<Punt> right = new ArrayList<>(punts.subList(mid, n));

        Resultat leftResult = divideix(left);
        Resultat rightResult = divideix(right);
        Resultat bestResult = leftResult.getDistancia() < rightResult.getDistancia() ? leftResult : rightResult;



        double dmin = bestResult.getDistancia();
        double midX = punts.get(mid).getX();

        boolean es3D = this.tp == TipoPunt.p3D;

        List<Punt> areaPossible = new ArrayList<>();
        for (Punt p : punts) {
            boolean areaX = Math.abs(p.getX() - midX) < dmin;
            if (areaX ) {
                areaPossible.add(p);
            }
        }


        areaPossible.sort(Comparator.comparingInt(Punt::getY));


        int m = areaPossible.size();
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                double diffY = areaPossible.get(j).getY() - areaPossible.get(i).getY();
                double diffZ = Math.abs(areaPossible.get(j).getZ() - areaPossible.get(i).getZ());

                if (areaPossible.get(i).equals(areaPossible.get(j))) { //punts duplicats
                    continue;
                }
                if (es3D) {
                    if (diffY >= dmin && diffZ >= dmin) break;
                } else {
                    if (diffY >= dmin) break;
                }

                double d = areaPossible.get(i).distancia(areaPossible.get(j));
                if (d < dmin) {
                    bestResult = new Resultat(areaPossible.get(i), areaPossible.get(j), d, 0, null, null, null);
                    dmin = d;
                }
            }
        }
        return bestResult;
    }
}