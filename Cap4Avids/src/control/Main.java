package control;

import model.*;
import model.Huffman.Compressor;
import model.Huffman.Decompressor;
import model.Huffman.Huffman;
import vista.Finestra;
import vista.FinestraInfo;
import vista.PanellInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar {
    FinestraInfo finestraInfo;
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


        SwingUtilities.invokeLater(() -> finestra = new Finestra());

    }

    @Override
    public void comprimir(int id, String fileIn, String folderOut, Huffman.WordSize wordSize, Huffman.TipusCua tipusCua) {
        executar(id, new Compressor(id, wordSize, tipusCua, fileIn, folderOut));
    }

    @Override
    public void descomprimir(int id, String fileIn, String folderOut) {
        executar(id, new Decompressor(id, fileIn, folderOut));
    }

    @Override
    public void afegirEnEspera(int id, File file, boolean aComprimir) {
        if(aComprimir){
            dades.addDescomprimit(id, file);
        }else{
            dades.addComprimit(id, file);
        }
        finestra.actualitzar();
    }

    @Override
    public void arrancar(int id) {
        finestra.arrancar(id);
    }

    @Override
    public void finalitzar(int id) {
        processos.remove(id);
        finestra.finalitzar(id);
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
        if (!descomprimir) {
            dades.removeAComprimir(file);
        } else {
            dades.removeADescomprimir(file);
        }
        finestra.actualitzar();
    }

    /**
     * Elimina un fitxer
     */
    private void removeFitxer(String arg, boolean descomprimir) {
        assert arg != null;
        File f = new File(arg);
        if (!descomprimir) {
            dades.removeAComprimir(f);
        } else {
            dades.removeADescomprimir(f);
        }
        finestra.actualitzar();
    }

    @Override
    public void estadistiquesLLestes() {
       if (finestraInfo==null){
           finestraInfo = new FinestraInfo();
       }
       finestraInfo.estadistiquesLLestes();
    }


    public Comunicar getFinestra(){
        return finestra;
    }

    public Dades getDades(){
        return dades;
    }
}
