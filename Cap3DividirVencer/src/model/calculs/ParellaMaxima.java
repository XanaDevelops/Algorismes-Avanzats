package model.calculs;

import model.Dades;
import model.punts.Punt;
import model.Dades.Resultat;

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
public class ParellaMaxima extends Calcul {

    public ParellaMaxima(Dades dades) {
        super();
    }

    @Override
    public void run() {
        long start = System.nanoTime();

        // Obtenim la parella més llunyana amb estratègia eficient segons el tipus de punt
        Resultat resultat = calculaParellaMaxima();

        long time = System.nanoTime() - start;

        // Guardam el resultat
        dades.afegeixDistMax(punts.size(), resultat.getP1(), resultat.getP2(), resultat.getDistancia(), time, "max");
    }

    private Resultat calculaParellaMaxima() {
        if (punts.size() < 2) return new Resultat(null, null, 0.0, 0);

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

        double dist1 = adaltEsquerra.distancia(abaixDreta);
        double dist2 = adaltDreta.distancia(abaixEsquerra);

        if (dist1 >= dist2) {
            return new Resultat(adaltEsquerra, abaixDreta, dist1, 0);
        } else {
            return new Resultat(adaltDreta, abaixEsquerra, dist2, 0);
        }
    }

    /**
     * Estratègia per punts 3D: comparació entre franges extrems en X.
     */
    private Resultat calculaParellaMaxima3D() {
        int n = punts.size();
        int k = (int) Math.sqrt(n);

        List<Punt> ordenatsX = new ArrayList<>(punts);
        ordenatsX.sort(Comparator.comparingInt(Punt::getX));

        List<Punt> franjaEsquerra = ordenatsX.subList(0, Math.min(k, n));
        List<Punt> franjaDreta = ordenatsX.subList(Math.max(0, n - k), n);

        double maxDist = 0.0;
        Punt millorA = null;
        Punt millorB = null;

        for (Punt a : franjaEsquerra) {
            for (Punt b : franjaDreta) {
                double dist = a.distancia(b);
                if (dist > maxDist) {
                    maxDist = dist;
                    millorA = a;
                    millorB = b;
                }
            }
        }

        return new Resultat(millorA, millorB, maxDist, 0);
    }
}
