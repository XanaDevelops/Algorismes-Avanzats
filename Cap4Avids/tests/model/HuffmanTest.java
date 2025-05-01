package model;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanTest {



    @Test
    void testFileAll() {
        Huffman huffman = new Huffman("tests/res/testAll.txt");
        huffman.run();
        Map<Long, Long> freqs = huffman.getFreqs();
        for (Map.Entry<Long, Long> e : freqs.entrySet()) {
            assertEquals(e.getKey()+1, e.getValue());
        }

        System.out.println(huffman.getTable());
        System.out.println("Entropia: " + huffman.getEntropia());
        Set<String> values = new HashSet<>();
        for (String b: huffman.getTable().values()){
            if(values.contains(b)){
                fail("Duplicate value: "+b);
            }
            values.add(b);
        }

        Huffman huffman2 = new Huffman("tests/res/testAll.txt", Huffman.TipusCua.FIB_HEAP);
        huffman2.run();
        Map<Long, Long> freqs2 = huffman.getFreqs();
        for (Map.Entry<Long, Long> e : freqs2.entrySet()) {
            assertEquals(e.getKey()+1, e.getValue());
        }
        System.out.println(huffman2.getTable());
        System.out.println("Entropia2: " + huffman2.getEntropia());
        System.out.println("\n");
    }
    @Test
    void testFileABC(){
        Huffman huffman = new Huffman("tests/res/testABC.txt");
        huffman.run();
        System.out.println(huffman.getFreqs());
        System.out.println(huffman.getTable());

        Huffman huffman2 = new Huffman("tests/res/testABC.txt", Huffman.TipusCua.FIB_HEAP);
        huffman2.run();
        System.out.println(huffman2.getFreqs());
        System.out.println(huffman2.getTable());

        System.out.println("\n");

    }

    @Test
    void testFileABC2(){
        Huffman huffman = new Huffman("tests/res/testABC2.txt");
        huffman.run();
        System.out.println(huffman.getFreqs());
        System.out.println(huffman.getTable());

        Huffman huffman2 = new Huffman("tests/res/testABC2.txt", Huffman.TipusCua.FIB_HEAP);
        huffman2.run();
        System.out.println(huffman2.getFreqs());
        System.out.println(huffman2.getTable());

        System.out.println("\n");

    }

    @Test
    void testFileABC3(){
        Huffman huffman = new Huffman("tests/res/testABC3.txt");
        huffman.run();
        System.out.println(huffman.getFreqs());
        System.out.println(huffman.getTable());
        System.out.println(huffman.getEntropia());

    }

    @Test
    void testThread(){
        Huffman huffman = new Huffman("tests/res/testAll.txt");
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);
        Future<?> h = executor.submit(huffman);
        try {
            h.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(huffman.getFreqs());
        System.out.println(huffman.getTable());
    }
    @Test
    void test16Bits(){
        Huffman huffman = new Huffman("tests/res/testA.txt", Huffman.WordSize.BIT16);
        huffman.run();
        System.out.println(huffman.getFreqs().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%X}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
        System.out.println(huffman.getTable().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%s}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
    }
    @Test
    void test32Bits(){
        Huffman huffman = new Huffman("tests/res/testA.txt", Huffman.WordSize.BIT32);
        huffman.run();
        System.out.println(huffman.getFreqs().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%X}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
        System.out.println(huffman.getTable().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%s}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
    }
    @Test
    void test64Bits(){
        Huffman huffman = new Huffman("tests/res/testA.txt", Huffman.WordSize.BIT64);
        huffman.run();
        System.out.println(huffman.getFreqs().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%X}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
        System.out.println(huffman.getTable().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%s}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));

        huffman = new Huffman("tests/res/testAll.txt", Huffman.WordSize.BIT64);
        huffman.run();
        System.out.println(huffman.getFreqs().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%X}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
        System.out.println(huffman.getTable().entrySet().stream()
                .map(e -> String.format("{0x%X=0x%s}", e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
    }

}