package control;

import model.*;
import model.Huffman.Compressor;
import model.Huffman.Huffman;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vista.Finestra;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar {

    public static final Main instance = new Main();

    private Finestra finestra;
    private Dades dades;

    public final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);
    public final List<Future<?>> runnables = new ArrayList<>();

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
        String[] parts = s.split(";");
        String cmd  = parts[0];
        String path  = parts.length > 1 ? parts[1] : null;

        switch (cmd) {
            case "carregar" -> carregarFitxers(path, parts[2].equalsIgnoreCase("comprimir"), Integer.parseInt(parts[3]));
            case "Eliminar"-> {
                assert path != null;
                removeFitxer(path, path.endsWith(".huf"));
            }

            case "Comprimir"->{
                System.err.println(Arrays.toString(parts));
//                runnables.add(executor.submit(() ->
//                {Compressor c = new Compressor(Integer.parseInt(parts[5]), getWordSize(parts[2]),
//                        getTipusCua(parts[3]), path, parts[4]);
//                    c.run();
//                }));
                executor.execute(() ->
                {Compressor c = new Compressor(Integer.parseInt(parts[5]), getWordSize(parts[2]),
                        getTipusCua(parts[3]), path, parts[4]);
                    c.run();
                });

            }
            case "Descomprimir"->System.out.println("Esperant per descomprimir" + s);
            case "Guardar"->System.out.println("Esperant per guardar");

            default -> System.err.println("WARNING: Main reb missatge?: " + s);
        }
    }

    private Huffman.WordSize getWordSize(String s){
        return switch (s) {
            case "16 bits" -> Huffman.WordSize.BIT16;
            case "32 bits" -> Huffman.WordSize.BIT32;
            case "64 bits" -> Huffman.WordSize.BIT64;
            default -> Huffman.WordSize.BIT8;
        };
    }
    private Huffman.TipusCua getTipusCua(String s){
        return switch (s) {
            case "Fibonacci Heap" -> Huffman.TipusCua.FIB_HEAP;
            case "Rank-Pairing Heap"-> Huffman.TipusCua.RANK_PAIRING_HEAP;
            default -> Huffman.TipusCua.BIN_HEAP;
        };
    }

    /**
     * Carregar fitxers
     */
    private void carregarFitxers(@NotNull String arg, boolean esAComprimir, int id) {
        File f = new File(arg);

        if (esAComprimir) {
            dades.addComprimit(id, f);
            finestra.comunicar("actualitzar;comprimit");
        } else {
            dades.addDescomprimit(id, f);
            finestra.comunicar("actualitzar;descomprimit");
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
