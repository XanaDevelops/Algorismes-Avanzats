package model;

import model.cues.FibonacciHeap;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;

public class Huffman implements Runnable {
    public static enum TipusCua{
        BIN_HEAP(PriorityQueue.class),
        FIB_HEAP(FibonacciHeap.class)
        ;

        private Class<? extends AbstractQueue> cua;

        TipusCua(Class<? extends AbstractQueue> cua) {
            this.cua = cua;
        }

        public Class<? extends AbstractQueue> getCua() {
            return this.cua;
        }
    }
    public static class Node implements Comparable<Node> {
        public int val;
        public int bval;
        private Node left, right;

        public Node(int val, int bval) {
            this.val = val;
            this.bval = bval;
        }

        public boolean isLeaf() {
            return left == null && right == null && bval != -1;
        }
        @Override
        public int compareTo(Node o) {
            return Integer.compare(val, o.val);
        }

    }
    private final BufferedInputStream fileIn;
    private final static int N_THREADS = 16;

    //un byte te 256 possibles valors
    public static final int BITSIZE = 256;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS);
    private final List<Future<?>> runnables = new ArrayList<>();

    private final int[][] acumulators = new int[N_THREADS][BITSIZE];
    private byte[] fileBytes;
    private int[] freqs = new int[BITSIZE];

    private final TipusCua tipusCua;

    private Node treeRoot;

    private final ConcurrentHashMap<Byte, Byte> table = new ConcurrentHashMap<>();

    public Huffman(String fileName) {
        this(fileName, TipusCua.BIN_HEAP);
    }

    public Huffman(String fileName, TipusCua tipusCua) {
        this.tipusCua = tipusCua;
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
        calculateFreqs();
        try {
            genTree();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        createTable(treeRoot, (byte) 0);
        joinAll();
    }

    private void createTable(Node node, int val){
        if (node == null) {
            return;
        }
        if (node.isLeaf()){
            table.put((byte) node.bval, (byte) val);
        }else{
            if (node.left != null) addConcurrent(() -> createTable(node.left, (val<<1)));
            if (node.right != null) addConcurrent(() -> createTable(node.right, (val<<1) + 1));
//            if (node.left != null) createTable(node.left, (val<<1));
//            if (node.right != null) createTable(node.right, (val<<1) + 1);
        }
    }

    @SuppressWarnings("unchecked")
    private void genTree() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Queue<Node> pq = tipusCua.getCua().getConstructor().newInstance();
        for (int i = 0; i < freqs.length; i++) {
            if(freqs[i] == 0){
                continue;
            }
            pq.add(new Node(freqs[i], i));
        }
        while (pq.size() > 1){
            Node first = pq.poll();
            Node second = pq.poll();
            Node parent = new Node(first.val + second.val, -1);
            parent.left = first;
            parent.right = second;
            pq.add(parent);
        }

        treeRoot = pq.poll();
    }

    private void calculateFreqs() {
        int split = fileBytes.length / N_THREADS;
        for (int i = 0; i < N_THREADS-1 && split > 0; i++) {
            int j = i;
            addConcurrent(() -> {recursiveAccumulate(j, j*split, (j+1)*split);});
        }
        //assegurar-se final
        addConcurrent(() -> {recursiveAccumulate(N_THREADS-1, (N_THREADS-1)*split, fileBytes.length);});

        //esperar
        joinAll();

        for (int i = 0; i < N_THREADS; i++) {
            for (int j = 0; j < freqs.length; j++) {
                freqs[j] += acumulators[i][j];
            }
        }
    }

    private void recursiveAccumulate(int id, int l, int r){
        for (int i = l; i < r; i++) {
            //bytes en C2, necessari index positiu
            int b = fileBytes[i];
            if(b < 0){
                b = b+256;
            }
            acumulators[id][b]++;
        }
    }

    private void addConcurrent(Runnable r){
        runnables.add(executor.submit(r));
    }

    /**
     * Espera a que tots el fils acabin
     */
    private void joinAll(){
        while (!runnables.isEmpty()) {
            Future<?> runnable = runnables.removeFirst();
            if(runnable == null) continue;
            try {
                runnable.get();

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * retona array de freqüecies absolutes
     * @return Array on a[byte] = freq
     */
    public final int[] getFreqs() {
        return freqs;
    }

    /**
     * Retorna la rel de l'arbre
     * @return Node rel
     */
    public final Node getTree(){
        return treeRoot;
    }

    /**
     * Retorna la tabla de traducció:<br>
     * Byte -> Byte <br>
     * Original -> Codi
     * @return tabla traducció
     */
    public final Map<Byte, Byte> getTable(){
        return table;
    }
}
