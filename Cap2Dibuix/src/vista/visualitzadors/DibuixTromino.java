package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DibuixTromino extends JPanel implements Comunicar {

    private final Comunicar principal;
    private boolean colorON = false;

    private double midaCellx, midaCelly;
    private int iniciX = 0, iniciY = 0;

    /**
     * Inicialitza els límits del panell del, i la instància de classe.
     *
     * @param p instància del programa principal
     */
    public DibuixTromino(Comunicar p) {
        this.principal = p;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                detectarCasella(e.getX(), e.getY(), true);
            }

        });
        addMouseMotionListener(new MouseAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                detectarCasella(e.getX(), e.getY(), false);
                repaint();
            }
        });
    }

    public void colorON() {
        this.colorON = !colorON;
        repaint();
    }

    private void dibuixarVoraExterior(int[][] matriu, int i, int j, Graphics g) {

        int id = matriu[i][j];

        int x = (int)(j * midaCellx);
        int y = (int)(i * midaCelly);

        // Vora superior
        if (i == 0 || matriu[i - 1][j] != id) {
            g.drawLine(x, y, (int)(x + midaCellx), y);
        }
        // Vora inferior
        if (i == matriu.length - 1 || matriu[i + 1][j] != id) {
            g.drawLine(x, (int) (y + midaCelly), (int) (x + midaCellx), (int) (y + midaCelly));
        }
        // Vora esquerra
        if (j == 0 || matriu[i][j - 1] != id) {
            g.drawLine(x, y, x, (int) (y + midaCelly));
        }
        // Vora dreta
        if (j == matriu[0].length - 1 || matriu[i][j + 1] != id) {
            g.drawLine((int) (x + midaCellx), y, (int) (x + midaCellx), (int) (y + midaCelly));
        }
    }

    private Color getColorForTromino(int id) {
        Color[] colors = ((Main)principal).getDades().getColors();
        return colors[Math.abs(id) % colors.length];
    }

    private void detectarCasella(int x, int y, boolean doSet) {
        int[][] matriu = ((Main) (principal)).getMatriu();
        if (matriu == null) return;

        int files = matriu.length;
        int columnes = (files > 0) ? matriu[0].length : 1;


        int fila = (int) (y / midaCelly);
        int columna = (int) (x / midaCellx);

        if (doSet) {
            if (fila >= 0 && fila < files && columna >= 0 && columna < columnes) {
                principal.comunicar("inici:" + columna + ":" + fila);
            }
        }
        iniciX = columna;
        iniciY = fila;
    }

    /**
     * Pinta el panell de la gràfica
     *
     * @param g
     */

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());


        int[][] matriu = ((Main) (principal)).getMatriu();
        if (matriu == null) {
            return;
        }

        int files = matriu.length;
        int columnes = (files > 0) ? matriu[0].length : 1;

        midaCellx = this.getWidth() / (double) columnes;
        midaCelly = this.getHeight() / (double) files;


        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                // Dibuixar línies guia de color gris
                g.setColor(new Color(0x7FEEEEEE, true)); //transparencia
                g.drawRect((int) (j * midaCellx), (int) (i * midaCelly), (int) midaCellx, (int) midaCelly);
                if (matriu[i][j] == -1) {
                    //iniciX = j;
                    //iniciY = i;
                    //pintant de negre
                    g.setColor(Color.BLACK);
                    g.fillRect((int) (j * midaCellx), (int) (i * midaCelly), (int) midaCellx, (int) midaCelly);
                } else if (matriu[i][j] != 0) { // Suposant que -1 significa buit
                    if (colorON) {
                        g.setColor(getColorForTromino(matriu[i][j]));
                        g.fillRect((int) (j * midaCellx), (int) (i * midaCelly), (int) midaCellx, (int) midaCelly);
                    }
                    // Dibuixar només les vores exteriors
                    g.setColor(Color.BLACK);
                    dibuixarVoraExterior(matriu, i, j, g);
                }
            }
        }
        g.setColor(Color.RED);
        g.drawRect((int) (iniciX * midaCellx), (int) (iniciY * midaCelly), (int) midaCellx, (int) midaCelly);
    }

    /**
     * Crida a paintComponent() per actualitzar el panell.
     */
    @Override
    public void comunicar(String s) {
        switch (s) {
            case "pintar":
                repaint();
                break;
            case "color":
                colorON();
                break;
        }

    }
}
