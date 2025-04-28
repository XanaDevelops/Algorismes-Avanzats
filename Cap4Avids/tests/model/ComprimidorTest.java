package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ComprimidorTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testComprimidor() throws IOException {

        String fileName = "message.txt";
        String path = "tests/res/";
        Huffman huffman = new Huffman(path+fileName);
        huffman.run();
        Dades data = new Dades();
        data.setExtensioComprimit(Extensio.LZH);
        Compressor c = new Compressor(huffman, data, path+fileName,path+ "Compressed.lzh");
        Decompressor d = new Decompressor(path+ "Compressed.lzh" );
        c.compressFile();
        d.decompressFile();

        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(path+fileName)),
                inB = new BufferedInputStream(new FileInputStream(  "Decompressed.txt"));
        byte[] a = inA.readAllBytes();

        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }
}