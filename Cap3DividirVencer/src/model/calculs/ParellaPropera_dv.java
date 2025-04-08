package model.calculs;

import model.Dades;
import model.punts.Punt;
import model.punts.Punt2D;

import java.util.*;

/**
 * Classe que implementa l'algorisme Divideix i Venceràs per calcular
 * la parella de punts més pròxima dins d’un conjunt 2D.
 * Complexitat: O(n·log n)
 */
public class ParellaPropera_dv extends Calcul {

    public ParellaPropera_dv(List<Punt> punts, Dades dades) {
        super(punts, dades);
    }

    @Override
    public void run() {
        // Convertim sa llista genèrica de Punt a Punt2D
        List<Punt2D> punts2D = punts.stream()
                .map(p -> (Punt2D) p)
                .toList();

        // Cream còpies de la llista per ordenar segons X i segons Y
        List<Punt2D> perX = new ArrayList<>(punts2D);
        List<Punt2D> perY = new ArrayList<>(punts2D);

        // Ordenació prèvia per coordenada X i Y
        perX.sort(Comparator.comparingInt(p -> p.x));
        perY.sort(Comparator.comparingInt(p -> p.y));

        // Mesura del temps d'execució
        long t = System.nanoTime();
        ResultatMinim millor = recursiu(perX, perY);
        t = System.nanoTime() - t;

        // Enregistram el resultat en l’objecte Dades
        dades.addDividirVencer(millor.p1, millor.p2, millor.distancia, t, "min");
    }

    /**
     * Mètode recursiu principal que aplica l’estratègia Divideix i Venceràs
     */
    private ResultatMinim recursiu(List<Punt2D> perX, List<Punt2D> perY) {
        int n = perX.size();

        // Cas base: si hi ha 3 punts o menys, fem força bruta
        if (n <= 3) {
            return calculaMinimaDistanciaForcaBruta(perX);
        }

        // Dividim el conjunt en dues meitats segons X
        int mid = n / 2;
        Punt2D puntMig = perX.get(mid);

        List<Punt2D> esquerraX = perX.subList(0, mid);
        List<Punt2D> dretaX = perX.subList(mid, n);

        // Dividim també la llista ordenada per Y en dos subconjunts
        List<Punt2D> esquerraY = new ArrayList<>();
        List<Punt2D> dretaY = new ArrayList<>();
        for (Punt2D p : perY) {
            if (p.x <= puntMig.x) esquerraY.add(p);
            else dretaY.add(p);
        }

        // Recursió en cada meitat
        ResultatMinim dEsq = recursiu(esquerraX, esquerraY);
        ResultatMinim dDre = recursiu(dretaX, dretaY);

        // Determinam la millor distància entre les dues meitats
        ResultatMinim millor = (dEsq.distancia < dDre.distancia) ? dEsq : dDre;

        // Construcció de la franja central amb punts a menys de d del punt de tall
        List<Punt2D> franja = new ArrayList<>();
        for (Punt2D p : perY) {
            if (Math.abs(p.x - puntMig.x) < millor.distancia) {
                franja.add(p);
            }
        }

        // Comparació entre punts de la franja (com a màxim 6 següents)
        for (int i = 0; i < franja.size(); i++) {
            for (int j = i + 1; j < franja.size() && j < i + 7; j++) {
                double d = franja.get(i).distancia(franja.get(j));
                if (d < millor.distancia) {
                    millor = new ResultatMinim(franja.get(i), franja.get(j), d);
                }
            }
        }

        return millor;
    }

    /**
     * Càlcul de la parella més propera mitjançant força bruta (complexitat O(n²))
     */
    private ResultatMinim calculaMinimaDistanciaForcaBruta(List<Punt2D> punts) {
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

        return new ResultatMinim(p1, p2, min);
    }

    /**
     * Classe interna auxiliar per emmagatzemar la millor parella trobada
     */
    private static class ResultatMinim {
        public final Punt2D p1, p2;
        public final double distancia;

        public ResultatMinim(Punt2D p1, Punt2D p2, double distancia) {
            this.p1 = p1;
            this.p2 = p2;
            this.distancia = distancia;
        }
    }
}
