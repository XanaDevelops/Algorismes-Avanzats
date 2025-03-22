package model.solvers;

import model.Dades;
import principal.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TreeSolver implements Runnable, Comunicar {
    private BufferedImage image;
    private Dades data;
    private Main main;
    Graphics2D g;

    public TreeSolver(Main main, Dades data) {
        this.main = main;
        this.data = data;

        int width = 1024;
        int height = 1024;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

    }

    private void generarArbol(Graphics2D g, int x1, int y1, double angle, int p) {
        if (p == 0) return;

        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * 25 );
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * 25);
        g.setColor(Color.RED);

        g.drawLine(x1, y1, x2, y2);

        generarArbol(g, x2, y2, angle - 20, p - 1);
        generarArbol(g, x2, y2, angle + 20, p - 1);
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void run() {
        generarArbol(g, image.getHeight()/2, image.getHeight()-300, -90, data.getProfunditat());

        g.dispose();

    }

    @Override
    public void comunicar(String s) {

    }




}
