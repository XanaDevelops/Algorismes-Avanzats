package vista;

import principal.Comunicar;

import javax.swing.*;
import java.awt.*;

public class Dibuix extends JPanel {

    private Comunicar principal;
    private int N;
    private int maxY;

    /**
     * Inicialitza els límits del panell del, i la instància de classe.
     *
     * @param w amplada del panell del Dibuix
     * @param h altura del panell del Dibuix
     * @param p instància del programa principal
     */
    public Dibuix(int w, int h, Comunicar p) {
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("paintComponent s'està executant");


        int w = this.getWidth() - 1;
        int h = this.getHeight() - 1;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.drawLine(50, 10, 50, h - 10);
        g.drawLine(50, h - 10, w - 10, h - 10);

            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= 5; i++) {
                int x = 50 + i * (w - 60) / 5;
                int y = h - 10 - i * (h - 40) / 5;
                g.drawLine(x, h - 10, x, 10);
                g.drawLine(50, y, w - 10, y);
                g.setColor(Color.BLACK);
                g.setColor(Color.LIGHT_GRAY);
            }



        }
    }


