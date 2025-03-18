package principal;

import model.Dades;
import model.TrominoSolver2;

import java.awt.image.BufferedImage;

public class Main implements Comunicar {
    static BufferedImage image;
    private Dades data;
    private TrominoSolver2 model;


    private int weight, height, profunditat;
    public static void main(String[] args) {
        (new Main()).inici();
    }

    private void inici(){
       weight = height = 800;
        profunditat = 4;
        data = new Dades(weight, height, profunditat, 0, 1);
        model = new TrominoSolver2(data, this);
//        model.resol();
        model.imprimir();
    }


    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}