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

    @Test
    void testCompressor() throws IOException {

        Dades dades = new Dades();
        String fileName = "tests/res/testABC.txt";


        Compressor c = new Compressor(-1, Huffman.WordSize.BIT8, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        c.compressFile();
        Decompressor d = new Decompressor(-1, "tests/res/Compressed testABC.kib","tests/res/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/Decompressed testABC.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }

    @Test
    void test16Bits() throws IOException {


        String fileName = "tests/res/testA.txt";

        Compressor c = new Compressor(-1, Huffman.WordSize.BIT16, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        c.compressFile();
        Decompressor d = new Decompressor(-1, "tests/res/Compressed testA.kib","tests/res/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/Decompressed testA.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }
    @Test
    void test32Bits() throws IOException {

        String fileName = "tests/res/testA.txt";
        Compressor c = new Compressor(-1, Huffman.WordSize.BIT32, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        c.compressFile();
        Decompressor d = new Decompressor(-1, "tests/res/Compressed testA.kib","tests/res/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/Decompressed testA.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }

    @Test
    void test64Bits() throws IOException {
        String fileName = "tests/res/testA.txt";
        Compressor c = new Compressor(-1, Huffman.WordSize.BIT64, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        c.compressFile();
        Decompressor d = new Decompressor(-1, "tests/res/testA.kib","tests/res/check/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/check/testA.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }
}