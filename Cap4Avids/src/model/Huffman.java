package model;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class Huffman implements Runnable {
    private final BufferedInputStream fileIn;
    private final static int N_THREADS = 16;

    public static final int BITSIZE = 256;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS);
    private final List<Future<?>> runnables = new ArrayList<>();
    //un byte te 256 possibles valors
    private final int[][] acumulators = new int[N_THREADS][BITSIZE];
    private byte[] fileBytes;
    private int[] freqs = new int[BITSIZE];

    public Huffman(String fileName) {
        try {
            fileIn = new BufferedInputStream(new FileInputStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        try {
            fileBytes = fileIn.readAllBytes();
            fileIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //calcular frecuencies en valor absolut
        int split = fileBytes.length / N_THREADS;
        for (int i = 0; i < N_THREADS-1 && split > 0; i++) {
            int j = i;
            runnables.add(executor.submit(() -> {
                recursiveAccumulate(j, j*split, (j+1)*split);}));
        }
        //assegurar-se final
        runnables.add(executor.submit(() -> {recursiveAccumulate(N_THREADS-1, (N_THREADS-1)*split, fileBytes.length);}));

        //esperar
        for (Future<?> runnable : runnables) {
            try {
                runnable.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < N_THREADS; i++) {
            for (int j = 0; j < freqs.length; j++) {
                freqs[j] += acumulators[i][j];
            }
        }


    }

    private void recursiveAccumulate(int id, int l, int r){
        for (int i = l; i < r; i++) {
            //bytes en C2
            int b = fileBytes[i];
            if(b < 0){
                b = b+256;
            }
            acumulators[id][b]++;
        }
    }

    public int[] getFreqs() {
        return freqs;
    }
}
