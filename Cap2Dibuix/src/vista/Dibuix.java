package vista;

import model.Model;
import principal.Main;

import javax.swing.*;
import java.awt.*;

public class Dibuix extends JPanel {

    private final Main principal;
    private Model solver;

    /**
     * Inicialitza els límits del panell del, i la instància de classe.
     *
     * @param w amplada del panell del Dibuix
     * @param h altura del panell del Dibuix
     * @param p instància del programa principal
     */
    public Dibuix(int w, int h, Main p) {
        this.principal = p;
        this.setBounds(0, 0, w, h);
    }

    /**
     * Crida a paintComponent() per actualitzar el panell.
     */
    public synchronized void pintar() {
        this.repaint();
    }

    /**
     * Pinta el panell de la gràfica
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Model dades = principal.getDades();
        if (dades == null) {
            System.out.println("Dades és null");
            return;
        }

        int[][] matriu = dades.getMatriu();
        if (matriu == null) {
            System.out.println("Matriu és null");
            return;
        }

        int files = matriu.length;
        int columnes = matriu[0].length;

        int midaCella = this.getWidth() / columnes;
        int midaCellb = this.getHeight() / files;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (matriu[i][j] != -1) { // Suposant que -1 significa buit
                    g.setColor(getColorForTromino(matriu[i][j]));
                    g.fillRect(j * midaCella, i * midaCellb, midaCella, midaCellb);

                    // Dibuixar només les vores exteriors
                    g.setColor(Color.BLACK);
                    esVoraExterior(matriu, i, j, midaCella, midaCellb, g);

                }
            }
        }
    }

    private void esVoraExterior(int[][] matriu, int i, int j, int midaCella, int midaCellb, Graphics g) {

        int id = matriu[i][j];
        int x = j * midaCella;
        int y = i * midaCellb;

        // Vora superior
        if (i == 0 || matriu[i - 1][j] != id) {
            g.drawLine(x, y, x + midaCella, y);
        }
        // Vora inferior
        if (i == matriu.length - 1 || matriu[i + 1][j] != id) {
            g.drawLine(x, y + midaCellb, x + midaCella, y + midaCellb);
        }
        // Vora esquerra
        if (j == 0 || matriu[i][j - 1] != id) {
            g.drawLine(x, y, x, y + midaCellb);
        }
        // Vora dreta
        if (j == matriu[0].length - 1 || matriu[i][j + 1] != id) {
            g.drawLine(x + midaCella, y, x + midaCella, y + midaCellb);
        }
    }

    private Color getColorForTromino(int id) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
        return colors[Math.abs(id) % colors.length];
    }

}


