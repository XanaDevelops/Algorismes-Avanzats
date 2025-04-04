package model;

import model.punts.Punt;

import java.util.ArrayList;
import java.util.List;

public class Dades {
    private final List<Resultat> forcaBruta;
    private final List<Resultat> dividirVencer;
    private  List<Punt> punts;
    public Dades() {
        this.forcaBruta = new ArrayList<>();;
        this.dividirVencer = new ArrayList<>();;
        this.punts = new ArrayList<>();
    }

    public void setPunts(List<Punt> punts) {
        this.punts= punts;

    }
    public List<Punt> getPunts() {
        return punts;
    }


    public List<Resultat> getForcaBruta() {
        return forcaBruta;
    }

    public List<Resultat> getDividirVencer() {
        return dividirVencer;
    }

    public void clearForcaBruta() {
        forcaBruta.clear();
    }

    public void clearDividirVencer() {
        dividirVencer.clear();
    }

    public void addForcaBruta(Punt p1, Punt p2, double distancia, long temps, String tipus) {
        Resultat res = new Resultat(p1, p2, distancia, temps, tipus);
        forcaBruta.add(res);
    }

    public void addDividirVencer(Punt p1, Punt p2, double distancia, long temps, String tipus) {
        Resultat res = new Resultat(p1, p2, distancia, temps, tipus);
        dividirVencer.add(res);
    }

    public class Resultat {
        public Punt p1, p2;
        public double distancia;
        public long temps;
        public String tipus;

        public Resultat(Punt p1, Punt p2, double distancia, long temps, String tipus) {
            this.p1 = p1;
            this.p2 = p2;
            this.distancia = distancia;
            this.tipus = tipus;
            this.temps = temps;
        }
    }



}