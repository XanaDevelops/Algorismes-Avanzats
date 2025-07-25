package model.calculs.maxim;

import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;
import model.Resultat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Estratègia per trobar la parella de punts amb màxima distància.
 *
 * Aquesta implementació està optimitzada per a conjunts de punts generats de forma
 * uniforme dins un cub (3D) o quadrat (2D).
 *
 * En el cas tridimensional, s'utilitza una heurística basada en l'eix X per seleccionar
 * dues franges de punts (una amb X baixos i una amb X alts) i es calcula la distància
 * màxima entre les dues.
 *
 * Aquesta heurística pot no ser precisa si la distribució dels punts no és uniforme.
 */
public class ParellaMaximaUniforme extends Calcul {

    public ParellaMaximaUniforme() {
        super();
    }

    @Override
    public void run() {
        long start = System.nanoTime();

        // Obtenim la parella més llunyana amb estratègia eficient segons el tipus de punt
        Resultat resultat = calculaParellaMaxima();

        long time = System.nanoTime() - start;

        // Guardam el resultat
        dades.afegeixResultat(punts.size(), resultat.getP1(), resultat.getP2(), resultat.getDistancia(), time, TipusCalcul.UNI_MAX);
    }

    private Resultat calculaParellaMaxima() {
        if (punts.size() < 2) return new Resultat(punts.size(), null, null, 0.0, 0);

        switch (this.tp) {
            case p2D:
                return calculaParellaMaxima2D();
            case p3D:
                return calculaParellaMaxima3D();
            default:
                throw new IllegalArgumentException("Tipus de punt no reconegut");
        }
    }

    /**
     * Estratègia per punts 2D: comparació de diagonals.
     */
    private Resultat calculaParellaMaxima2D() {
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

    /**
     * Estratègia per punts 3D amb distribució uniforme:
     * Seleccionam punts extrems a partir de dues projeccions diagonals (X+Y i Y−X)
     * per calcular aproximadament la parella més allunyada en O(n).
     */
    private Resultat calculaParellaMaxima3D() {
        int n = punts.size();
        int k = (int) Math.sqrt(n); // Mida del subconjunt de candidats

        // Ordenació per la primera projecció (X + Y)
        List<Punt> ordenatsDiagonalXY = new ArrayList<>(punts);
        ordenatsDiagonalXY.sort(Comparator.comparingInt(p -> p.getX() + p.getY()));

        // Ordenació per la segona projecció (Y − X)
        List<Punt> ordenatsDiagonalYX = new ArrayList<>(punts);
        ordenatsDiagonalYX.sort(Comparator.comparingInt(p -> -p.getX() + p.getY()));

        // Selecció de franges extrems a cada projecció
        List<Punt> diagonalXYInici = ordenatsDiagonalXY.subList(0, Math.min(k, n));
        List<Punt> diagonalXYFi = ordenatsDiagonalXY.subList(Math.max(0, n - k), n);
        List<Punt> diagonalYXInici = ordenatsDiagonalYX.subList(0, Math.min(k, n));
        List<Punt> diagonalYXFi = ordenatsDiagonalYX.subList(Math.max(0, n - k), n);

        // Càlcul de la millor parella entre les dues diagonals
        Resultat millorXY = millorParellaEntreFranges(diagonalXYInici, diagonalXYFi);
        Resultat millorYX = millorParellaEntreFranges(diagonalYXInici, diagonalYXFi);

        // Retornam la millor parella trobada entre ambdues projeccions
        return (millorXY.getDistancia() > millorYX.getDistancia()) ? millorXY : millorYX;
    }

    /**
     * Donades dues franges de punts, calcula la parella més allunyada entre elles.
     */
    private Resultat millorParellaEntreFranges(List<Punt> franjaA, List<Punt> franjaB) {
        double maxDist = 0.0;
        Punt millorA = null;
        Punt millorB = null;

        for (Punt a : franjaA) {
            for (Punt b : franjaB) {
                double dist = a.distancia(b);
                if (dist > maxDist) {
                    maxDist = dist;
                    millorA = a;
                    millorB = b;
                }
            }
        }

        return new Resultat(punts.size(), millorA, millorB, maxDist, 0);
    }

}