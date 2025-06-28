package model;

import control.Main;
import model.Huffman.Compressor;
import model.Huffman.Decompressor;
import model.Huffman.Huffman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CompressorTest {

    @BeforeEach
    void setUp() {
        Main.main(null);
    }


    String path = "tests/res/";
    String[] names = {"testABC", "testAllSmall", "testAll"};

    private void actualTest(Compressor c, String name) throws IOException {
        c.compressFile();
        Decompressor d = new Decompressor(-1, path+name+".huf","tests/res/check/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(path+name+".txt")),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/check/"+name+".txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
        System.out.println("OK " + name);
    }

    @Test
    void test8Bits() throws IOException {
        for(String name : names) {
            Compressor c = new Compressor(-1, Huffman.WordSize.BIT8, Huffman.TipusCua.FIB_HEAP, path+name+".txt", "tests/res/");
            actualTest(c, name);
        }
    }

    @Test
    void test16Bits() throws IOException {
        for(String name : names) {
            Compressor c = new Compressor(-1, Huffman.WordSize.BIT16, Huffman.TipusCua.FIB_HEAP, path+name+".txt", "tests/res/");
            actualTest(c, name);
        }
    }
    @Test
    void test32Bits() throws IOException {
        for(String name : names) {
            Compressor c = new Compressor(-1, Huffman.WordSize.BIT32, Huffman.TipusCua.FIB_HEAP, path+name+".txt", "tests/res/");
            actualTest(c, name);
        }
    }

    @Test
    void test64Bits() throws IOException {
        for(String name : names) {
            Compressor c = new Compressor(-1, Huffman.WordSize.BIT64, Huffman.TipusCua.FIB_HEAP, path+name+".txt", "tests/res/");
            actualTest(c, name);
        }
    }
}