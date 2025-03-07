
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Classe que manipula el dibuix dels eixos de la gràfica.
 */

public class Eixos extends JPanel {

    private Main principal;

    /**
     * Inicialitza els límits del panell de la gràfica, i la instància de classe.
     *
     * @param w amplada del panell de gràfica
     * @param h altura del panell de gràfica
     * @param p instància del programa principal
     */
    public Eixos(int w, int h, Main p) {
        principal = p;
        this.setBounds(0, 0, w, h);
    }

    /**
     * Crida a paintComponent() si hi ha qualque gràfic disponible per
     * actualitzar el panell.
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
        Dades dad = principal.getDades();
        int w = this.getWidth() - 1;
        int h = this.getHeight() - 24;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.drawLine(50, 10, 50, h - 10);
        g.drawLine(50, h - 10, w - 10, h - 10);

        if (dad != null) {
            int maxelement = 0;
            //calcula el maxim valor de N (mida de la matriu)
            for (int i = 0; i < dad.getSumes().size(); i++) {
                Dades.Resultat r = dad.getSumes().get(i);
                if (r.getN() > maxelement) {
                    maxelement = r.getN();
                }
            }
            for (int i = 0; i < dad.getMult().size(); i++) {
                Dades.Resultat r = dad.getMult().get(i);
                if (r.getN() > maxelement) {
                    maxelement = r.getN();
                }
            }

            long maxtemps;
            int px, py, pax, pay;
            maxtemps = 0;
            //cerca el major temps registrat dins les operacions
            //de suma i multiplicació de matrius
            for (int i = 0; i < dad.getSumes().size(); i++) {
                Dades.Resultat r = dad.getSumes().get(i);
                if (r.getTemps() > maxtemps) {
                    maxtemps = r.getTemps();
                }
            }
            for (int i = 0; i < dad.getMult().size(); i++) {
                Dades.Resultat r = dad.getMult().get(i);
                if (r.getTemps() > maxtemps) {
                    maxtemps = r.getTemps();
                }
            }

            // Dibuixar línies guia
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= 5; i++) {
                int x = 50 + i * (w - 60) / 5;
                int y = h - 10 - i * (h - 40) / 5;
                g.drawLine(x, h - 10, x, 10);
                g.drawLine(50, y, w - 10, y);
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(i * maxelement / 5), x - 10, h);
                g.drawString(String.valueOf((long) Math.exp(i * Math.log(maxtemps) / 5)), 10, y + 5);
                g.setColor(Color.LIGHT_GRAY);
            }

            // llistaSuma
            pax = 50;
            pay = h - 10;
            for (int i = 0; i < dad.getSumes().size(); i++) {
                Dades.Resultat r = dad.getSumes().get(i);
                if (maxelement == 0) {
                    break;
                }
                g.setColor(Color.green);
                px = 50 + r.getN() * (w - 60) / maxelement;
                py = (h - 20) - ((int) (r.getLogTemps() * (h - 40) / Math.log10(maxtemps)));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.drawString("(" + r.getN() + ", " + r.getTempsPrint() + ")", px - 20, py - 20);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
            // llistaMult
            pax = 50;
            pay = h - 10;
            for (int i = 0; i < dad.getMult().size(); i++) {
                Dades.Resultat r = dad.getMult().get(i);
                if (maxelement == 0) {
                    break;
                }
                g.setColor(Color.red);
                px = 50 + r.getN() * (w - 60) / maxelement;
                py = (h - 20) - ((int) (r.getLogTemps() * (h - 40) / Math.log10(maxtemps)));
                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.drawString("(" + r.getN() + ", " + r.getTempsPrint() + ")", px - 20, py + 20);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
        }
    }


}