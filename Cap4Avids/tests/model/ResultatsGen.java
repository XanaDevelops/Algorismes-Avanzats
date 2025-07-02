package model;

import control.Main;
import model.Huffman.Compressor;
import model.Huffman.Decompressor;
import model.Huffman.Huffman;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ResultatsGen {

    public static final Huffman.TipusCua TIPUS_CUA = Huffman.TipusCua.FIB_HEAP;

    @BeforeAll
    public static void init() {
        Main.main(new String[0]);
    }

    String basePath = "tests/result/";
    String baseFile = basePath+"lorem.txt";
    String genFile = basePath+"lorem_";

    int[] bytesSizes = new int[]{1024, 10240, 102400, 1048576, 10485760, 104857600, 524288000, 1073741824};
    @Test
    public void genFiles() throws IOException {
        File p = new File(basePath);
        for(File f: p.listFiles()){
            if(f.getName().equals("lorem.txt")) continue;
            f.delete();
        }
        byte[] buffer;
        try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(baseFile))){
            buffer = in.readAllBytes();
        }
        for(int b: bytesSizes){
            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(genFile+b+".txt"))){
                int bytesActual = 0;
                while(bytesActual < b){
                    out.write(buffer[bytesActual % buffer.length]);
                    bytesActual++;
                }

            }

        }
    }

    private String getTXT(int b){
        return genFile+b+".txt";
    }
    private String getHUF(int b){
        return genFile+b+".huf";
    }

    private long getMem(){
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024;
    }

    @Test
    public void test() throws IOException {
        System.out.println("HUFFMAN " + Arrays.toString(Huffman.TipusCua.values()));
        for(int i: bytesSizes){
            String txt = getTXT(i);
            System.out.print(i+": \t\t\t");
            for(Huffman.TipusCua cua: Huffman.TipusCua.values()){
                Huffman h = new Huffman(txt, Huffman.WordSize.BIT8, cua);
                long start = System.nanoTime();
                h.run();
                long end = System.nanoTime();
                long m = getMem();
                System.gc();
                System.gc();
                System.out.print((end - start) + "ns, " + m + "B\t\t");
            }
            System.out.println();
        }
        System.out.println("COMPRIMIR");
        for (int i: bytesSizes){
            String txt = getTXT(i);
            System.out.print(i+": \t\t\t");
            Compressor c = new Compressor(-1, Huffman.WordSize.BIT8, TIPUS_CUA, txt, basePath);
            c.compressFile();
            Dades.Informacio inf = Main.instance.getDades().getInfo();
            Decompressor d = new Decompressor(-1, getHUF(i), basePath);
            d.decompressFile();
            Dades.Informacio inf2 = Main.instance.getDades().getInfo();
            System.out.println("C: " + inf.tempsCompressio + "ns, " + "D: " + inf2.tempsDecompressio+"ns, "+
                    inf.tamanyComprimit + "B, " + (1-(inf.tamanyComprimit/(double)inf.tamanyOriginal)));
        }

        System.out.println("COMPARATIVA BYTES");
        int i = bytesSizes[5];
        String txt = getTXT(i);
        for(Huffman.WordSize w: Huffman.WordSize.values()){
            Compressor c = new Compressor(-1, w, TIPUS_CUA, txt, basePath);
            c.compressFile();
            Dades.Informacio inf = Main.instance.getDades().getInfo();
            System.out.println(w+": " + inf.tempsCompressio + "ns, " + inf.tamanyComprimit + "B\t\t");
        }
    }
}
