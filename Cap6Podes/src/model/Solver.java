package model;

import controlador.Comunicar;

import java.util.concurrent.Callable;

public class Solver implements Callable<Dades.Solucio>, Comunicar {
    public Solver(int id, Dades dades) {
    }

    @Override
    public Dades.Solucio call() throws Exception {
        return null;
    }

    public void interrompre() {
    }

    public void resume() {
    }

    public void pause() {
    }

    @Override
    public void comunicar(String msg) {

    }
}
