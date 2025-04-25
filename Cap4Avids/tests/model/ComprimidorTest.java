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
        System.out.println(Arrays.toString(a));

        byte[] b = inB.readAllBytes();
        System.out.println(Arrays.toString(b));
        assertArrayEquals(a, b);
    }
}