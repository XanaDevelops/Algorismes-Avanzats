package principal;

import model.TrominoSolver;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Main implements Comunicar {
    static BufferedImage image;

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        TrominoSolver solver = new TrominoSolver(2);
        frame.add(solver);
        frame.setVisible(true);
    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {

    }
}