package model.calculs.maxim;

import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;
import model.Resultat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParellaMaximaUniforme extends Calcul {

    public ParellaMaximaUniforme() {
        super();
    }

    @Override
    public void run() {
        long start = System.nanoTime();

        // Obtenim la llista de punts
        List<Punt> punts = new ArrayList<>(dades.getPunts());

        // Obtenim la parella més llunyana amb estratègia eficient
        Resultat resultat = calculaParellaMaxima(punts);

        long time = System.nanoTime() - start;

        // Guardam el resultat
        dades.afegeixResultat(punts.size(), resultat.getP1(), resultat.getP2(), resultat.getDistancia(), time, TipusCalcul.UNI_MAX);
    }

    private Resultat calculaParellaMaxima(List<Punt> punts) {
        if (punts.size() < 2) {
            return new Resultat(punts.size(), null, null, 0.0, 0);
        }

        Punt adaltEsquerra = Collections.min(punts, Comparator.comparingInt(p -> p.getX() + p.getY()));
        Punt adaltDreta    = Collections.min(punts, Comparator.comparingInt(p -> -p.getX() + p.getY()));
        Punt abaixEsquerra = Collections.min(punts, Comparator.comparingInt(p -> p.getX() - p.getY()));
        Punt abaixDreta    = Collections.min(punts, Comparator.comparingInt(p -> -p.getX() - p.getY()));

        // Comparam les dues diagonals oposades
        double dist1 = adaltEsquerra.distancia(abaixDreta);
        double dist2 = adaltDreta.distancia(abaixEsquerra);

        if (dist1 >= dist2) {
            return new Resultat(punts.size(), adaltEsquerra, abaixDreta, dist1, 0);
        } else {
            return new Resultat(punts.size(), adaltDreta, abaixEsquerra, dist2, 0);
        }
    }

    public void comunicar(String s) {
    }
}

