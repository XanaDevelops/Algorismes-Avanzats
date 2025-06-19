package controlador;

import model.Dades;
import model.Solver;
import vista.Finestra;

import javax.swing.*;
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
        });
    }

    @Override
    public void carregarImatge(Path ruta) {
    }

    @Override
    public void classificar() {
    }

    @Override
    public void progressar(double percent) {
        SwingUtilities.invokeLater(() -> finestra.progressar(percent));
    }

    @Override
    public void comunicar(String msg) {
        String[] args = msg.split(":");

        System.err.println("MAIN: missatge? " + msg);
    }
}
