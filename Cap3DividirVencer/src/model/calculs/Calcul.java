package model.calculs;

import model.Dades;
import model.punts.Punt2D;

import java.util.List;

abstract class Calcul implements Runnable {


    protected Dades dades;
    protected List<Punt2D> punts;
    public Calcul(List<Punt2D> punts, Dades dades) {
        this.dades = dades;
        this.punts = dades.getPunts();
    }
}
