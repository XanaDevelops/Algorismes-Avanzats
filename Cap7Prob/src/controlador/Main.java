package controlador;

import model.*;
import vista.Finestra;
import vista.FinestraColors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main implements Comunicar {
    private static Main instance;

    private Dades dades;
    private Finestra finestra;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        if(instance == null){
            instance = this;
        }else{
            return;
        }

        dades = new Dades();
        SwingUtilities.invokeLater(() -> finestra = new Finestra());
        FinestraColors finestraColors = new FinestraColors();
        finestraColors.setVisible(true);


    }


    public Dades getDades(){
        return dades;
    }

    public Comunicar getFinestra(){
        return finestra;
    }

    public static Main getInstance(){
        return instance;
    }

    @Override
    public void comunicar(String msg) {
        System.err.println(msg);
    }


    @Override
    public void classificarHSV() {
        ClassHSV classHSV = new ClassHSV();
        classHSV.run();
        finestra.actualitzarFinestra();

    }

    public void classificarXarxa(){
        XarxaSolver xarxaSolver = new XarxaSolver();
    }

}
