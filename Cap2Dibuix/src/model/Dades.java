package model;

import java.util.Arrays;

public class Dades {
    private Tipus tipus;

    private int[][] tauler;
    private double constantMultiplicativa;
    private int profunditat;
    //
    //dades tromino
    private int [] foratTromino;

    //dades triangles


    //dades loquesigui

    //dades programa
    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;

    public Dades() {
    }

    public Tipus getTipus() {
        return tipus;
    }

    public void setValor(int fila, int columna, int valor) {
        this.tauler[fila][columna] = valor;
    }

    public int getValor(int fila, int columna) {
        return tauler[fila][columna];
    }

    public void setTipus(Tipus tipus) {
        this.tipus = tipus;
    }

    public int[][] getTauler() {
        return tauler;
    }

    public void setTauler(int[][] tauler) {
        this.tauler = tauler;
    }

    public double getConstantMultiplicativa() {
        return constantMultiplicativa;
    }

    public void setConstantMultiplicativa(double constantMultiplicativa) {
        this.constantMultiplicativa = constantMultiplicativa;
    }

    public int getProfunditat() {
        return profunditat;
    }

    public void setProfunditat(int profunditat) {
        this.profunditat = profunditat;
    }

    public void setForatTromino(int[] forat) {
        this.foratTromino = forat;
    }

    public int [] getForatTromino() {
        return foratTromino;
    }
}
