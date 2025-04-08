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
            for (Punt2D punt2D : punts) {
                if (maxX == 0) {
                    break;
                }
                g.setColor(new Color(102, 178, 255));
                px = 50 + punt2D.getX() * (width - 60) / maxX;
                py = (height - 20) - (punt2D.getY() * (height - 40)) / maxY;
                g.fillOval(px - 3, py - 3, 4, 4);
                g.setColor(Color.black);
                g.drawOval(px - 3, py - 3, 4, 4);


            }

            //draw linias de la distància
            if (!punts.isEmpty()) {

//                drawDistance(punts.get(0), punts.get(1), g, maxX, maxY);

            }

        }

    }

    private void drawDistance (Punt2D p1, Punt2D p2, Graphics g, int maxX, int maxY) {
        g.setColor(Color.RED);
        double distance = Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
        int p1x = 50 + p1.getX() * (width - 60) / maxX;
        int p1y =  (height - 20) - (p1.getY() * (height - 40)) / maxY;
        int p2x = 50 + p2.getX() * (width - 60) / maxX;
        int p2y = (height - 20) - (p2.getY() * (height - 40)) / maxY;
        g.drawLine(p1x-1, p1y-1, p2x-1, p2y-1);

        int midX = (p1x+p1y) / 2;
        int midY = (p2x+p2y) / 2;
        g.drawString(String.format("%.2f", distance), midX, midY - 10);
    }
}
