package control;

import model.*;
import model.Huffman.Compressor;
import model.Huffman.Decompressor;
import model.Huffman.Huffman;
import vista.Finestra;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar {
    public static final Main instance = new Main();

    private Finestra finestra;
    private Dades dades;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);
    private final Map<Integer, Comunicar> processos = new TreeMap<>();

    public static void main(String[] args) {
        instance.start();
    }

    public Main() {

    }

    private void start(){
        dades = new Dades();

//        SwingUtilities.invokeLater(() -> finestra = new Finestra());
        finestra = new Finestra();
        finestra.setVisible(true);
        //dades.setInfo(new Dades.Informacio(12.2, 120, 100,32,9 ));

    }

    @Override
    public void comprimir(int id, String fileIn, String folderOut, Huffman.WordSize wordSize, Huffman.TipusCua tipusCua) {
        executar(id, new Compressor(id, wordSize, tipusCua, fileIn, folderOut));
    }

    @Override
    public void descomprimir(int id, String fileIn, String folderOut) {
        executar(id, new Decompressor(id, fileIn, folderOut)); // torna a ser la ruta completa
    }

    @Override
    public int afegirEnEspera(File file) {
        int id = Dades.getTaskId();
        if (file == null) return -1;
        String nom = file.getName().toLowerCase();
        if (nom.endsWith(".huf")) {
            dades.addComprimit(id, file);
        }else if(nom.endsWith(".txt") ||  nom.endsWith(".bin")){
            dades.addDescomprimit(id, file);
        } else {
            JOptionPane.showMessageDialog(finestra,
                    "Extensió no vàlida: " + file.getName(),
                    "Error", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        finestra.actualitzar();
        return id;
    }

    @Override
    public void arrancar(int id) {
        SwingUtilities.invokeLater(() -> {
            finestra.arrancar(id);
        });
    }

    @Override
    public void finalitzar(int id) {
        processos.remove(id);
        SwingUtilities.invokeLater(() -> {
            finestra.finalitzar(id);
        });
    }

    @Override
    public void aturar(int id) {
        Comunicar c = processos.remove(id);
        c.aturar(id);
    }

    private void executar(int id, Comunicar comunicar) {
        executor.execute(() -> ((Runnable)comunicar).run());
        processos.put(id, comunicar);
    }


    @Override
    public void eliminarFitxer(File file, boolean descomprimir) {
        removeFitxer(file, descomprimir);
    }

    public void removeFitxer(File file, boolean descomprimir) {
        if (descomprimir) {
            dades.removeAComprimir(file);
        } else {
            dades.removeADescomprimir(file);
        }
        finestra.actualitzar();
    }

    @Override
    public void estadistiquesLLestes() {

        this.finestra.estadistiquesLLestes();
    }


    public Comunicar getFinestra(){
        return finestra;
    }

    public Dades getDades(){
        return dades;
    }
}
