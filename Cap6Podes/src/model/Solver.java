package model;

import controlador.Comunicar;
import controlador.Main;

import java.util.concurrent.Callable;

public class Solver implements Callable<Dades.Solucio>, Comunicar {


    public Solver(int id, int[][] adj) {
    }

    public Solver(int id, int n) {
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
