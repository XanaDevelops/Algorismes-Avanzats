package vista;

import model.Dades;

import javax.swing.*;
import java.awt.*;

public class PanellGraf extends JPanel {
    private static final int AMPLADA_PANELL = 600;
    private static final int ALTURA_PANELL = 600;
    private static final int RADI = 200;
    private static final int RADI_NODES = 6;
    private static final int DIST_TEXT = 12;
    private static final int DIST_LABEL_NODE = 10;
    private static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 12);

    private Dades dades;
    private int[][] graf;

    public PanellGraf(Dades dades) {
        this.dades = dades;
        this.setPreferredSize(new Dimension(AMPLADA_PANELL, ALTURA_PANELL));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(dades != null) {
            graf = dades.getGraf();
        }

        if (graf == null) return;
        int n = graf.length;
        if (n == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setFont(FONT_LABEL);
        Point[] coords = new Point[n];

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            coords[i] = new Point(
                    cx + (int)(RADI * Math.cos(angle)),
                    cy + (int)(RADI * Math.sin(angle))
            );
        }

        g2.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = graf[i][j];
                if (dist != Integer.MAX_VALUE) {
                    Point pi = coords[i];
                    Point pj = coords[j];
                    g2.drawLine(pi.x, pi.y, pj.x, pj.y);

                    int mx = (pi.x + pj.x) / 2;
                    int my = (pi.y + pj.y) / 2;

                    int dx = pj.y - pi.y;
                    int dy = pi.x - pj.x;
                    double length = Math.sqrt(dx * dx + dy * dy);
                    if (length != 0) {
                        mx += (int)(DIST_TEXT * dx / length);
                        my += (int)(DIST_TEXT * dy / length);
                    }

                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(dist), mx, my);
                    g2.setColor(Color.LIGHT_GRAY);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            Point p = coords[i];
            g2.setColor(Color.BLUE);
            g2.fillOval(p.x - RADI_NODES, p.y - RADI_NODES, RADI_NODES * 2, RADI_NODES * 2);
            g2.setColor(Color.BLACK);
            g2.drawString("C" + i, p.x + DIST_LABEL_NODE, p.y);
        }
    }

    // prova temporal
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prova PanellGraf");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Dades dades = new DadesFake();
            PanellGraf panell = new PanellGraf(dades);
            frame.add(panell);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

// temporal per fer proves
class DadesFake extends Dades {
    @Override
    public int[][] getGraf() {
        final int INF = Integer.MAX_VALUE;
        return new int[][]{
                {0,   2,   4, INF, INF},
                {2,   0,   1,   7, INF},
                {4,   1,   0,   3,   6},
                {INF, 7,   3,   0,   2},
                {INF, INF, 6,   2,   0}
        };
    }
}
