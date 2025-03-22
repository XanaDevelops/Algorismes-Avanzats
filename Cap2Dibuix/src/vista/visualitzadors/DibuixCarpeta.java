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
    private final int xBorder = 10, yBorder = 10;

    private final Color[] colors = {Color.orange, Color.CYAN, Color.green, Color.RED, Color.pink, Color.magenta};

    public DibuixCarpeta(Comunicar principal) {
        this.principal = principal;

    }

    private void colorSwitch(){
        System.err.println("Color switch " + !doColor);
        doColor = !doColor;
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

        System.err.println(Arrays.deepToString(matriu));

        int rows = matriu.length;
        int cols = matriu[0].length;

        int width = getWidth()-xBorder;
        int height = getHeight()-yBorder;

        double widthRatio = (double)width / (double)(cols);
        double heightRatio = (double)height / (double)(rows);

        for (int i = 0; i < rows; i++) {
            int colorI = i;
            for (int j = 0; j < cols; j++) {
                if (matriu[i][j] != 0) {
                    //calcula la posició relativa a la mida del panell
                    int x = (int)((j)*widthRatio) + (xBorder/2);
                    int y = (int)((i)*heightRatio) + (yBorder/2);

                    if(doColor){
                        g2d.setColor(colors[(colorI++) % colors.length]);
                        g2d.fillRect(x,y, (int)widthRatio, (int)heightRatio);
                    }
                    g2d.setColor(Color.black);
                    g2d.drawRect(x,y, (int)widthRatio, (int)heightRatio);
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
