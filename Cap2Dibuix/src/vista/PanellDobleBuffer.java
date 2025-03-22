package vista;

import principal.Comunicar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public abstract class PanellDobleBuffer extends JPanel implements Comunicar, Runnable{

    protected BufferedImage buffer;

    private FrameTick tick;
    protected boolean aturar = false;
    public static final int FPS = 60;

    public PanellDobleBuffer() {
        Thread a = new Thread(this);
        a.start();

    }

    @Override
    public void repaint(){
        if(this.getGraphics() != null){
            paint(this.getGraphics());
        }
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        if(buffer == null){
            buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        buffer.getGraphics().setColor(Color.white);
        buffer.getGraphics().fillRect(0, 0, getWidth(), getHeight());
        pintar(buffer.getGraphics());

        g2d.drawImage(buffer, 0, 0, this);
    }

    public abstract void pintar(Graphics g);

    @Override
    public void run(){
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        tick = new FrameTick();
        tick.start();
    }

    @Override
    public void comunicar(String s){
        switch (s){
            case "aturar":
                aturar = true;
        }
    }

    private class FrameTick extends Thread {
        @Override
        public void run() {
            while(!aturar) {
                esperar();
            }
        }

        private void esperar(){
            try {
                repaint();
                sleep(1000/FPS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
