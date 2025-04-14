package model;

import model.punts.Punt;
import vista.Dimensio;
import vista.Distribucio;

public class Resultat {
    private final long tempsNano;
    private final Punt p1;
    private final Punt p2;
    private final double distancia;
    private final Distribucio dist;
    private final TipoPunt tp;
    private final TipusCalcul tc;


    public Resultat(Punt p1, Punt p2, double distancia, long tempsNano) {
        this.p1 = p1;
        this.p2 = p2;
        this.distancia = distancia;
        this.tempsNano = tempsNano;
        this.dist = null;
        this.tp = null;
        this.tc = null;
    }

    public Resultat(Punt p1, Punt p2, double distancia, long tempsNano, Distribucio dist, TipoPunt tp, TipusCalcul tc) {
        this.p1 = p1;
        this.p2 = p2;
        this.distancia = distancia;
        this.tempsNano = tempsNano;
        this.dist = dist;
        this.tp = tp;
        this.tc = tc;
    }



    public long getTempsNano() {
        return tempsNano;
    }

    public Punt getP1() {
        return p1;
    }

    public Punt getP2() {
        return p2;
    }

    public double getDistancia() {
        return distancia;
    }

    public Distribucio getDist() {
        return dist;
    }

    public TipoPunt getTp() {
        return tp;
    }

    public TipusCalcul getTc() {
        return tc;
    }

    @Override
    public String toString() {
        return "Resultat{" +
                "tempsNano=" + tempsNano +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", distancia=" + distancia +
                ", tipus='" + tc + '\'' +
                '}';
    }

}
