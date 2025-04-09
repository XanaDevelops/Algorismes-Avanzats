package model.calculs;

import model.Dades;
import model.punts.Punt;

import java.util.List;

abstract class Calcul implements Runnable {

    protected final List<Punt> punts;
    protected Dades dades;

    public Calcul(Dades dades) {
        this.punts = dades.getPunts();
        this.dades = dades;
    }
}
