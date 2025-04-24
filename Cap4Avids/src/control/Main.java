package control;

import model.Dades;
import vista.Finestra;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar {

    public static final Main instance = new Main();

    private Finestra finestra;
    private Dades dades;

    public final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);

    public static void main(String[] args) {
        instance.start();
    }

    public Main() {


    }



    private void start(){
        dades = new Dades();

        SwingUtilities.invokeLater(() -> finestra = new Finestra());
    }

    /**
     * Envia un missatge
     *
     * @param s El missatge
     */
    @Override
    public void comunicar(String s) {
        String[] args = s.split(":");
        switch (args[0]) {
            default -> System.err.println("WARNING: Main reb missatge?: " + s);
        }
    }

    public Comunicar getFinestra(){
        return finestra;
    }

    public Dades getDades(){
        return dades;
    }
}
