package model;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTest {



    @org.junit.jupiter.api.Test
    void testFileAll() {
        Huffman huffman = new Huffman("res/testAll.txt");
        huffman.run();
        int[] freqs = huffman.getFreqs();
        for (int i = 0; i < freqs.length; i++) {
            assertEquals(i+1, freqs[i]);
        }

        System.out.println(huffman.getTable());

        Huffman huffman2 = new Huffman("res/testAll.txt", Huffman.TipusCua.FIB_HEAP);
        huffman2.run();
        int[] freqs2 = huffman2.getFreqs();
        for (int i = 0; i < freqs2.length; i++) {
            assertEquals(i+1, freqs2[i]);
        }
        System.out.println(huffman2.getTable());
        System.out.println("\n");
    }
    @org.junit.jupiter.api.Test
    void testFileABC(){
        Huffman huffman = new Huffman("res/testABC.txt");
        huffman.run();
        System.out.println(Arrays.toString(huffman.getFreqs()));
        System.out.println(huffman.getTable());

        Huffman huffman2 = new Huffman("res/testABC.txt", Huffman.TipusCua.FIB_HEAP);
        huffman2.run();
        System.out.println(Arrays.toString(huffman2.getFreqs()));
        System.out.println(huffman2.getTable());

        System.out.println("\n");

    }

    @org.junit.jupiter.api.Test
    void testFileABC2(){
        Huffman huffman = new Huffman("res/testABC2.txt");
        huffman.run();
        System.out.println(Arrays.toString(huffman.getFreqs()));
        System.out.println(huffman.getTable());

        Huffman huffman2 = new Huffman("res/testABC2.txt", Huffman.TipusCua.FIB_HEAP);
        huffman2.run();
        System.out.println(Arrays.toString(huffman2.getFreqs()));
        System.out.println(huffman2.getTable());

        System.out.println("\n");

    }
}