package controlador;

import model.Dades;
import model.Solver;
import vista.Finestra;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

public class Main implements Comunicar {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private Dades dades;
    private Comunicar finestra;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
    private final Map<Integer, Solver> runnables = new TreeMap<>();

    public Main() {
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
    public void comunicar(String s) {
        String[] args = s.split(":");

        System.err.println("MAIN: missatge? " + s);
    }

    @Override
    public synchronized void calcular(int N) {
        int id = dades.getIdCount();
        Solver s = null;
        try {
            s = new Solver(id, N);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Solver finalS = s;
        executor.execute(() -> {
            try {
                Solver.Node sol = finalS.call();
                System.out.println("[Solver " + id + "] Sol:  " + sol.toString());
            } catch (Exception e) {
                System.out.println("[Solver " + id + "] Error: " + e.getMessage());
            } finally {
                // Notifiquem a la UI l'estat final
                SwingUtilities.invokeLater(() -> finestra.actualitzar(id));
            }
        });
        runnables.put(id, s);
    }

    @Override
    public void calcular(int[][] adj) {
        int id = dades.getIdCount();
        Solver solver = null;
        try {
            solver = new Solver(id, adj);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        runnables.put(id, solver);

        Solver finalSolver = solver;
        executor.submit(() -> {
            try {
                Solver.Node sol = finalSolver.call();
                comunicar("[Solver " + id + "] Solució obtinguda: " + sol);
            } catch (Exception e) {
                comunicar("[Solver " + id + "] Error: " + e.getMessage());
            } finally {
                SwingUtilities.invokeLater(() -> finestra.actualitzar(id));
            }
        });
    }

    @Override
    public void pausar(int id) {
        Solver s = runnables.get(id);
        if (s != null) {
            s.pause();
            System.err.println("Tasca " + id + " pausada.");
            finestra.actualitzar(id);
        }
    }

    @Override
    public void reanudar(int id) {
        Solver s = runnables.get(id);
        if (s != null) {
            s.resume();
            System.err.println("Tasca " + id + " reanudada.");
            finestra.actualitzar(id);
        }
    }

    @Override
    public void aturar(int id) {
        Solver s = runnables.remove(id);
        if (s != null) {
            s.interrompre();
            System.err.println("Tasca " + id + " aturada definitivament.");
            finestra.aturar(id);
        }
    }

    @Override
    public void actualitzar(int id) {
        finestra.actualitzar(id);
    }
}
