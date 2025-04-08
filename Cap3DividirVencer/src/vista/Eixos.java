package vista;

import controlador.Main;
import model.Dades;
import model.punts.Punt;
import model.punts.Punt2D;

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


   public synchronized void pintar() {

       if(this.getGraphics() != null){
           paint(this.getGraphics());
       }
   }
//    private void iniciar(Graphics g) {
//        g.setColor(Color.white);
//        g.fillRect(0, 0, width, height);
//
//        g.setColor(new Color(64, 181, 217));
//
//        //dibuixar eixos
//        g.drawLine(MARGIN, height-MARGIN, width-MARGIN, height-MARGIN);
//        g.drawLine(MARGIN, height-MARGIN, MARGIN, MARGIN);
//    }

    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);

        g.setColor(new Color(64, 181, 217));

        //dibuixar eixos
        g.drawLine(MARGIN, height - MARGIN, width - MARGIN, height - MARGIN);
        g.drawLine(MARGIN, height - MARGIN, MARGIN, MARGIN);

        g.setColor(new Color(64, 181, 217));
        if (dades != null) {
            List<Punt2D> punts = dades.getPunts();
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
            for (Punt2D punt2D : punts) {
                if (punt2D != null) {
                    if (punt2D.getX() > maxX) {
                        maxX = punt2D.getX();
                    }
                    if (punt2D.getY() > maxY) {
                        maxY = punt2D.getY();
                    }
                }
            }
            int px, py;
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < punts.size(); i++) {
                if (maxX == 0){
                    break;
                }
                Punt2D punt = punts.get(i);
                g.setColor(new Color (102, 178, 255));
                px = 50 + punt.getX() * (width - 60) / maxX;
                System.out.println("PX = "+ px);
                py = (height-20) - (punt.getY() * (height - 40) )/ maxY;
                System.out.println("PY = "+ py);
                g.fillOval(px - 3, py - 3, 3, 3);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 3, 3);


            }


        }

    }
}
