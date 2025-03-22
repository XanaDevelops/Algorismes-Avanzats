package model;

import java.util.Arrays;

public class Dades {
    private Tipus tipus;

    private int[][] tauler;
    private double constantMultiplicativa;
    private int profunditat;

    //TROMINOS
    private int[] iniciTromino = new int[2];

    public void createMatrix(int n) {
        int m = (int) Math.pow(2,n);
        tauler = new int[m][m];
    }

    public void setForatTromino(int x, int y) {
        tauler[iniciTromino[0]][iniciTromino[1]] = 0;
        iniciTromino[0]=y;
        iniciTromino[1]=x;
        tauler[y][x] = -1;
    }

    public int[] getIniciTromino() {
        return iniciTromino;
    }
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

}
