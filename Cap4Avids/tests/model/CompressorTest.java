package model;

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
    }

    @Test
    void testCompressor() throws IOException {

        Dades dades = new Dades();
        String fileName = "tests/res/testABC.txt";
        Huffman h = new Huffman(fileName );
        h.run();
        Compressor c = new Compressor(h, dades,fileName, "tests/res/");
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

        Dades dades = new Dades();
        String fileName = "tests/res/testA.txt";
        Huffman h = new Huffman(fileName, Huffman.WordSize.BIT16);
        h.run();
        Compressor c = new Compressor(h, dades,fileName, "tests/res/");
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

        Dades dades = new Dades();
        String fileName = "tests/res/testA.txt";
        Huffman h = new Huffman(fileName, Huffman.WordSize.BIT32);
        h.run();
        Compressor c = new Compressor(h, dades,fileName, "tests/res/");
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

        Dades dades = new Dades();
        String fileName = "tests/res/testA.txt";
        Huffman h = new Huffman(fileName, Huffman.WordSize.BIT64);
        h.run();
        Compressor c = new Compressor(h, dades,fileName, "tests/res/");
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