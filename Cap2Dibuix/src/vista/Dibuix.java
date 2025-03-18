package vista;

import model.Model;
import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;

public class Dibuix extends JPanel {

    private Main principal;
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
        if (this.getGraphics() != null) {
            paintComponent(this.getGraphics());
        }
    }

    /**
     * Pinta el panell de la gràfica
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Model  dades = principal.getDades();
        if (dades == null) return;

        int[][] matriu = dades.getMatriu();
        if (matriu == null) return;

        int files = matriu.length;
        int columnes = matriu[0].length;

        int midaCella = this.getWidth() / columnes;
        int midaCellb = this.getHeight() / files;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (matriu[i][j] != -1) { // Suposant que -1 significa buit
                    g.setColor(getColorForTromino(matriu[i][j]));
                    g.fillRect(j * midaCella, i * midaCella, midaCella, midaCella);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * midaCella, i * midaCella, midaCella, midaCella);
                }
            }
        }
    }

    private Color getColorForTromino(int id) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
        return colors[Math.abs(id) % colors.length];
    }

}


