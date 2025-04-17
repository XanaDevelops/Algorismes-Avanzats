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
    }
    @org.junit.jupiter.api.Test
    void testFileABC(){
        Huffman huffman = new Huffman("res/testABC.txt");
        huffman.run();
        System.out.println(Arrays.toString(huffman.getFreqs()));
        System.out.println(huffman.getTable());
    }
}