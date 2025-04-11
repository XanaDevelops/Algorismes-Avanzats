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
        int midX = punts.get(mid).getX();
        int midZ = punts.get(mid).getZ();


        List<Punt> areaPossible = new ArrayList<>();
        boolean es3D = this.tp == TipoPunt.p3D;

        for (Punt p : punts) {
            boolean properX = Math.abs(p.getX() - midX) < dmin;
            boolean properZ = !es3D || Math.abs(p.getZ() - midZ) < dmin;
            if (properX && properZ) {
                areaPossible.add(p);
            }
        }

        areaPossible.sort(Comparator.comparingInt(Punt::getY));
        int mida = areaPossible.size();
        for (int i = 0; i < mida; i++) {
            for (int j = i + 1; j < mida && (areaPossible.get(j).getY() - areaPossible.get(i).getY()) < dmin; j++) {
                if (es3D && Math.abs(areaPossible.get(j).getZ() - areaPossible.get(i).getZ()) >= dmin) {
                    continue;
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