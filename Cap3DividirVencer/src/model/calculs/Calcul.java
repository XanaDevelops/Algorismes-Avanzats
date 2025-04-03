package model.calculs;

import model.Dades;
import model.punts.Punt;

import java.util.List;

abstract class Calcul implements Runnable {

    protected final List<Punt> punts;
    protected Dades dades;

    public Calcul(List<Punt> punts, Dades dades) {
        this.punts = punts;
        this.dades = dades;
    }
}
