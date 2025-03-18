package principal;

import model.Model;
import model.TrominoSolver2;

import java.awt.image.BufferedImage;

import vista.Finestra;

public class Main implements Comunicar {
    Finestra finestra;
    Model dades;

    public Main(){
        finestra = new Finestra(this);
        dades = new TrominoSolver2(8, 0,0);
        dades.resol();
        ((TrominoSolver2) (dades)).imprimir();
        System.out.println("---------------------------------------------------------");
        finestra.comunicar("pintar");
    }

    public static void main(String[] args) {
        new Main();
    }

    public Model getDades() {
        return dades;
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}