package model.calculs;

import model.Dades;
import model.punts.Punt;
import model.punts.Punt2D;

import java.util.List;

abstract class Calcul implements Runnable {

    protected final List<Punt2D> punts;
    protected Dades dades;

    public Calcul(Dades dades) {
        this.punts = dades.getPunts();
        this.dades = dades;
    }
}
