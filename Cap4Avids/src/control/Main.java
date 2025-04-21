package control;

import model.Comprimidor;
import model.Dades;
import model.Huffman;
import vista.Finestra;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
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
        Huffman huffman = new Huffman("src/model/text.txt");
//        huffman.run();
//        Comprimidor c = new Comprimidor("src/model/text.txt");


//        try {
////            c.comprimir(huffman.getTable(), "sortida.txt", false);
////            c.Descomprimir("sortida.txt", "descomprimir.txt");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        SwingUtilities.invokeLater(() -> finestra = new Finestra());
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
