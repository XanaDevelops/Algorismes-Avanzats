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

    private final ThreadPoolExecutor exec =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors() - 1);

    private final Map<Integer, Solver> tasques = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();

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
        int id = idCounter.incrementAndGet();
        // dades.afegirImatge(id, ruta); // model actualitza estat de la imatge
        finestra.progressar(id, 0.0);
    }

    @Override
    public void classificar(int id) {
        // ImageData img = dades.getImage(id);  // agafar les dades de la imatge

        // if (img == null) { return; }

        // Solver s = new Solver(id, img, this); // instanciar solver
        // tasques.put(id, s); // afegir la tasca
        // exec.execute(s); // executar la tasca

    }

    @Override
    public void aturar(int id) {
        Solver s = tasques.remove(id);
        // if (s != null) { s.aturar();}
    }

    @Override
    public void progressar(int id, double percent) {
        SwingUtilities.invokeLater(() -> finestra.progressar(id, percent));
    }

    /*
    @Override
    public void resultat(int id) {
        tasques.remove(id);                      // neteja
        dades.marcarComClassificat(id, res);     // Model
        SwingUtilities.invokeLater(() -> finestra.resultat(id));
    }
    */

    @Override
    public void comunicar(String msg) {
        String[] args = msg.split(":");

        System.err.println("MAIN: missatge? " + msg);
    }
}
