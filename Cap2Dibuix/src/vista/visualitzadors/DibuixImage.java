package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DibuixImage extends CanvasDobleBuffer implements Comunicar {
    Comunicar principal;

    private boolean doColor = false;



    private final Color[] colors = {Color.orange, Color.CYAN, Color.green, Color.RED, Color.pink, Color.magenta};

    public DibuixImage(Comunicar principal) {
        super();
        this.principal = principal;

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

        BufferedImage image = ((Main)principal).getDades().getImage();
        if(image == null){
            return;
        }

        BufferedImage newImage;
        if(!doColor){
            newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            newImage.getGraphics().setXORMode(Color.BLACK);
            newImage.getGraphics().drawImage(image, 0, 0, null);
        }else{
            newImage = image;
        }

        g2d.drawImage(newImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, this);


    }

    /**
     * @param s
     */
    @Override
    public void comunicar(String s) {
        switch (s) {
            case "pintar":
                break;
            case "borrar":
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
