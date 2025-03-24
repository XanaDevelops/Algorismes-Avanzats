package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;

public class DibuixSierpinski extends CanvasDobleBuffer implements Comunicar {
    Comunicar principal;

    private boolean doColor = false;

    private final boolean zeldaEasterEgg = true;

    //Evita que dibuixi just a la borera de la finestra
    private final int xBorder = 20, yBorder = 20;

    private final Color[] colors;

    public DibuixSierpinski(Comunicar principal) {
        super();
        this.principal = principal;
        this.colors = ((Main)principal).getDades().getColors();
    }

    private void colorSwitch(){
        doColor = !doColor;
        repaint();
    }

    @Override
    public void pintar(Graphics g){
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

        double widthRatio = (double)width / (double)(cols+1);
        double heightRatio = (double)height / (double)(rows);

        for (int i = 0; i < rows; i++) {
            int colorI = i;
            for (int j = 0; j < cols; j++) {
                if (matriu[i][j] == 1) {
                    //asumir diagonals 1
                    //calcula la posició relativa a la mida del panell
                    int[] pix = {(int)((j)*widthRatio) + (xBorder/2), (int)((j+1)*widthRatio) + (xBorder/2), (int)((j+2)*widthRatio) + (xBorder/2)};
                    int[] piy = {(int)((i+1)*heightRatio) + (yBorder/2), (int)((i)*heightRatio) + (yBorder/2), (int)((i+1)*heightRatio) + (yBorder/2)};
                    Polygon p = new Polygon(pix, piy, 3);
                    if(doColor){
                        if (((Main)principal).getDades().getProfunditat() == 2 && zeldaEasterEgg){
                            System.out.println("zeldaEasterEgg");
                            g2d.setColor(Color.decode("#ffd700"));
                        }
                        else {
                            g2d.setColor(colors[(colorI++) % colors.length]);
                        }
                        g2d.fillPolygon(p);
                    }
                    g2d.setColor(Color.black);
                    g2d.drawPolygon(p);
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
