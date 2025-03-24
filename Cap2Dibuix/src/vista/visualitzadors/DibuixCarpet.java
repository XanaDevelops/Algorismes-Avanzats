package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class DibuixCarpet extends CanvasDobleBuffer implements Comunicar {


    Comunicar principal;

    private boolean doColor = false;

    private final Color[] colors;
    private int colorIndex = 0;


    public DibuixCarpet(Comunicar principal) {
        super();
        this.principal = principal;
        this.colors = ((Main)principal).getDades().getColors();
    }

    private void colorSwitch(){
        colorIndex = (colorIndex + 1) % (colors.length + 1);
        doColor = colorIndex > 0;

        repaint();
    }

    @Override
    protected void pintar(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int[][] matriu = ((Main)principal).getMatriu();
        if(matriu == null){
            return;
        }


        int rows = matriu.length;
        int cols = matriu[0].length;

        int width = getWidth();
        int height = getHeight();

        double widthRatio = (double)width / (double)(cols);
        double heightRatio = (double)height / (double)(rows);

        Color fg, bg;
        if (doColor) {
            fg = colors[colorIndex -1];
            bg = fg.darker();
        } else {
            fg = Color.BLACK;
            bg = Color.WHITE;
        }

        g2d.setColor(bg);
        g2d.fillRect(0, 0, width, height);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                //calcula la posició relativa a la mida del panell
                int x = (int) ((j) * widthRatio);
                int y = (int) ((i) * heightRatio);

                if (matriu[i][j] != 0) {
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
            case "pintar", "borrar":
                break;
            case "color":
                colorSwitch();
                break;
            case "arrancar":
                initBuffers();
                break;
            case "aturar":
                aturar = true;
                break;

        }
    }




}
