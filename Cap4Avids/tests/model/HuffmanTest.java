package model;

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
    }
}