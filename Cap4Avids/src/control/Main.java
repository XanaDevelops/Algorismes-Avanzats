package control;

import model.Compressor;
import model.Dades;
import model.Extensio;
import model.Huffman;
import vista.Finestra;

import javax.swing.*;
import java.io.IOException;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar {

    public static final Main instance = new Main();

    private Finestra finestra;
    private Dades dades;

    public final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);

    public static void main(String[] args) throws IOException {
        instance.start();
    }

    public Main() {


    }



    private void start() throws IOException {
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
        // Missatges de la forma "Comanda:fitxer"
        String[] parts = s.split(":", 2);
        String cmd  = parts[0];
        String path  = parts.length > 1 ? parts[1] : null;

        switch (cmd) {
            case "Carregar" -> carregarFitxers(path);
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
    private void carregarFitxers(String arg) {
        assert arg != null;
        File f = new File(arg);
        String nom = f.getName().toLowerCase();
        if (nom.endsWith(".huf")) {
            dades.addComprimit(f);
            finestra.comunicar("actualitzar:comprimit");
        } else {
            dades.addDescomprimit(f);
            finestra.comunicar("actualitzar:descomprimit");
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
            finestra.comunicar("actualitzar:descomprimit");
        } else {
            dades.removeComprimit(f);
            finestra.comunicar("actualitzar:comprimit");
        }
    }


    public Comunicar getFinestra(){
        return finestra;
    }

    public Dades getDades(){
        return dades;
    }
}
