package principal;

import model.TrominoSolver2;

import java.awt.image.BufferedImage;

import vista.Finestra;

public class Main implements Comunicar {
    Finestra finestra;

    public Main(){
        finestra = new Finestra(this);
        TrominoSolver2 model = new TrominoSolver2(8, 0,0);
        model.resol();
        model.imprimir();
    }

    public static void main(String[] args) {
        new Main();
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}