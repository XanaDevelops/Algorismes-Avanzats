package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DibuixCarpeta extends JPanel implements Comunicar {
    Comunicar principal;

    private boolean doColor = false;

    //Evita que dibuixi just a la borera de la finestra
    private final int xBorder = 0, yBorder = 0;

    private final Color[] colors = {Color.orange, Color.CYAN, Color.green, Color.RED, Color.pink, Color.magenta};
    private int colorIndex = 0;

    public DibuixCarpeta(Comunicar principal) {
        this.principal = principal;

    }

    private void colorSwitch(){
        System.err.println("Color switch " + !doColor);
        doColor = !doColor;
        if(doColor){
            colorIndex = (colorIndex + 1) % colors.length;
        }
        repaint();
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int[][] matriu = ((Main)principal).getMatriu();
        if(matriu == null){
            return;
        }


        int rows = matriu.length;
        int cols = matriu[0].length;

        int width = getWidth()-xBorder;
        int height = getHeight()-yBorder;

        double widthRatio = (double)width / (double)(cols);
        double heightRatio = (double)height / (double)(rows);

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {
                //calcula la posició relativa a la mida del panell

                int x = (int) ((j) * widthRatio) + (xBorder / 2);
                int y = (int) ((i) * heightRatio) + (yBorder / 2);

                Color fg, bg;
                if (doColor) {
                    fg = colors[colorIndex % colors.length];
                    bg = fg.darker();
                } else {
                    fg = Color.BLACK;
                    bg = Color.WHITE;
                }

                if (matriu[i][j] == 0) {
                    g2d.setColor(bg);
                    g2d.fillRect(x,y, (int)Math.ceil(widthRatio), (int)Math.ceil(heightRatio));
                } else {
                    g2d.setColor(fg);
                    g2d.fillRect(x,y, (int)Math.ceil(widthRatio), (int)Math.ceil(heightRatio));
                }


            }
        }

    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {
        switch (s) {
            case "pintar":
                repaint();
                break;
            case "borrar":
                System.err.println("borrar no implementat");
                break;
            case "color":
                colorSwitch();
                break;
        }
    }
}
