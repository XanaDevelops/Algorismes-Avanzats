package control;

import model.CompressorDecompressor;
import model.Dades;
import model.Huffman;
import vista.Finestra;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
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
        String fileName = "testABC.txt";
        String path = "res/";
        Huffman huffman = new Huffman(path+fileName);
        huffman.run();
        CompressorDecompressor c = new CompressorDecompressor(huffman, path+fileName, path+ "Compressed"+ fileName);

        c.compressFile();
        c.decompressFile(path+ "Compressed"+ fileName, path+ "ORIGINAL " + fileName);

        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(path+fileName)),
                inB = new BufferedInputStream(new FileInputStream( path+ "ORIGINAL " + fileName));
        byte[] a = inA.readAllBytes();


        byte[] b = inB.readAllBytes();

        System.out.println(Arrays.equals(a, b));
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
