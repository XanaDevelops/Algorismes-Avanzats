package model;

import model.punts.Punt;
import vista.Distribucio;

import java.util.*;
import java.util.stream.Collectors;

public class Dades {
    private List<Punt> punts; // llista original de punts
    private TipoPunt tp;
    private Distribucio dist;

    private final TreeMap<Integer, Resultat> resultats;

    public static final int RANG_PUNT = 100000;

    public Dades() {
        this.resultats = new TreeMap<>();
        this.dist = Distribucio.Uniforme;
    }

    public Dades(List<Punt> punts, TipoPunt tp) {
        this();
        this.punts = punts;
        this.tp = tp;
    }

    public void afegeixResultat(int n, Punt p1, Punt p2, double distancia, long tempsNano, TipusCalcul calcul) {
        Resultat r = new Resultat(p1, p2, distancia, tempsNano, dist, tp, calcul);
        resultats.put(n, r);
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

    public void clearPunts() {
        punts.clear();
    }

    public void clearResultats(){
        resultats.clear();
    }

    public Map.Entry<Integer, Resultat> getLastResultat() {
        return resultats.lastEntry();
    }

    public List<Map.Entry<Integer, Resultat>> getResultatsTipus(TipusCalcul tc){
        return resultats.entrySet().stream().filter(x ->x.getValue().getTc() == tc).collect(Collectors.toList());
    }

    public NavigableMap<Integer, Resultat> getResultats() {
        return resultats;
    }

}