package model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TrominoSolver extends JPanel {

    private int profunditat;
//    private final int size;
    private BufferedImage img;

    public TrominoSolver(int profunditat) {
        this.profunditat = profunditat;
//        this.size = size;

//        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        img = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);

        Graphics g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        drawTrominosRecursively(0, 0, g, img.getHeight(), profunditat, Mode.LU);
//

    }

    private void drawTrominosRecursively(int x, int y, Graphics g, int mida, int profunditat, Mode mode) {
        if (profunditat == 0) {

            g.setColor(Color.BLACK);   // Establecer el color del borde a negro
            drawTromino(x, y, mode, g, mida);


//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            return;
        } else {

            int arees = mida / 2;  // Se divide en 4 partes, y cada parte tendrá la mitad del tamaño de la anterior

            drawTrominosRecursively(x, y, g, arees, profunditat - 1, Mode.LU); // Área superior izquierda
            drawTrominosRecursively(x + arees, y, g, arees, profunditat - 1, Mode.RU); // Área superior derecha
            drawTrominosRecursively(x, y + arees, g, arees, profunditat - 1, Mode.LD); // Área inferior izquierda
            drawTrominosRecursively(x + arees, y + arees, g, arees, profunditat - 1, Mode.RD); // Área inferior derecha

        }
    }

    /**
     * Un tromino ve representat per 3 rectangles
     */
    private void drawTromino(int x, int y, Mode modeRotacio, Graphics g, int size) {
       size /= 2;
        switch (modeRotacio.getValue()) {

            case 0: // Left Down
                g.drawRect(x, y, size, size);
                g.drawRect(x, y + size, size, size);
                g.drawRect(x + size, y + size, size, size);

                g.setColor(Color.WHITE);
                g.drawLine(x , y+size, x + size, y +size);
                g.drawLine(x+size, y + size, x + size, y + 2*size);
                break;

            case 1: //Right Down
                g.drawRect(x, y + size, size, size);
                g.drawRect(x + size, y, size, size);
                g.drawRect(x + size, y + size, size, size);

                g.setColor(Color.WHITE);
                g.drawLine(x+size , y+size, x + 2*size, y +size);
                g.drawLine(x+size, y + size, x + size, y + 2*size);
                break;
            case 2: //LEFT UP
                g.drawRect(x, y, size, size);
                g.drawRect(x, y + size, size, size);
                g.drawRect(x + size, y, size, size);
                g.setColor(Color.WHITE);
                g.drawLine(x , y+size, x + size, y +size);
                g.drawLine(x+size, y , x + size, y + size);
                break;

            case 3:
                g.drawRect(x, y, size, size);
                g.drawRect(x + size, y, size, size);
                g.drawRect(x + size, y + size, size, size);
                g.setColor(Color.WHITE);
                g.drawLine(x +size, y, x + size, y +size);
                g.drawLine(x+size, y + size, x + 2*size, y + size);
                break;
        }
    }

    public BufferedImage getImg() {
        return img;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 0, 0, null);
    }
}
