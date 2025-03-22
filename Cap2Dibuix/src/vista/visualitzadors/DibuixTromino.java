package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;

public class DibuixTromino extends JPanel implements Comunicar {

    private final Comunicar principal;
    private boolean colorON = false;


    /**
     * Inicialitza els límits del panell del, i la instància de classe.
     *
     * @param w amplada del panell del Dibuix
     * @param h altura del panell del Dibuix
     * @param p instància del programa principal
     */
    public DibuixTromino(int w, int h, Comunicar p) {
        this.principal = p;
        this.setBounds(0, 0, w, h);
    }

    public void colorON() {
        this.colorON = !colorON;
    }

    private void dibuixarVoraExterior(int[][] matriu, int i, int j, Graphics g) {

        int id = matriu[i][j];
        int files = matriu.length;
        int columnes = matriu[0].length;
        int midaCellx = this.getWidth() / columnes;
        int midaCelly = this.getHeight() / files;
        int x = j * midaCellx;
        int y = i * midaCelly;

        // Vora superior
        if (i == 0 || matriu[i - 1][j] != id) {
            g.drawLine(x, y, x + midaCellx, y);
        }
        // Vora inferior
        if (i == matriu.length - 1 || matriu[i + 1][j] != id) {
            g.drawLine(x, y + midaCelly, x + midaCellx, y + midaCelly);
        }
        // Vora esquerra
        if (j == 0 || matriu[i][j - 1] != id) {
            g.drawLine(x, y, x, y + midaCelly);
        }
        // Vora dreta
        if (j == matriu[0].length - 1 || matriu[i][j + 1] != id) {
            g.drawLine(x + midaCellx, y, x + midaCellx, y + midaCelly);
        }
    }

    private Color getColorForTromino(int id) {
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
        return colors[Math.abs(id) % colors.length];
    }

    /**
     * Pinta el panell de la gràfica
     *
     * @param g
     */

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);


        int[][] matriu = ((Main) (principal)).getMatriu();
        if (matriu == null) {
            System.err.println("No se puede pintar la matriu");
            return;
        }

        int files = matriu.length;
        int columnes = (files > 0) ? matriu[0].length : 1;

        int midaCellx = this.getWidth() / columnes;
        int midaCelly = this.getHeight() / files;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (matriu[i][j] != -1) { // Suposant que -1 significa buit
                    if (colorON) {
                        g.setColor(getColorForTromino(matriu[i][j]));
                        g.fillRect(j * midaCellx, i * midaCelly, midaCellx, midaCelly);
                    }
                    // Dibuixar només les vores exteriors
                    g.setColor(Color.BLACK);
                    dibuixarVoraExterior(matriu, i, j, g);

                }
            }
        }
    }

    /**
     * Crida a paintComponent() per actualitzar el panell.
     */
    @Override
    public void comunicar(String s) {
        switch (s){
            case "pintar":
                repaint();
                break;
            case "color":
                colorON();
                break;
            case "borrar":
                System.err.println("borrar no implementat");
                break;
        }

    }
}
