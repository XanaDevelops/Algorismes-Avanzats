package model;

import model.punts.Punt;
import vista.Distribucio;

import java.util.*;
import java.util.stream.Collectors;

public class Dades {
    private List<Punt> punts; // llista original de punts
    private TipoPunt tp;
    private Distribucio dist;
    private TipusCalcul tipusCalcul;

    private final TreeMap<TipusCalcul, List<Resultat>> resultats;

    public static final int RANG_PUNT = 100000;

    public Dades() {
        this.resultats = new TreeMap<>();
        this.dist = Distribucio.Uniforme;
        this.tipusCalcul = TipusCalcul.FB_MIN;
    }

    public Dades(List<Punt> punts, TipoPunt tp) {
        this();
        this.punts = punts;
        this.tp = tp;
    }

    public void afegeixResultat(int n, Punt p1, Punt p2, double distancia, long tempsNano, TipusCalcul calcul) {
        Resultat r = new Resultat(n, p1, p2, distancia, tempsNano, dist, tp, calcul);
        List<Resultat> l = resultats.getOrDefault(calcul, new ArrayList<>());
        l.add(r);
        this.tipusCalcul = calcul;
        resultats.put(calcul, l);
    }

    public void setTp(TipoPunt tp) {
        this.tp = tp;
    }

    public TipoPunt getTp() {
        return tp;
    }

    public void setPunts(List<Punt> punts) {
        this.punts = punts;
    }

    public List<Punt> getPunts() {
        return punts;
    }

    public TipusCalcul getTipusCalcul() {
        return tipusCalcul;
    }

    public void setTipusCalcul(TipusCalcul tipusCalcul) {
        this.tipusCalcul = tipusCalcul;
    }

    public Distribucio getDist() {
        return dist;
    }

    public void setDist(Distribucio dist) {
        this.dist = dist;
    }

    public void clearPunts() {
        punts.clear();
    }

    public void clearResultats(){
        resultats.clear();
    }

    public Resultat getLastResultat() {
        return resultats.get(tipusCalcul).getLast();
    }

    public NavigableMap<TipusCalcul, List<Resultat>> getResultats() {
        return resultats;
    }

}