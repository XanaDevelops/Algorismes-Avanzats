package control;

import model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vista.Finestra;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
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
            case "carregar" -> carregarFitxers(path, parts[2].equalsIgnoreCase("comprimir"));
            case "Eliminar"-> {
                assert path != null;
                removeFitxer(path, path.endsWith(".huf"));
            }

            case "Comprimir"->System.out.println("Esperant per comprimir" + s);
            case "Descomprimir"->System.out.println("Esperant per descomprimir" + s);
            case "Guardar"->System.out.println("Esperant per guardar");

            default -> System.err.println("WARNING: Main reb missatge?: " + s);
        }
    }

    /**
     * Carregar fitxers
     */
    private void carregarFitxers(@NotNull String arg, boolean esAComprimir) {
        File f = new File(arg);

        if (esAComprimir) {
            dades.addComprimit(f);
            finestra.comunicar("actualitzar;comprimit");
        } else {
            dades.addDescomprimit(f);
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
