package model.calculs;

import model.Dades;
import model.punts.Punt;

import java.util.ArrayList;
import java.util.List;

abstract class Calcul implements Runnable {


    protected Dades dades;
    protected List<Punt> punts;
    public Calcul( Dades dades) {
        this.dades = dades;
        this.punts = dades.getPunts();
    }
}
