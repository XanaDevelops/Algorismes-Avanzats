package model;

public class Dades {
    private Tipus tipus;

    private int[][] tauler;
    private double constantMultiplicativa;
    private int profunditat;

    //dades tromino
    private int[]  inici = new int[2];

    public void setInici(int x, int y) {
        inici[0] = x;
        inici[1] = y;
        tauler[x][y] = -1;
    }
    public void createMatrix(int n) {
        int m = (int) Math.pow(2,n);
        tauler = new int[m][m];
    }

    //dades triangles


    //dades loquesigui


    public void setTauler(int[][] tauler) {
        this.tauler = tauler;
    }

    public int[][] getTauler() {
        return tauler;
    }
}
