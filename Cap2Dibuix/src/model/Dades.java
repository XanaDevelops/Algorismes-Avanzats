package model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Dades {
    private BufferedImage image;
    private int profunditat;
    private double ConstantMult = 1.0;
    private int xRetjola;
    private int yRetjola;


    public Dades(int width, int height, int profunditat, int xRetjola,  int yRetjola) {
        this.profunditat = profunditat;
        this.xRetjola = xRetjola;
        this.yRetjola = yRetjola;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

    }

    public int getxRetjola() {
        return xRetjola;
    }

    public void setxRetjola(int xRetjola) {
        this.xRetjola = xRetjola;
    }

    public int getyRetjola() {
        return yRetjola;
    }

    public void setyRetjola(int yRetjola) {
        this.yRetjola = yRetjola;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getProfunditat() {
        return profunditat;
    }

    public void setProfunditat(int profunditat) {
        this.profunditat = profunditat;
    }

    public  double getConstantMult() {
        return this.ConstantMult;
    }

    public  void setConstantMult(double constantMult) {
        this.ConstantMult = constantMult;
    }
}
