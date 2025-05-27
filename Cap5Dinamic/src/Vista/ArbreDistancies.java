package Vista;

import Model.Dades;
import Model.Idioma;
import controlador.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static java.util.Collections.shuffle;

public class ArbreDistancies extends JPanel {
    private static final Color[] COLORS = {
            new Color(0x320E3B),
            new Color(0x41521F),
            new Color(0x1C7C54),
            new Color(0x633F33),
            new Color(0xF78764),
            new Color(0xB87152),
            new Color(0x99B2DD),
            new Color(0x79BEEE),
            new Color(0x586BA4),
            new Color(0xC03021)
    };

    private final Dades dades;
    private final int NODES;
    private  int CENTRE_X;
    private int CENTRE_Y;
    private  int tamanyText;
    private  int tamanyDist;
    private int height;
    private int width;
    private int RADI;
    private Point[] posicions;

    public ArbreDistancies() {
        this.dades = Main.getInstance().getDades();

        this.NODES = dades.getIdiomes().size();
        this.width = Finestra.WIDTH_PANELL;
        this.height = Finestra.HEIGHT_PANELL;

        this.setLayout(null);

        this.setPreferredSize(new Dimension(width, height));
    }

    public void getPosicions() {

        posicions = new Point[NODES];
        for (int i = 0; i < NODES; i++) {
            //per millor distribucio
            double angle = 2 * Math.PI * i / NODES;
            posicions[i] = new Point(CENTRE_X + (int) (RADI * Math.cos(angle)), CENTRE_Y + (int) (RADI * Math.sin(angle)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        this.width = getWidth();
        this.height = getHeight();
        this.CENTRE_X = width / 2;
        this.CENTRE_Y = height / 2;
        shuffle(Arrays.asList(COLORS));
        this.tamanyText = Math.max(10, width / 40);

        this.tamanyDist = Math.max(8, width / 40);

        this.RADI = Math.min(CENTRE_X, CENTRE_Y) - (int) (0.1 * Math.min(CENTRE_X, CENTRE_Y));
        getPosicions();
        Graphics2D g2d = (Graphics2D) g;

        pintarDistancias(g2d);

    }


    private void pintarDistancias(Graphics2D g) {
        double[][] distancies = dades.getDistancies();

        // Recorremos solo la mitad inferior de la matriz.
        for (int i = 0; i < NODES; i++) {
            for (int j = 0; j < NODES; j++) {
                if (distancies[i][j] != 0.0 ) {

                    Color color = COLORS[i % COLORS.length];
                    dibuixaLinea(g, posicions[i], posicions[j],
                            String.format("%.2f", distancies[i][j]), color);
                }
            }
        }
        // Dibuja los nodos
        for (int k = 0; k < NODES; k++) {
            dibuixaNode(g, posicions[k].x, posicions[k].y, 15, Idioma.values()[k].name());
        }
    }


    private void dibuixaLinea(Graphics2D g2d, Point start, Point end, String text, Color labelColor) {
        g2d.setStroke(new BasicStroke(1.5f));

        // Dibuja la línea
        g2d.setColor(Color.BLACK);
        g2d.drawLine(start.x, start.y, end.x, end.y);

        int centerX = (start.x + end.x) / 2;
        int centerY = (start.y + end.y) / 2;

        Font font = new Font("Arial", Font.BOLD, tamanyDist);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics(font);

        int textWidth = fm.stringWidth(text);

        int textX = centerX - textWidth / 2;

        int textY = centerY + fm.getAscent() / 2 - fm.getDescent() / 2;

        // Dibuja el texto centrado
        g2d.setColor(labelColor);
        g2d.drawString(text, textX+3, textY-3);

        g2d.setColor(Color.BLACK);
    }

    private void dibuixaNode(Graphics2D g, int x, int y, int margin, String text) {
        g.fillOval(x - margin, y - margin, 30, 30);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.PLAIN, tamanyText));
        g.drawString(text, x - textWidth / 2, y + textHeight / 4);
        g.setColor(Color.BLACK);

    }


}
