package model;

import model.punts.Punt;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Dades {


    private List<Punt> punts; // llista original de punts
    private TipoPunt tp;

    private final TreeMap<Integer, Resultat> forcaBruta;
    private final TreeMap<Integer, Resultat> dividirVencer;

    private final TreeMap<Integer, Resultat> kd;

    public Dades() {
        this.forcaBruta = new TreeMap<>();
        this.dividirVencer = new TreeMap<>();
        this.kd = new TreeMap<>();
    }

    public Dades(List<Punt> punts, TipoPunt tp) {
        this.punts = punts;
        this.tp = tp;
        this.forcaBruta = new TreeMap<>();
        this.dividirVencer = new TreeMap<>();
        this.kd = new TreeMap<>();
    }

    public NavigableMap<Integer, Resultat> getKd() {
        return kd;
    }


    public void setTp(TipoPunt tp) {
        this.tp = tp;
    }

    public void setPunts(List<Punt> punts) {
        this.punts = punts;
    }

    public TipoPunt getTp() {
        return tp;
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

    public Map.Entry<Integer, Resultat> getLastResultatFB() {
        return forcaBruta.lastEntry();
    }

    public Map.Entry<Integer, Resultat> getLastResultatDV() {
        return dividirVencer.lastEntry();
    }

    public Map.Entry<Integer, Resultat> getLastResultatKD() {
        return kd.lastEntry();
    }

    public void afegeixForcaBruta(int n, Punt p1, Punt p2, double distancia, long tempsNano, String tipus) {
        Resultat r = new Resultat(p1, p2, distancia, tempsNano, tipus);
        forcaBruta.put(n, r);
    }

    public void afegeixDividirVencer(int n, Punt p1, Punt p2, double distancia, long tempsNano, String tipus) {
        Resultat r = new Resultat(p1, p2, distancia, tempsNano, tipus);
        dividirVencer.put(n, r);
    }

    public void afegeixKD(int n, Punt p1, Punt p2, double distancia, long tempsNano, String tipus) {
        Resultat r = new Resultat(p1, p2, distancia, tempsNano, tipus);
        kd.put(n, r);
    }

    public List<Punt> getPunts() {
        return punts;
    }

    public void clearPunts() {
        punts.clear();
    }

    public static class Resultat {
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

        // Constructor auxiliar sense tipus (opcional)
        public Resultat(Punt p1, Punt p2, double distancia, long tempsNano) {
            this(p1, p2, distancia, tempsNano, "min");
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

        public String getTipus() {
            return tipus;
        }

        @Override
        public String toString() {
            return "Resultat{" +
                    "tempsNano=" + tempsNano +
                    ", p1=" + p1 +
                    ", p2=" + p2 +
                    ", distancia=" + distancia +
                    ", tipus='" + tipus + '\'' +
                    '}';
        }
    }

}