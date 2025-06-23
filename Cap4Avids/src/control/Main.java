package control;

import model.*;
import model.Huffman.Compressor;
import model.Huffman.Decompressor;
import model.Huffman.Huffman;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vista.Finestra;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
            //dades.addComprimit(new File);
        }else{
            //
        }

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

    /**
     * Envia un missatge
     *
     * @param s El missatge
     */
    @Override
    public void comunicar(String s) {
        String[] parts = s.split(";");
        String cmd  = parts[0];
        String path  = parts.length > 1 ? parts[1] : null;

        switch (cmd) {         //TODO: exportar esto
            case "Eliminar"-> {
                assert path != null;
                removeFitxer(path, path.endsWith(".huf"));
            }
            case "Guardar"->System.out.println("Esperant per guardar");

            default -> System.err.println("WARNING: Main reb missatge?: " + s);
        }
    }

    /**
     * Elimina un fitxer
     */
    private void removeFitxer(String arg, boolean descomprimir) {
        assert arg != null;
        File f = new File(arg);
        if (!descomprimir) {
            dades.removeDescomprimit(f);
            finestra.comunicar("actualitzar;descomprimit");
        } else {
            dades.removeComprimit(f);
            finestra.comunicar("actualitzar;comprimit");
        }
    }


    public Comunicar getFinestra(){
        return finestra;
    }

    public Dades getDades(){
        return dades;
    }
}
