package model;

import model.cues.FibonacciHeap;
import model.cues.RankPairingHeap;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Huffman implements Runnable {
    public enum WordSize{
        BIT8(1),
        BIT16(2),
        BIT32(3),
        BIT64(4);
        private int size;
        WordSize(int x){
            this.size = x;
        }
        public int getBitSize() {return size;}
    }
    public enum TipusCua {
        BIN_HEAP(PriorityQueue.class),
        FIB_HEAP(FibonacciHeap.class),
        RANK_PAIRING_HEAP(RankPairingHeap.class);

        private Class<? extends AbstractQueue> cua;

        TipusCua(Class<? extends AbstractQueue> cua) {
            this.cua = cua;
        }

        public Class<? extends AbstractQueue> getCua() {
            return this.cua;
        }
    }

    public static final boolean DO_CONCURRENT = true;
    public static class Node implements Comparable<Node> {
        public long val;
        public long byteVal;
        private Node left, right;

        public Node(long val, long byteVal) {
            this.val = val;
            this.byteVal = byteVal;
        }

        public boolean isLeaf() {
            return left == null && right == null && byteVal != Integer.MIN_VALUE;
        }

        @Override
        public int compareTo(Node o) {
            int valCompare = Long.compare(this.val, o.val);
            if (valCompare == 0) {
                return -Long.compare(this.byteVal, o.byteVal);
            }
            return valCompare;
        }

        public long getByteVal() {
            return byteVal;
        }

        public void setByteVal(int byteVal) {
            this.byteVal = byteVal;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return val == node.val;
        }

        @Override
        public String toString() {
            return "Node(val=" + val + ", byte" + byteVal + ")";
        }

    }

    private final BufferedInputStream fileIn;
    private final static int N_THREADS = 16;

    //un byte te 256 possibles valors
    public static final int BITSIZE = 256;

    private final int byteSize;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS);
    private final List<Future<?>> runnables = Collections.synchronizedList(new ArrayList<>());

    private final Map<Long, Long>[] acumulators;
    private byte[] fileBytes;
    private final Map<Long, Long> freqs;

    private final TipusCua tipusCua;

    private Node treeRoot;

    private double entropia = -1;

    private final ConcurrentHashMap<Long, String> table = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public Huffman(String fileName, WordSize byteSize, TipusCua tipusCua) {
        this.tipusCua = tipusCua;
        this.byteSize = byteSize.size;
        try {
            fileIn = new BufferedInputStream(new FileInputStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.freqs = new HashMap<>((1<< (8* this.byteSize)));
        this.acumulators = Stream.generate(() -> new TreeMap<Long, Long>()).limit(N_THREADS).toArray(TreeMap[]::new);

    }

    public Huffman(String filename) {
        this(filename, WordSize.BIT8, TipusCua.BIN_HEAP);
    }
    public Huffman(String fileName, WordSize byteSize) {
        this(fileName, byteSize, TipusCua.BIN_HEAP);
    }

    public Huffman(String fileName, TipusCua tipusCua) {
        this(fileName, WordSize.BIT8, tipusCua);
    }



    @Override
    public void run() {
        try {
            fileBytes = fileIn.readAllBytes(); //TODO: cambiar a stream o algo... comprobar arxius > 4GB
            fileIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //calcular frecuencies en valor absolut
        calculateFreqs();
        try {
            genTree();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        createTable(treeRoot, "", 0);
        joinAll();
        executor.shutdown();
    }

    private void createTable(Node node, String val, int dbgval) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {

            table.put(node.byteVal, val);
        } else {
            final int fdeep = dbgval;
            if (DO_CONCURRENT) {
                if (node.left != null) addConcurrent(() -> createTable(node.left, val + "0", fdeep+1));
                if (node.right != null) addConcurrent(() -> createTable(node.right, val + "1", fdeep+1));
            }else{
                if (node.left != null) createTable(node.left, val + "0", dbgval+1);
                if (node.right != null) createTable(node.right, val + "1", dbgval+1);
            }

        }
    }

    @SuppressWarnings("unchecked")
    private void genTree() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Queue<Node> pq = tipusCua.getCua().getConstructor().newInstance();
        for (Map.Entry<Long, Long> e : freqs.entrySet()) {

            pq.add(new Node(e.getValue(), e.getKey()));
        }
        while (pq.size() > 1) {
            Node first = pq.poll();
            Node second = pq.poll();
            Node parent = new Node(first.val + second.val, Integer.MIN_VALUE);
            parent.left = first;
            parent.right = second;
            pq.add(parent);
        }

        treeRoot = pq.poll();
    }

    private void calculateFreqs() {
        int split = fileBytes.length / (N_THREADS / byteSize);
        //assegurar-se amb split > 0 que te sentit dividir en threads
        for (int i = 0; i < N_THREADS - 1 && split > 0; i++) {
            int j = i;
            addConcurrent(() -> {
                recursiveAccumulate(j, j * split, (j + 1) * split);
            });
        }
        //assegurar-se final
        addConcurrent(() -> {
            recursiveAccumulate(N_THREADS - 1, (N_THREADS - 1) * split, fileBytes.length);
        });

        //esperar
        joinAll();
        long total = 0;
        for (int i = 0; i < N_THREADS; i++) {
//            for (long j = 0; j < (1L << (8*byteSize)); j++) {
//                //freqs.set(j, freqs.get(j) +  acumulators[i].get(j));
//                freqs.put(j, freqs.getOrDefault(j, 0L) + acumulators[i].getOrDefault(j, 0L));
//                total += acumulators[i].getOrDefault(j, 0L);
//            }
            for(Map.Entry<Long, Long> e : acumulators[i].entrySet()) {
                total += e.getValue();
                freqs.put(e.getKey(), freqs.getOrDefault(e.getKey(), 0L) + e.getValue());
            }
        }

        //calcular entropia
        entropia = 0;
        for(long i: freqs.values()) {
            if (i==0) continue;
            double freq =  i / (double) total;
            entropia += freq * Math.log10(freq)/Math.log10(2);
        }
        entropia = -entropia;
    }

    private void recursiveAccumulate(int id, int l, int r) {
        for (int i = l; i < r && i < fileBytes.length; i+=byteSize) {
            //bytes en C2, necessari index positiu
            long b = fileBytes[i];
            for (int j = 1; j < byteSize; j++){
                b = b<<8;
                b |= i+j < fileBytes.length ? fileBytes[i+j]: 0;
            }
            if (b < 0) {
                b = b + (1L << (8*byteSize));
            }
            acumulators[id].put(b, acumulators[id].getOrDefault(b, 0L) + 1L);
        }
    }

    private void addConcurrent(Runnable r) {
        runnables.add(executor.submit(r));
    }

    /**
     * Espera a que tots el fils acabin
     */
    private void joinAll() {
        while (!runnables.isEmpty()) {
            Future<?> runnable = runnables.removeFirst();
            if (runnable == null) continue;
            try {
                runnable.get();

            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static byte[][] generateCanonicalCodes(int[] lengths, List<Integer> symbolList) {

        //primer ordenar segons la longitud del símbol, i després en ordre lexicografic
        symbolList.sort(Comparator
                .comparingInt((Integer a) -> lengths[a])
                .thenComparingInt(a -> a));

        byte[][] codes = new byte[Huffman.BITSIZE][];

        int code = 0, prevLen = 0;
        //assigna codis canònics a cada símbol en funció de l'ordre establert
        for (int sym : symbolList) {
            int len = lengths[sym];
            //si la longitud aumenta, es desplaça a l'esquerre el valor de code
            code <<= (len - prevLen);

            byte[] bits = new byte[len];
            //afegir padding
            for (int i = 0; i < len; i++) {
                bits[i] = (byte) ((code >> (len - 1 - i)) & 1);

            }
            //guardar el codi canònic
            codes[sym] = bits;
            code++;
            prevLen = len;
        }
        return codes;
    }


    /**
     * Retorna array de frequencies absolutes
     * @return Array on a[byte] = freq
     */
    public final Map<Long, Long> getFreqs() {
        return freqs;
    }


    /**
     * Retorna l'arrel de l'arbre
     * @return Node rel
     */
    public final Node getTree() {
        return treeRoot;
    }

    /**
     * Retorna la tabla de traducció:<br>
     * Byte -> Byte <br>
     * Original -> Codi
     *
     * @return tabla traducció
     */
    public final Map<Long, String> getTable() {
        return table;
    }

    /**
     * Retorna l'entropia del fitxer
     * @return
     */
    public double getEntropia(){
        return entropia;
    }
}
