package controlador;

import model.ClassHSV;
import model.Dades;
import model.Solver;
import model.XarxaSolver;
import vista.Finestra;
import vista.FinestraColors;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main implements Comunicar {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Dades dades;
    private Comunicar finestra;
    private FinestraColors finestraColors;
    public Main(){
        if (instance == null) {
            instance = this;
        } else {
            return;
        }

        dades = new Dades();
    }

    public static void main(String[] args) {
        new Main().init();
    }

    private void init() {
        SwingUtilities.invokeLater(() -> {
            finestra = new Finestra();
            finestraColors = new FinestraColors();
        });
    }

    @Override
    public void carregarImatge(String ruta) {
        try {
            BufferedImage image = ImageIO.read(new File(ruta));
            if (image == null) {
                finestra.comunicar("error: el fitxer no conté una imatge vàlida.");
            }
            dades.setImatge(image);
        } catch (IOException e) {
            finestra.comunicar("error: " + e.getMessage());
        }
    }

    @Override
    public void progressar(double percent) {
        SwingUtilities.invokeLater(() -> finestra.progressar(percent));
    }

//    @Override
//    public void classificar() {
//        Solver solver = new ClassHSV();
//        executor.submit(solver);
//
//    }

    @Override
    public void actualitzarFinestra(){

        finestra.actualitzarFinestra();
    }
    @Override
    public void comunicar(String msg) {
        String[] args = msg.split(":");

        System.err.println("MAIN: missatge? " + msg);
    }

    @Override
    public void classificarHSV() {
        Solver solver = new ClassHSV();
        executor.submit(solver);
    }

    @Override
    public void classificarXarxa() {
        XarxaSolver xarxaSolver = new XarxaSolver();
        executor.submit(xarxaSolver);
    }

    public Dades getDades() { return this.dades; }

    public Comunicar getFinestra() { return this.finestra; }
}