package model;

import controlador.Comunicar;
import controlador.Main;

public abstract class Solver implements Runnable, Comunicar {

    protected final Dades dades;
    public Solver() {
        this.dades = Main.getInstance().getDades();
    }

    @Override
    public void classificarImatge() {
        run();
    }

    @Override
    public void comunicar(String msg) {

    }

    @Override
    public void run() {

    }
}
