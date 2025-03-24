package model;


import java.awt.*;
import java.awt.image.BufferedImage;

public class Dades {
    private Tipus tipus;

    private int[][] tauler;
    private double constantMultiplicativa;
    private int profunditat;

    private BufferedImage image;

    //array que emmagatzema els colors per a processos que ho necessitin
    private Color[] colors = {Color.RED, Color.yellow, Color.green, Color.CYAN};

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

    public synchronized void setValor(int fila, int columna, int valor) {
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

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public BufferedImage getImage() {
        return image;
    }

    public void clean(){
        this.profunditat = 0;
        this.tauler = null;

        this.image = null;

        iniciTromino[0] = 0;
        iniciTromino[1] = 0;
    }

    public Color getColor(int i){
        return colors[i];
    }
    public void setColor(int i, Color color){
        colors[i] = color;
    }
    public Color[] getColors() {return colors;}

}
