package model.calculs;

import controlador.Comunicar;
import model.Dades;
import model.TipoPunt;
import model.punts.Punt;
import model.punts.Punt2D;

import java.util.List;

abstract class Calcul implements Runnable, Comunicar {

    protected final List<Punt> punts;
    protected TipoPunt tp;
    protected Dades dades;

    public Calcul(Dades dades) {
        this.punts = dades.getPunts();
        this.tp = dades.getTp();
        this.dades = dades;
    }
}
