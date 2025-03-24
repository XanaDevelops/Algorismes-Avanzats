package vista.visualitzadors;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Classe que permet usar doble buffer mitjançant BufferStrategy
 * @see BufferStrategy
 */
public abstract class CanvasDobleBuffer extends Canvas implements Runnable {

    protected boolean aturar = false;
    public static final int FPS = 60;

    public CanvasDobleBuffer() {
        setIgnoreRepaint(true);

    }

    /**
     * Inicialitza els buffers. <br>
     * IMPORTANT: executar això abans de pintar res!!
     */
    protected void initBuffers(){
        createBufferStrategy(2);
        aturar = false;
        new Thread(this).start();
    }

    /**
     * Metode que sustitueix a paint(Graphics g)
     * @param g
     */
    protected abstract void pintar(Graphics g);

    /**
     * Prepara Graphics per a pintar
     */
    private void innerPintar(){
        BufferStrategy bufferStrategy = getBufferStrategy();
        if(bufferStrategy == null){
            return;
        }
        Graphics g = bufferStrategy.getDrawGraphics();
        pintar(g);

        g.dispose();
        if (aturar) {return;}
        bufferStrategy.show();
    }

    public void run(){
        while(!aturar){
            innerPintar();
            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
