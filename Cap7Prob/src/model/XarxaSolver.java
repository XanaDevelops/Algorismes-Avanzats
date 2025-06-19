package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XarxaSolver extends Solver{

    Xarxa xarxa;

    public XarxaSolver(){
        xarxa = new Xarxa(8, new int[]{4,4}, Paisatge.values().length);
        File carpeta = new File(Dades.PATH_IMATGES);
        File[] fotos = carpeta.listFiles();

        ArrayList<BufferedImage> images = new ArrayList<>();
        for(File f : fotos){
            try {
                images.add(ImageIO.read(f));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void comunicar(String msg) {

    }

    @Override
    public void run() {

    }
}
