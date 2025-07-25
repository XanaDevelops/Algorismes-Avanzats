package model.Huffman;

import model.cues.FibonacciHeap;
import model.cues.RankPairingHeap;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Huffman implements Runnable {
    public enum WordSize{
        BIT8(1, "8 bits"),
        BIT16(2, "16 bits"),
        BIT32(4, "32 bits"),
        BIT64(8, "64 bits");
        private int size;
        private String string;
        WordSize(int x, String s){
            this.size = x;
            this.string = s;
        }

        public int getBitSize() {return size;}
        @Override
        public String toString() {return string;}
    }
    public enum TipusCua {
        BIN_HEAP(PriorityQueue.class, "Binary Heap"),
        FIB_HEAP(FibonacciHeap.class, "Fibonacci Heap"),
        RANK_PAIRING_HEAP(RankPairingHeap.class, "Rank Pairing Heap"),;

        private Class<? extends AbstractQueue> cua;
        private String name;
        TipusCua(Class<? extends AbstractQueue> cua, String s) {
            this.cua = cua;
            this.name = s;
        }

        public Class<? extends AbstractQueue> getCua() {
            return this.cua;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    public static class Node implements Comparable<Node> {
        public long val;
        public long byteVal;
        private Node left, right;
        boolean isLeaf;

        public Node(long val, long byteVal, boolean isLeaf) {
            this.val = val;
            this.byteVal = byteVal;
            this.isLeaf = isLeaf;
        }

        public boolean isLeaf() {
            return isLeaf;
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

        public long getVal() {
            return val;
        }

        public void setVal(long val) {
            this.val = val;
        }

        public void setByteVal(long byteVal) {
            this.byteVal = byteVal;
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

    private final int byteSize;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS);
    private final List<Future<?>> runnables = Collections.synchronizedList(new ArrayList<>());

    private final Map<Long, Long>[] acumulators;
    private byte[] fileBytes;
    private final Map<Long, Long> freqs;

    public TipusCua getTipusCua() {
        return tipusCua;
    }

    private final TipusCua tipusCua;

    private Node treeRoot;



    private double entropia = -1;
    private long total = 0;

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
        this.acumulators = Stream.generate(() -> new TreeMap<Long, Long>(Long::compareUnsigned)).limit(N_THREADS).toArray(TreeMap[]::new);

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

            if (node.left != null) addConcurrent(() -> createTable(node.left, val + "0", fdeep+1));
            if (node.right != null) addConcurrent(() -> createTable(node.right, val + "1", fdeep+1));


        }
    }

    @SuppressWarnings("unchecked")
    private void genTree() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Queue<Node> pq = tipusCua.getCua().getConstructor().newInstance();
        for (Map.Entry<Long, Long> e : freqs.entrySet()) {

            pq.add(new Node(e.getValue(), e.getKey(), true));
        }
        while (pq.size() > 1) {
            Node first = pq.poll();
            Node second = pq.poll();
            Node parent = new Node(first.val + second.val, -1L, false);
            parent.left = first;
            parent.right = second;
            pq.add(parent);
        }

        treeRoot = pq.poll();
    }

    private void calculateFreqs() {
        final int split = Math.max(((fileBytes.length/byteSize)/N_THREADS)*byteSize, 256);
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
        total = 0;
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
            long b = (long) fileBytes[i] & (0xFFL);
            for (int j = 1; j < byteSize; j++){
                b = b<<8;
                b |= i+j < fileBytes.length ? (long) fileBytes[i+j] & (0xFFL): 0;
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

    public static Map<Long, byte[]> generateCanonicalCodes(Map<Long, Integer> lengths, List<Long> symbolList) {

        //primer ordenar segons la longitud del símbol, i després en ordre lexicografic
        symbolList.sort(Comparator
                .comparingLong((Long a) -> lengths.get(a))
                .thenComparing(Function.identity(), Long::compareUnsigned));

        //byte[][] codes = new byte[Huffman.BITSIZE][];
        Map<Long, byte[]> codes = new HashMap<>();
        int code = 0, prevLen = 0;
        //assigna codis canònics a cada símbol en funció de l'ordre establert
        for (long sym : symbolList) {
            int len = lengths.get(sym);
            //si la longitud aumenta, es desplaça a l'esquerre el valor de code
            code <<= (len - prevLen);

            byte[] bits = new byte[len];
            //afegir padding
            for (int i = 0; i < len; i++) {
                bits[i] = (byte) ((code >> (len - 1 - i)) & 1);

            }
            //guardar el codi canònic
            codes.put(sym, bits);
            code++;
            prevLen = len;
        }
        return codes;
    }


    /**
     * Retorna array de frequencies absolutes
     * @return Array on a[byte] = freq
     */
    public final Map<Long, Long> getAbsFreqs() {
        return freqs;
    }

    public final Map<Long, Double> getRelFreqs(){
        Map<Long, Double> freqs = new HashMap<>();
        for(Map.Entry<Long, Long> e : this.freqs.entrySet()) {
            freqs.put(e.getKey(), e.getValue()/(double)total);
        }
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

    /**
     * retorna el tamany en bytes de la paraula compresa
     * Exemple:
     * 1 -> 8b -> 1B
     * 4 -> 32b -> 4B
     * @return
     */
    public int getByteSize(){
        return byteSize;
    }
}
