package controlador;

import model.ClassHSV;
import model.Dades;
import model.Solver;
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


    public Main(){
        if(instance == null){
            instance = this;
        }else{
            return;
        }

        dades = new Dades();
        finestra = new Finestra();

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

    public static void main(String[] args) {
        new Main();
    }
}
