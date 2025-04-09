package model;

import model.punts.Punt;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Dades {

    private final List<Punt> punts; // llista original de punts
    private final TreeMap<Integer, Resultat> forcaBruta;
    private final TreeMap<Integer, Resultat> dividirVencer;

    public Dades(List<Punt> punts) {
        this.punts = punts;
        this.forcaBruta = new TreeMap<>();
        this.dividirVencer = new TreeMap<>();
    }

    public NavigableMap<Integer, Resultat> getForcaBruta() {
        return forcaBruta;
    }

    public NavigableMap<Integer, Resultat> getDividirVencer() {
        return dividirVencer;
    }

    public void clearForcaBruta() {
        forcaBruta.clear();
    }

    public void clearDividirVencer() {
        dividirVencer.clear();
    }

    public void afegeixForcaBruta(int n, Punt p1, Punt p2, double distancia, long tempsNano, String tipus) {
        Resultat r = new Resultat(p1, p2, distancia, tempsNano, tipus);
        forcaBruta.put(n, r);
    }

    public void afegeixDividirVencer(int n, Punt p1, Punt p2, double distancia, long tempsNano, String tipus) {
        Resultat r = new Resultat(p1, p2, distancia, tempsNano, tipus);
        dividirVencer.put(n, r);
    }

    public List<Punt> getPunts() {
        return punts;
    }

    public class Resultat {
        private final long tempsNano;
        private final Punt p1;
        private final Punt p2;
        private final double distancia;
        private final String tipus; // "curta", "llarga", "aproximada", etc.

        public Resultat(Punt p1, Punt p2, double distancia, long tempsNano, String tipus) {
            this.p1 = p1;
            this.p2 = p2;
            this.distancia = distancia;
            this.tempsNano = tempsNano;
            this.tipus = tipus;
        }
    }



}