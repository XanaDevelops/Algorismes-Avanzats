package model.calculs;

import model.Dades;
import model.Dades.Resultat;
import model.punts.Punt2D;

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
        List<Punt2D> puntsOrdenatsX = new ArrayList<>(punts);
        puntsOrdenatsX.sort(Comparator.comparingInt(Punt2D::getX));
        Resultat resultat = divideix(puntsOrdenatsX);
        t = System.nanoTime() - t;

        dades.afegeixDividirVencer(punts.size(), resultat.p1, resultat.p2, resultat.distancia, t, "min");
    }

    private Resultat divideix(List<Punt2D> punts) {
        int n = punts.size();
        if (n <= 3) {
            return bruteForce(punts);
        }

        int mid = n / 2;
        List<Punt2D> esquerra = punts.subList(0, mid);
        List<Punt2D> dreta = punts.subList(mid, n);

        Resultat minEsquerra = divideix(esquerra);
        Resultat minDreta = divideix(dreta);

        Resultat millor = (minEsquerra.distancia < minDreta.distancia) ? minEsquerra : minDreta;
        double dMin = millor.distancia;

        int midX = punts.get(mid).getX();
        List<Punt2D> franja = new ArrayList<>();
        for (Punt2D p : punts) {
            if (Math.abs(p.getX() - midX) < dMin) {
                franja.add(p);
            }
        }
        franja.sort(Comparator.comparingInt(Punt2D::getY));

        for (int i = 0; i < franja.size(); i++) {
            for (int j = i + 1; j < franja.size() && (franja.get(j).getY() - franja.get(i).getY()) < dMin; j++) {
                double d = franja.get(i).distancia(franja.get(j));
                if (d < millor.distancia) {
                    millor = new Resultat(franja.get(i), franja.get(j), d, 0);
                    dMin = d;
                }
            }
        }

        return millor;
    }

    private Resultat bruteForce(List<Punt2D> punts) {
        double min = Double.MAX_VALUE;
        Punt2D p1 = null, p2 = null;
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
        return new Resultat(p1, p2, min, 0);
    }
}
