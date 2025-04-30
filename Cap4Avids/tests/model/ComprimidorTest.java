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

class ComprimidorTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testComprimidor() throws IOException {

        Dades dades = new Dades();
        String fileName = "tests/res/testABC.txt";
        Huffman h = new Huffman(fileName );
        h.run();
        dades.setExtensioComprimit(Extensio.LZH);
        Compressor c = new Compressor(h, dades,fileName, "tests/res/");
        c.compressFile();
        Decompressor d = new Decompressor("tests/res/Compressed testABC.lzh","tests/res/");
        d.decompressFile();
        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "tests/res/Decompressed testABC.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }
}