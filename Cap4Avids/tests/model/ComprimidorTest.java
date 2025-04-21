package model;

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

        String in = "res/testAll.txt";
        Huffman huffman = new Huffman(in);
        huffman.run();
        Comprimidor c = new Comprimidor(in);

        c.comprimir(huffman.getTable(), in + ".cip", false);
        c.Descomprimir(in + ".cip", in + ".txt");

        //comparar
        BufferedInputStream inA = new BufferedInputStream(new FileInputStream(in)),
                    inB = new BufferedInputStream(new FileInputStream(in + ".txt"));
        byte[] a = inA.readAllBytes();
        byte[] b = inB.readAllBytes();
        assertArrayEquals(a, b);
    }
}