package Vista;

import Model.Dades;
import Model.Idioma;
import controlador.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

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
    private  int height;
    private  int width;
    private final int CENTRE_X;
    private final int CENTRE_Y;
    private int RADI;
    private Point[] posicions;
    private final int tamanyText;
    private final int tamanyDist;

    public ArbreDistancies() {
        this.dades = Main.getInstance().getDades();

        this.NODES = dades.getIdiomes().size();
//        this.height = 200;
//        this.width = 300;

        this.CENTRE_X = width / 2;
        this.CENTRE_Y = height / 2;
        shuffle(Arrays.asList(COLORS));
        this.tamanyText =  Math.max(10, width/30);
        ;
        this.tamanyDist= Math.max(6, width/30);;

        this.RADI = Math.min(CENTRE_X, CENTRE_Y) - (int) (0.1 * Math.min(CENTRE_X, CENTRE_Y));
        this.setLayout(null);
        //una vegada conegudes les dimensions de la pantalla i altres panels. Es
        //canviarán aquests valors
        this.setPreferredSize(new Dimension(width, height));
    }

    public void getPosicions() {
        int nodes = dades.isParellaOTot() ? NODES : NODES - 1;

        posicions = new Point[nodes];
        for (int i = 0; i < nodes; i++) {
            //per millor distribucio
            double angle = 2 * Math.PI * i / nodes;
            posicions[i] = new Point(CENTRE_X + (int) (RADI * Math.cos(angle)), CENTRE_Y + (int) (RADI * Math.sin(angle)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);

        getPosicions();
        Graphics2D g2d = (Graphics2D) g;

        if (dades.isParellaOTot()) {
            pintarTot(g2d);
        } else
            pintarUnATot(g2d, Idioma.CAT);
    }


    private void pintarUnATot(Graphics2D g, Idioma idioma) {
        double[][] distancies = dades.getDistancies();
        Random r = new Random();
        Color color = COLORS[r.nextInt(COLORS.length)];
        int j = 0;
        for (int i = 0; i < NODES; i++) {
            Idioma id = Idioma.values()[i];
            if (!id.equals(idioma) && distancies[idioma.ordinal()][i]!=0.0) {
                dibuixaLinea(g, new Point(CENTRE_X, CENTRE_Y), posicions[j], String.format("%.2f",distancies[i][j] ), color);
                dibuixaNode(g, posicions[j].x, posicions[j].y, 15, Idioma.values()[i].name());
                j++;
            }
        }
        dibuixaNode(g, CENTRE_X, CENTRE_Y, 15, idioma.name());

    }


    private void pintarTot(Graphics2D g) {
        double[][] distancies = dades.getDistancies();
        for (int i = 0; i < NODES; i++) {
            for (int j = 0; j < i; j++) {
                if (distancies[i][j]!=0.0) {
                    dibuixaLinea(g, posicions[i], posicions[j], String.format("%.2f",distancies[i][j] ), COLORS[i]);
                }
            }
        }
        for (int i = 0; i < NODES; i++) {
            dibuixaNode(g, posicions[i].x, posicions[i].y, 15, Idioma.values()[i].name());
        }
    }

    private void dibuixaLinea(Graphics2D g2d, Point start, Point end, String text, Color labelColor) {
        g2d.setStroke(new BasicStroke(1.5f));

        g2d.setColor(Color.BLACK);
        g2d.drawLine(start.x, start.y, end.x, end.y);

        int px = (start.x + end.x) / 2;
        int py = (start.y + end.y) / 2;
        g2d.setColor(labelColor);
        g2d.setFont(new Font("Arial", Font.BOLD, tamanyDist));
        g2d.drawString(text, px + 5, py - 5);
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
