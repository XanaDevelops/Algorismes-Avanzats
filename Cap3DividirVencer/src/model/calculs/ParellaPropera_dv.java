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
        long t = System.nanoTime();
        // Obtenim la llista de punts i els ordenem per la coordenada X.
        List<Punt> punts = new ArrayList<>(dades.getPunts());
        punts.sort(Comparator.comparingInt(Punt::getX));

        Resultat resultat = divideix(punts);

        long temps = System.nanoTime() - t;
        dades.afegeixDividirVencer(punts.size(), resultat.p1, resultat.p2, resultat.distancia, temps, "min");
    }

    private Resultat divideix(List<Punt> punts) {
        int n = punts.size();

        if (n <= 3) {
            return ParellaPropera_fb.calc((ArrayList<Punt>) punts);
        }

        int mid = n / 2;
        List<Punt> esquerra = new ArrayList<>(punts.subList(0, mid));
        List<Punt> dreta = new ArrayList<>(punts.subList(mid, n));

        Resultat esquerraR = divideix(esquerra);
        Resultat dretaR = divideix(dreta);
        Resultat bestResult = esquerraR.distancia < dretaR.distancia ? esquerraR : dretaR;

        double dmin = bestResult.distancia;
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

                if (es3D) {
                    if (diffY >= dmin && diffZ >= dmin) break;
                } else {
                    if (diffY >= dmin) break;
                }

                double d = areaPossible.get(i).distancia(areaPossible.get(j));
                if (d < dmin) {
                    bestResult = new Resultat(areaPossible.get(i), areaPossible.get(j), d, 0);
                    dmin = d;
                }
            }
        }
        return bestResult;
    }
}