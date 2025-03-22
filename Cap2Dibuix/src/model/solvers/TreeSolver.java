package model.solvers;

import model.Dades;
import model.Tipus;
import principal.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TreeSolver implements Runnable, Comunicar {
    private BufferedImage image;
    private Dades data;
    private Main main;
    Graphics2D g;
    private boolean stop;

    public TreeSolver(Main main, Dades data) {
        this.main = main;
        this.data = data;

        data.setTipus(Tipus.TREE);
        int size = 1024;
        image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);

    }

    private void generarArbol(Graphics2D g, int x1, int y1, double angle, int p) {
       if(!stop) {
           if (p == 0) return;

           int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * 25 );
           int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * 25);
           g.setColor(Color.RED);

           g.drawLine(x1, y1, x2, y2);
//            try {
//            Thread.sleep(5/1000000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//           main.comunicar("pintar");

           generarArbol(g, x2, y2, angle - 20, p - 1);
           generarArbol(g, x2, y2, angle + 20, p - 1);
       }
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void run() {
        stop = false;
        //suposant aqui, q
        double tempsEsperat = data.getConstantMultiplicativa()* Math.pow(2,data.getProfunditat());
        main.comunicar("tempsEsperat:"+ tempsEsperat);//??

        long time = System.nanoTime();
        generarArbol(g, image.getHeight()/2, image.getHeight()-300, -90, data.getProfunditat());
        main.comunicar("aturar");

        time = System.nanoTime() - time;
        System.out.println("Temps real " + time + " segons");
        main.comunicar("tempsReal:"+ time);
        g.dispose();
        if (!stop) {
            aturar();
        }

    }

    @Override
    public void comunicar(String s) {
        switch (s){
            case "aturar":
                System.err.println("ATURAR carpeta sierpinski");
                break;
        }

    }
    private void aturar() {
        stop = true;
    }




}
