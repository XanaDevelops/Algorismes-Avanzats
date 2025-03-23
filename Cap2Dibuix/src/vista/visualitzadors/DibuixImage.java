package vista.visualitzadors;

import principal.Comunicar;
import principal.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DibuixImage extends JPanel implements Comunicar {
    Comunicar principal;

    private boolean doColor = false;



    private final Color[] colors = {Color.orange, Color.CYAN, Color.green, Color.RED, Color.pink, Color.magenta};

    public DibuixImage(Comunicar principal) {
        this.principal = principal;

    }

    private void colorSwitch(){
        doColor = !doColor;
        repaint();
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        BufferedImage image = ((Main)principal).getDades().getImage();
        if(image == null){
            return;
        }


        g2d.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, this);


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
