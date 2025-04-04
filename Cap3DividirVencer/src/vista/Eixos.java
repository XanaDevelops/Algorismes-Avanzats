package vista;

import controlador.Main;
import model.Dades;
import model.punts.Punt;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;


public class Eixos extends JPanel {
   private Dades dades;
    private BufferedImage image;
    private int width;
    private int height;
    private final static int MARGIN = 15;

    public Eixos(int height, int width, Dades dades) {
        this.dades = dades;
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        this.setBounds(0, 0, width, height);

    }


   public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            iniciar(g);

//            g.drawImage(image, 0, 0, this);
        }
   }
    private void iniciar(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(64, 181, 217));

        //dibuixar eixos
        g.drawLine(MARGIN, height-MARGIN, width-MARGIN, height-MARGIN);
        g.drawLine(MARGIN, height-MARGIN, MARGIN, MARGIN);
    }

    private void pintarPunts (Graphics g) {

        g.setColor(new Color(64, 181, 217));
//        List<Punt> punts = dades.getPunts();


    }


}
