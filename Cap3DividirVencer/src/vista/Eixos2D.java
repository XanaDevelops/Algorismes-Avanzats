package vista;

import controlador.Comunicar;
import controlador.Main;
import model.Dades;
import model.Resultat;
import model.TipoPunt;
import model.punts.Punt;


import javax.swing.*;
import java.awt.*;
import java.util.List;


public class Eixos2D extends JPanel implements Comunicar {
    private Dades dades;

    private int width;
    private int height;
    protected final static int MARGIN = 15;
    private Resultat resultatADibuixar = null;

    public Eixos2D() {
        this.dades = Main.instance.getDades();

    }

    public synchronized void pintar() {
        if (this.getGraphics() != null) {
            paint(this.getGraphics());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.width = this.getWidth();
        this.height = this.getHeight();

        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(64, 181, 217));

        //dibuixar eixos
        g.drawLine(MARGIN, height - MARGIN, width - MARGIN, height - MARGIN);
        g.drawLine(MARGIN, height - MARGIN, MARGIN, MARGIN);

        g.setColor(new Color(64, 181, 217));
        pintarPunts(g);
        if (resultatADibuixar != null) {

            pintarDistancia(g, resultatADibuixar);
            resultatADibuixar = null;
        }
    }

    private void pintarPunts(Graphics g) {
        if (dades != null && dades.getPunts() != null && !dades.getPunts().isEmpty() && dades.getTp() == TipoPunt.p2D) {
            List<Punt> punts = dades.getPunts();
            int[] maxims = getMaxMin();
            g.setColor(Color.LIGHT_GRAY);

            for (Punt punt2D : punts) {
                dibuixaPunt(g, punt2D, getPointSize(punts), maxims[0], maxims[1], new Color(102, 178, 255));
            }
        }
    }

    private int[] getMaxMin() {
        List<Punt> punts = dades.getPunts();
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Punt punt2D : punts) {
            if (punt2D != null) {
                if (punt2D.getX() > maxX) {
                    maxX = punt2D.getX();
                }
                if (punt2D.getY() > maxY) {
                    maxY = punt2D.getY();
                }
            }
        }
        return new int[]{maxX, maxY};
    }

    public void dibuixaPunt(Graphics g, Punt punt2D, int pointSize, int maxX, int maxY, Color color) {
        int px, py;
        g.setColor(color);
        px = 50 + punt2D.getX() * (width - 80) / maxX;
        py = (height - 20) - (punt2D.getY() * (height - 40)) / maxY;
        g.fillOval(px - pointSize / 2, py - pointSize / 2, pointSize, pointSize);
        g.setColor(Color.black);
        g.drawOval(px - pointSize / 2, py - pointSize / 2, pointSize, pointSize);
    }

    public void pintarDistancies(String algorisme) {
        if (dades.getPunts() != null && !dades.getPunts().isEmpty()) {
            resultatADibuixar = dades.getLastResultat();
            repaint();
        }
    }

    public int getPointSize(List<Punt> punts) {
        int maxSizePoint = 10;
        int minSizePoint = 4;

        double ratio = Math.min(1.0, (double) punts.size() / 10000);
        return (int) (maxSizePoint - ratio * (maxSizePoint - minSizePoint));
    }

    private void pintarDistancia(Graphics g, Resultat res) {
        Graphics2D g2d = (Graphics2D) g;
        List<Punt> punts = dades.getPunts();
        int[] maxs = getMaxMin();
        int maxX = maxs[0], maxY = maxs[1];
        int p1x = 50 + res.getP1().getX() * (width - 80) / maxX;
        int p1y = (height - 20) - (res.getP1().getY() * (height - 40)) / maxY;
        int p2x = 50 + res.getP2().getX() * (width - 80) / maxX;
        int p2y = (height - 20) - (res.getP2().getY() * (height - 40)) / maxY;


        dibuixaPunt(g, res.getP1(), getPointSize(punts), maxX, maxY, new Color(255, 153, 51));
        dibuixaPunt(g, res.getP2(), getPointSize(punts), maxX, maxY, new Color(255, 153, 51));

        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(Color.RED);
        g2d.drawLine(p1x - 1, p1y - 1, p2x - 1, p2y - 1);

        int midX = (p1x + p2x) / 2;
        int midY = (p1y + p2y) / 2;
        Font font = new Font("Verdana", Font.BOLD, 10);
        g2d.setFont(font);
        g2d.drawString(String.format("%.2f", res.getDistancia()), midX, midY - 10);
    }

    @Override
    public void comunicar(String s) {
        String[] args = s.split(":");
        switch(args[0]){
            case "dibuixPunts":
            case "pintar":
                pintar();
                break;
            case "dibuixDistancia":
                pintarDistancies(args[1]);
            case "aturar":
                break;
        }
    }
}
