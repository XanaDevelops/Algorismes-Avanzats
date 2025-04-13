package vista;

import controlador.Main;
import model.Dades;
import  model.Dades.Resultat;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static vista.Eixos.MARGIN;

public class EixosTempsExec extends JPanel {


    /**
     * Inicialitza els límits del panell de la gràfica, i la instància de classe.
     *
     * @param w amplada del panell de gràfica
     * @param h altura del panell de gràfica
     */
    public EixosTempsExec(int w, int h) {

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
        Dades dades = Main.instance.getDades();
        int w = this.getWidth() - 1;
        int h = this.getHeight() - 24;
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.black);
        g.drawLine(MARGIN, h - MARGIN, w - MARGIN, h - MARGIN);
        g.drawLine(MARGIN, h - MARGIN, MARGIN, MARGIN);


        if (dades != null && dades.getPunts() != null) {

            //calcula el maxim valor de N (mida de la matriu)
            NavigableMap<Integer, Resultat> dv = dades.getDividirVencer();
            NavigableMap<Integer, Resultat> fb = dades.getForcaBruta();
            NavigableMap<Integer, Resultat> kd = dades.getKd();
            ArrayList<Integer> nPunts = new ArrayList<>();
            if (dv != null && !dv.isEmpty()) {
                nPunts.addAll(dv.keySet());
            }
            if (fb != null && !fb.isEmpty()) {
                nPunts.addAll(fb.keySet());
            }
            if (kd != null && !kd.isEmpty()) {
                nPunts.addAll(kd.keySet());
            }

            if (nPunts.isEmpty()){
                return;
            }


            Collections.sort(nPunts);

            int maxelement= nPunts.getLast();

            //cerca el major temps registrat dins les operacions
            long maxTemps = getMaxTemps(dv, fb, kd);


            System.out.println("maxTemps: " + maxTemps);
            int px, py, pax, pay;


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
                g.drawString(String.valueOf((long) Math.exp(i * Math.log(maxTemps) / 5)), 10, y + 5);
                g.setColor(Color.LIGHT_GRAY);
            }

            // llista de resultats de l'algorisme de Dividir i Vèncer
            pax = 50;
            pay = h - 10;
            if (dv != null) {
                for (Map.Entry<Integer, Resultat> r : dv.entrySet()) {
                    if (maxelement == 0 || r.getValue() == null) break;
                    g.setColor(FinestraTempsExec.VERD);
                    px = 50 + r.getKey() * (w - 60) / maxelement;
                    py = (h - 20) - (int)((Math.log10(r.getValue().getTempsNano()) * (h - 40)) / Math.log10(maxTemps));

                    g.fillOval(px - 3, py - 3, 7, 7);
                    g.drawLine(pax, pay, px, py);
                    g.drawString("(" + r.getKey() + ", " + r.getValue().getTempsNano() + ")", px - 20, py - 20);
                    g.setColor(Color.black);
                    g.drawOval(px - 3, py - 3, 7, 7);
                    pax = px;
                    pay = py;
                }
            }
            // llista de resultats de l'algorisme de Força Bruta
            pax = 50;
            pay = h - 10;
            if (fb != null) {
                for (Map.Entry<Integer, Resultat> r : fb.entrySet()) {
                    if (maxelement == 0 || r.getValue() == null) break;
                    g.setColor(FinestraTempsExec.VERMELL);
                    px = 50 + r.getKey() * (w - 60) / maxelement;
                    py = (h - 20) - (int)((Math.log10(r.getValue().getTempsNano()) * (h - 40)) / Math.log10(maxTemps));

                    g.fillOval(px - 3, py - 3, 7, 7);
                    g.drawLine(pax, pay, px, py);
                    g.drawString("(" + r.getKey() + ", " + r.getValue().getTempsNano() + ")", px - 20, py - 20);
                    g.setColor(Color.black);
                    g.drawOval(px - 3, py - 3, 7, 7);
                    pax = px;
                    pay = py;
                }
            }

            pax = 50;
            pay = h - 10;
            if (kd != null) {
                for (Map.Entry<Integer, Resultat> r : kd.entrySet()) {
                    if (maxelement == 0 || r.getValue() == null) break;
                    g.setColor(FinestraTempsExec.BLUE);
                    px = 50 + r.getKey() * (w - 60) / maxelement;
                    py = (h - 20) - (int)((Math.log10(r.getValue().getTempsNano()) * (h - 40)) / Math.log10(maxTemps));

                    g.fillOval(px - 3, py - 3, 7, 7);
                    g.drawLine(pax, pay, px, py);
                    g.drawString("(" + r.getKey() + ", " + r.getValue().getTempsNano() + ")", px - 20, py - 20);
                    g.setColor(Color.black);
                    g.drawOval(px - 3, py - 3, 7, 7);
                    pax = px;
                    pay = py;
                }
            }
        }
    }

    private static long getMaxTemps(NavigableMap<Integer, Resultat> dv, NavigableMap<Integer, Resultat> fb, NavigableMap<Integer, Resultat> kd) {
        long maxTemps = Long.MIN_VALUE;

        maxTemps = getMaxTemps(dv, maxTemps);

        maxTemps = getMaxTemps(fb, maxTemps);
        maxTemps = getMaxTemps(kd, maxTemps);
        return maxTemps;
    }

    private static long getMaxTemps(NavigableMap<Integer, Resultat> fb, long maxTemps) {
        if (fb != null) {
            for (Map.Entry<Integer, Resultat> entry : fb.entrySet()) {
                Resultat resultat = entry.getValue();
                if (resultat != null && resultat.getTempsNano() > maxTemps) {
                    maxTemps = resultat.getTempsNano();
                }
            }
        }
        return maxTemps;
    }


}

