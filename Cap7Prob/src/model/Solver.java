package model;

import controlador.Comunicar;

public abstract class Solver implements Runnable, Comunicar {

    Dades dades;
    public Solver(Dades dades) {
        this.dades = dades;
    }

    @Override
    public void classificarImatge() {
        run();
    }

}
