package model.calculs;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;
import model.TipoPunt;
import model.punts.Punt;
import model.punts.Punt2D;

import java.util.ArrayList;
import java.util.List;


public abstract class Calcul implements Runnable {
    protected final List<Punt> punts;
    protected TipoPunt tp;
    protected Dades dades;

    public Calcul() {
        if (Main.instance == null) {
            System.err.println("WARNING, Main null!");
            punts = new ArrayList<>();
            return;
        }
        this.dades = Main.instance.getDades();
        this.punts = new ArrayList<>(dades.getPunts());
        this.tp = dades.getTp();

    }
}
