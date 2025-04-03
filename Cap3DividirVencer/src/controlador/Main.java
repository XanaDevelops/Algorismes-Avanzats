package controlador;


import model.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Comunicar {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    //private Finestra finestra;
    private List<Punt2D> punts;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().init());
    }

    private void init() {
        punts = new ArrayList<>();
        this.comunicar("generar");
        //finestra = new Finestra(this);
    }

    @Override
    public void comunicar(String s) {
        if (s.startsWith("generar")) {
            String[] res = s.split(":");
            int num = Integer.parseInt(res[1]);
            Random r = new Random();
            punts.clear();
            for (int i = 0; i < num; i++)
                punts.add(new Punt2D(r.nextInt() * 600, r.nextInt() * 500));
        }
    }
}