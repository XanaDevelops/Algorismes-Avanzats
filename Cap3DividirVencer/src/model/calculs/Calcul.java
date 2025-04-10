package model.calculs;

import model.Dades;
import model.TipoPunt;
import model.punts.Punt;
import model.punts.Punt2D;

import java.util.List;

public abstract class Calcul implements Runnable {

    protected final List<Punt> punts;
    protected TipoPunt tp;
    protected Dades dades;

    public Calcul(Dades dades) {
        this.punts = dades.getPunts();
        this.tp = dades.getTp();
        this.dades = dades;
    }
}
