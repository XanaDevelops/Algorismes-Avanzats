package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class DibuixTromino extends JPanel implements Comunicar {


    private final static Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK, Color.YELLOW};
    private final Comunicar principal;
    private boolean colorON = false;
    private final Map<Integer, Color> trominoColors = new HashMap<>();


    /**
     * Inicialitza els límits del panell del, i la instància de classe.
     *
     * @param w amplada del panell del Dibuix
     * @param h altura del panell del Dibuix
     * @param p instància del programa principal
     */
    public DibuixTromino(int w, int h, Comunicar p) {
        this.principal = p;
        this.setBounds(0, 0, w, h);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                detectarCasella(e.getX(), e.getY());
            }
        });
    }

    public void colorON() {
        this.colorON = !colorON;
        repaint();


    }

    private void dibuixarVoraExterior(int[][] matriu, int i, int j, Graphics g) {

        int id = matriu[i][j];
        int files = matriu.length;
        int columnes = matriu[0].length;
        int midaCellx = this.getWidth() / columnes;
        int midaCelly = this.getHeight() / files;
        int x = j * midaCellx;
        int y = i * midaCelly;

        // Vora superior
        if (i == 0 || matriu[i - 1][j] != id) {
            g.drawLine(x, y, x + midaCellx, y);
        }
        // Vora inferior
        if (i == matriu.length - 1 || matriu[i + 1][j] != id) {
            g.drawLine(x, y + midaCelly, x + midaCellx, y + midaCelly);
        }
        // Vora esquerra
        if (j == 0 || matriu[i][j - 1] != id) {
            g.drawLine(x, y, x, y + midaCelly);
        }
        // Vora dreta
        if (j == matriu[0].length - 1 || matriu[i][j + 1] != id) {
            g.drawLine(x + midaCellx, y, x + midaCellx, y + midaCelly);
        }
    }

    private Color getColorForTromino(int[][] matriu, int i, int j) {
        int id = matriu[i][j];

        // Si el tromino ja té color, el retornem
        if (trominoColors.containsKey(id)) {
            return trominoColors.get(id);
        }

        // Registrar colors usats pels trominos veïns
        HashSet<Color> colorsUsats = new HashSet<>();

        // Buscar els veïns i afegir els colors que ja estan usats
        int[][] direccions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : direccions) {
            int ni = i + d[0];
            int nj = j + d[1];
            if (ni >= 0 && ni < matriu.length && nj >= 0 && nj < matriu[0].length) {
                int nid = matriu[ni][nj];
                if (trominoColors.containsKey(nid)) {
                    colorsUsats.add(trominoColors.get(nid));
                }
            }
        }

        // Selecionar disponibles
        ArrayList<Color> colorsDisponibles = new ArrayList<>();
        for (Color color : colors) {
            if (!colorsUsats.contains(color)) {
                colorsDisponibles.add(color);
            }
        }

        // Si colors disponibles, escollim un a l'atzar
        Color colorFinal;
        if (!colorsDisponibles.isEmpty()) {
            colorFinal = colorsDisponibles.get(new Random().nextInt(colorsDisponibles.size()));
        } else {
            // tria un per defecte
            colorFinal = colors[new Random().nextInt(colors.length)];
        }

        trominoColors.put(id, colorFinal);
        return colorFinal;
    }


    private void detectarCasella(int x, int y) {
        int[][] matriu = ((Main) (principal)).getMatriu();
        if (matriu == null) return;

        int files = matriu.length;
        int columnes = (files > 0) ? matriu[0].length : 1;

        int midaCellx = this.getWidth() / columnes;
        int midaCelly = this.getHeight() / files;

        int fila = y / midaCelly;
        int columna = x / midaCellx;

        if (fila >= 0 && fila < files && columna >= 0 && columna < columnes) {
            principal.comunicar("inici:" + columna + ":" + fila);
        }
    }

    /**
     * Pinta el panell de la gràfica
     *
     * @param g
     */

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);


        int[][] matriu = ((Main) (principal)).getMatriu();
        if (matriu == null) {
            System.err.println("No se pot pintar la matriu");
            return;
        }

        int files = matriu.length;
        int columnes = (files > 0) ? matriu[0].length : 1;

        int midaCellx = this.getWidth() / columnes;
        int midaCelly = this.getHeight() / files;

        int iniciX = 0, iniciY = 0;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                // Dibuixar línies guia de color gris
                g.setColor(Color.WHITE);
                g.drawRect(j * midaCellx, i * midaCelly, midaCellx, midaCelly);
                if (matriu[i][j] == -1) {
                    iniciX = j;
                    iniciY = i;
                } else if (matriu[i][j] != 0) { // Suposant que -1 significa buit
                    if (colorON) {
                        g.setColor(getColorForTromino(matriu, i, j));
                        g.fillRect(j * midaCellx, i * midaCelly, midaCellx, midaCelly);
                    }
                    // Dibuixar només les vores exteriors
                    g.setColor(Color.BLACK);
                    dibuixarVoraExterior(matriu, i, j, g);

                }
            }
        }
        g.setColor(Color.RED);
        g.drawRect(iniciX * midaCellx, iniciY * midaCelly, midaCellx, midaCelly);
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
