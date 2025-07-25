package vista;

import controlador.Main;
import model.Dades;
import model.Resultat;
import model.TipusCalcul;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static vista.Eixos2D.MARGIN;

public class EixosTempsExec extends JPanel {


    /**
     * Inicialitza els límits del panell de la gràfica, i la instància de classe.
     *
     * @param w amplada del panell de gràfica
     * @param h altura del panell de gràfica
     */
    public EixosTempsExec(int w, int h) {

        this.setPreferredSize(new Dimension(w, h));
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
            ArrayList<Integer> nPunts = new ArrayList<>();

            NavigableMap<TipusCalcul, List<Resultat>> resultats = dades.getResultats();
            if(resultats!=null) {
                for(Map.Entry<TipusCalcul, List<Resultat>> entry : resultats.entrySet()) {
                    nPunts.addAll(entry.getValue().stream().map(Resultat::getN).toList());
                }

            }
            if (nPunts.isEmpty()) {
                return;
            }

            Collections.sort(nPunts);

            List<Resultat> dv = resultats.get(TipusCalcul.DV_MIN);
            List<Resultat> fb = resultats.get(TipusCalcul.FB_MIN);
            List<Resultat> kd = resultats.get(TipusCalcul.KD_MIN);

            if (dv != null) dv.sort(Comparator.comparingInt(Resultat::getN));
            if (fb != null) fb.sort(Comparator.comparingInt(Resultat::getN));
            if (kd != null) kd.sort(Comparator.comparingInt(Resultat::getN));

            List<Resultat> ch = resultats.get(TipusCalcul.CH_MAX);
            List<Resultat> fbm = resultats.get(TipusCalcul.FB_MAX);
            List<Resultat> uni = resultats.get(TipusCalcul.UNI_MAX);

            if (ch != null) ch.sort(Comparator.comparingInt(Resultat::getN));
            if (fbm != null) fbm.sort(Comparator.comparingInt(Resultat::getN));
            if (uni != null) uni.sort(Comparator.comparingInt(Resultat::getN));

            int maxelement = nPunts.getLast();

            //cerca el major temps registrat dins les operacions
            long maxTemps = getMaxTemps(dv, fb, kd);


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
            pintarCalcul(g, h, w, dv, maxelement, maxTemps, FinestraTempsExec.VERD);
            // llista de resultats de l'algorisme de Força Bruta
            pintarCalcul(g, h, w, fb, maxelement, maxTemps, FinestraTempsExec.VERMELL);
            // llista resultats kd
            pintarCalcul(g, h, w, kd, maxelement, maxTemps, FinestraTempsExec.BLUE);

            // llista de resultats de l'algorisme de convex hull
            pintarCalcul(g, h, w, ch, maxelement, maxTemps, FinestraTempsExec.MVERD);
            // llista de resultats de l'algorisme de Força Bruta
            pintarCalcul(g, h, w, fbm, maxelement, maxTemps, FinestraTempsExec.ORANGE);
            // llista resultats uniforme
            pintarCalcul(g, h, w, uni, maxelement, maxTemps, FinestraTempsExec.BLACK);

        }
    }

    private void pintarCalcul(Graphics g, int h, int w, List<Resultat> res, int maxelement,  long maxTemps, Color color) {
        int pax = 50;;
        int pay = h - 10;;
        int px, py;
        if (res != null) {
            for (Resultat r : res) {
                if (maxelement == 0) break;
                g.setColor(color);
                px = 50 + r.getN() * (w - 60) / maxelement;
                py = (h - 20) - (int) ((Math.log10(r.getTempsNano()) * (h - 40)) / Math.log10(maxTemps));

                g.fillOval(px - 3, py - 3, 7, 7);
                g.drawLine(pax, pay, px, py);
                g.drawString("(" + r.getN() + ", " + r.getTempsNano() + ")", px - 20, py - 20);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 7, 7);
                pax = px;
                pay = py;
            }
        }
    }

    private static long getMaxTemps(List<Resultat> dv, List<Resultat> fb, List<Resultat> kd) {
        long maxTemps = Long.MIN_VALUE;

        maxTemps = getMaxTemps(dv, maxTemps);

        maxTemps = getMaxTemps(fb, maxTemps);
        maxTemps = getMaxTemps(kd, maxTemps);
        return maxTemps;
    }

    private static long getMaxTemps(List<Resultat> fb, long maxTemps) {
        if (fb != null) {
            for (Resultat resultat : fb) {
                if (resultat != null && resultat.getTempsNano() > maxTemps) {
                    maxTemps = resultat.getTempsNano();
                }
            }
        }
        return maxTemps;
    }


}

