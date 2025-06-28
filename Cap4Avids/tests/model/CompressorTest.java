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

    String fileName = "tests/res/testABC.txt";

    private void actualTest(Compressor c) throws IOException {
        c.compressFile();
        Decompressor d = new Decompressor(-1, "tests/res/testABC.huf","tests/res/check/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/check/testABC.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }

    @Test
    void testCompressor() throws IOException {
        Compressor c = new Compressor(-1, Huffman.WordSize.BIT8, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        actualTest(c);
    }

    @Test
    void test16Bits() throws IOException {
        Compressor c = new Compressor(-1, Huffman.WordSize.BIT16, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        actualTest(c);
    }
    @Test
    void test32Bits() throws IOException {
        Compressor c = new Compressor(-1, Huffman.WordSize.BIT32, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        actualTest(c);
    }

    @Test
    void test64Bits() throws IOException {
        Compressor c = new Compressor(-1, Huffman.WordSize.BIT64, Huffman.TipusCua.FIB_HEAP, fileName, "tests/res/");
        actualTest(c);
    }
}