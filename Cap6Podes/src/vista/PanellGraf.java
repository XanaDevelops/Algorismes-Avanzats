package vista;

import model.Dades;
import model.Solver;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class PanellGraf extends JPanel {
    private static final int AMPLADA_PANELL = 600;
    private static final int ALTURA_PANELL = 600;
    private int RADI = 200;
    private int RADI_NODES = 6;
    private int MIDA_PUNTA_FLETXA = 10;
    private int DIST_TEXT = 12;
    private int DIST_LABEL_NODE = 10;
    private Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 12);

    private Dades dades;
    private int[][] graf;
    private Solver.Node solucio;
    private boolean mostrarSolucio = false;

    public PanellGraf(Dades dades) {
        this.dades = dades;
        this.setPreferredSize(new Dimension(AMPLADA_PANELL, ALTURA_PANELL));
    }

    private void ajustarParametresVisuals(int n) {
        int radiMax = Math.min(AMPLADA_PANELL, ALTURA_PANELL) / 2 - 40;

        double factor = Math.min(1.0, (radiMax * 2.0) / (n * 25.0));

        this.RADI = (int) (radiMax * factor);

        this.RADI_NODES = Math.max(3, (int) (8 * factor));
        this.MIDA_PUNTA_FLETXA = Math.max(5, (int) (12 * factor));
        this.DIST_TEXT = Math.max(6, (int) (15 * factor));
        this.DIST_LABEL_NODE = Math.max(5, (int) (12 * factor));
        this.FONT_LABEL = new Font("SansSerif", Font.PLAIN, Math.max(8, (int) (14 * factor)));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (dades != null) {
            graf = dades.getGraf();
        }
        if (graf == null || graf.length == 0) return;

        ajustarParametresVisuals(graf.length);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setFont(FONT_LABEL);

        if (mostrarSolucio && solucio != null) {
            dibuixarSolucio(g2);
        } else {
            dibuixarGrafComplet(g2);
        }
    }

    private void dibuixarSolucio(Graphics2D g2) {
        int n = graf.length;
        Point[] coords = calcularCoordenadesNode(n);

        // extreure el camí (del final a l'inici)
        LinkedList<Integer> path = new LinkedList<>();
        Solver.Node actual = solucio;
        while (actual != null) {
            path.addFirst(actual.getActual());
            actual = actual.getPare();
        }

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.RED);

        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            Point p1 = coords[from];
            Point p2 = coords[to];

            dibuixFletxa(g2, p1.x, p1.y, p2.x, p2.y);

            int pes = graf[from][to];
            int mx = (p1.x + p2.x) / 2;
            int my = (p1.y + p2.y) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(pes), mx, my);
            g2.setColor(Color.RED);
        }

        for (int i = 0; i < n; i++) {
            Point p = coords[i];
            if (i == path.getFirst()) {
                g2.setColor(Color.GREEN); // inici
            } else if (i == path.getLast()) {
                g2.setColor(Color.RED);   // final
            } else if (path.contains(i)) {
                g2.setColor(Color.ORANGE); // nodes del camí però no extrem
            } else {
                g2.setColor(Color.LIGHT_GRAY); // nodes fora del camí
            }
            g2.fillOval(p.x - RADI_NODES, p.y - RADI_NODES, RADI_NODES * 2, RADI_NODES * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - RADI_NODES, p.y - RADI_NODES, RADI_NODES * 2, RADI_NODES * 2);
            g2.drawString(convertirIndexALletres(i), p.x + DIST_LABEL_NODE, p.y);
        }
    }

    private Point[] calcularCoordenadesNode(int n) {
        Point[] coords = new Point[n];
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            coords[i] = new Point(
                    cx + (int) (RADI * Math.cos(angle)),
                    cy + (int) (RADI * Math.sin(angle))
            );
        }
        return coords;
    }

    private void dibuixarGrafComplet(Graphics2D g2) {
        int n = graf.length;
        Point[] coords = calcularCoordenadesNode(n);

        g2.setColor(Color.LIGHT_GRAY);
        g2.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && graf[i][j] != Integer.MAX_VALUE) {
                    Point pi = coords[i];
                    Point pj = coords[j];

                    boolean esBidireccional = (graf[j][i] != Integer.MAX_VALUE);

                    int dx = pj.x - pi.x;
                    int dy = pj.y - pi.y;
                    double dist = Math.hypot(dx, dy);

                    double px = -dy / dist;
                    double py = dx / dist;

                    int offset = esBidireccional ? 10 : 0;

                    int x1 = pi.x + (int) (px * offset);
                    int y1 = pi.y + (int) (py * offset);
                    int x2 = pj.x + (int) (px * offset);
                    int y2 = pj.y + (int) (py * offset);

                    dibuixFletxa(g2, x1, y1, x2, y2);

                    int mx = (x1 + x2) / 2 + (int) (DIST_TEXT * px);
                    int my = (y1 + y2) / 2 + (int) (DIST_TEXT * py);

                    g2.setColor(Color.BLACK);
                    g2.drawString(String.valueOf(graf[i][j]), mx, my);
                    g2.setColor(Color.LIGHT_GRAY);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            Point p = coords[i];
            g2.setColor(Color.BLUE);
            g2.fillOval(p.x - RADI_NODES, p.y - RADI_NODES, RADI_NODES * 2, RADI_NODES * 2);
            g2.setColor(Color.BLACK);
            g2.drawString(convertirIndexALletres(i), p.x + DIST_LABEL_NODE, p.y);
        }
    }

    private String convertirIndexALletres(int index) {
        StringBuilder nom = new StringBuilder();
        index++;
        while (index > 0) {
            index--;
            nom.insert(0, (char) ('A' + (index % 26)));
            index /= 26;
        }
        return nom.toString();
    }

    private void dibuixFletxa(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        double offset = RADI_NODES + 2;
        double sx = x1 + offset * Math.cos(angle);
        double sy = y1 + offset * Math.sin(angle);
        double ex = x2 - offset * Math.cos(angle);
        double ey = y2 - offset * Math.sin(angle);


        g2.drawLine((int) sx, (int) sy, (int) ex, (int) ey);

        double angleFletxa1 = angle - Math.PI / 6;
        double angleFletxa2 = angle + Math.PI / 6;

        int xFletxa1 = (int) (ex - MIDA_PUNTA_FLETXA * Math.cos(angleFletxa1));
        int yFletxa1 = (int) (ey - MIDA_PUNTA_FLETXA * Math.sin(angleFletxa1));

        int xFletxa2 = (int) (ex - MIDA_PUNTA_FLETXA * Math.cos(angleFletxa2));
        int yFletxa2 = (int) (ey - MIDA_PUNTA_FLETXA * Math.sin(angleFletxa2));

        Polygon capFletxa = new Polygon();
        capFletxa.addPoint((int) ex, (int) ey);
        capFletxa.addPoint(xFletxa1, yFletxa1);
        capFletxa.addPoint(xFletxa2, yFletxa2);

        g2.fill(capFletxa);
    }

    public void mostrarSolucio(Solver.Node sol) {
        this.solucio = sol;
        this.mostrarSolucio = true;
        this.repaint();
    }

    public void esborrarSolucio() {
        this.solucio = null;
        this.mostrarSolucio = false;
        this.repaint();
    }

}
