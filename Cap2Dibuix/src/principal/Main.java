package principal;

import model.TrominoSolver;
import model.TrominoSolver2;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Main implements Comunicar {
    static BufferedImage image;

    public static void main(String[] args) {
//        System.out.println("Hello, World!");
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
//        TrominoSolver solver = new TrominoSolver(4, 3, 3);
//        frame.add(solver);
//        frame.setVisible(true);

        // Definim la mida de la graella (potència de 2) i la posició del forat
        int size = 64; // Per exemple, una graella de 8x8
        int holeX = 3; // Posició X del forat (per exemple, en (3, 3))
        int holeY = 3; // Posició Y del forat (per exemple, en (3, 3))

//        // Creem l'objecte TrominoSolver
        TrominoSolver2 solver = new TrominoSolver2(size, holeX, holeY);

        frame.add(solver);
        // Resolem el problema de posar trominos
        solver.resol();

        // Imprimim la graella per veure el resultat
        solver.imprimir();
////
////
        frame.setVisible(true);
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}