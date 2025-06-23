package controlador;

import Model.Calcul.CalculIdiomes;
import Model.Calcul.ProbabilisticWrapper;
import Model.Dades;
import Model.Idioma;
import Vista.Finestra;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar {
    private static Main instance = null;
    private Comunicar finestra;
    private Dades dades;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    private final Map<Integer, Comunicar> runnables = new TreeMap<>();

    public static void main(String[] args) {
        if (args.length == 0)
            (new Main()).init();
        else
            new Main();
    }

    public Main() {
        if (instance == null) {
            instance = this;
        } else {
            return;
        }
        dades = new Dades();

    }

    private void init() {

        SwingUtilities.invokeLater(() -> finestra = new Finestra());

    }


    @Override
    public void comunicar(String s) {

    }

    @Override
    public void calcular(Idioma a, Idioma b, boolean prob, int percent) {
        this.calcular(a, b, -1, prob, percent);
    }

    @Override
    public void calcular(Idioma a, Idioma b, int id, boolean prob, int percent) {

        if (a == Idioma.TOTS) {
            for (Idioma idioma : Idioma.values()) {
                if (idioma == Idioma.TOTS || idioma == b) {
                    continue;
                }
                addAndExec(idioma, b, dades.getIdCount(), prob, percent);
            }
        } else if (b == Idioma.TOTS) {
            for (Idioma idioma : Idioma.values()) {
                if (idioma == Idioma.TOTS || idioma == a) {
                    continue;
                }
                addAndExec(idioma, a, dades.getIdCount(), prob, percent);
            }
        } else {
            addAndExec(a, b, dades.getIdCount(), prob, percent);
        }
    }


    private void addAndExec(Idioma a, Idioma b, int id, boolean prob, int percent) {
        if (a == b) {
            System.err.println("S'ha intentat calcular la distancia entre iguals " + a + "==" + b);
        }
        System.err.println("Calculant D(" + a + "-" + b + ") " + id + (prob ? "%" + percent : ""));

        CalculIdiomes c;
        if(prob)
            c = new ProbabilisticWrapper(a, b, id, percent/100d);
        else
            c= new CalculIdiomes(a, b, id);

        executor.execute(c);
        runnables.put(id, c);
        finestra.calcular(a, b, id);
    }

    @Override
    public void aturar(int id) {
        Comunicar c = runnables.remove(id);
        if (c != null) {
            c.aturar(id);
            finestra.aturar(id);
        }


    }

    @Override
    public void actualitzar(int id) {
        finestra.actualitzar(id);
    }

    @Override
    public void pasarTemps(int id, long nanos){
        finestra.pasarTemps(id, nanos);
    }

    public static Main getInstance() {
        return instance;
    }

    public Comunicar getFinestra() {
        return finestra;
    }

    public Dades getDades() {
        return dades;
    }
}