package model;

import java.util.LinkedList;

public class Dades {
    public int getIdCount() {
        return 0;
    }
    private int [][] graf;
    private LinkedList<Integer> solucio; //conté les ciutats representades pel seu índex en la matriu

    public Dades() {
        solucio = new LinkedList<>();

    }
    public Dades(int [][] graf) {
        this.graf = graf;
    }

    public int[][] getGraf() {
        return graf;
    }
    public void setGraf(int[][] graf) {
        this.graf = graf;
    }
    public LinkedList<Integer> getSolucio() {
        return solucio;
    }

    public int guardarSolucio(LinkedList<Integer> solucio) {
        this.solucio = solucio;
        return 0;
    }
}
