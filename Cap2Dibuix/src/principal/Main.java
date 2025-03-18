package principal;

import model.TrominoSolver2;

import java.awt.image.BufferedImage;

public class Main implements Comunicar {
    static BufferedImage image;

    public static void main(String[] args) {
    TrominoSolver2 model = new TrominoSolver2(8, 0,0);
    model.resol();
    model.imprimir();
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}