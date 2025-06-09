package model;

import controlador.Comunicar;
import controlador.Main;

import java.util.*;

public class Solver implements Runnable, Comunicar {
    private static final int INFINITY = Integer.MAX_VALUE;
    private volatile static boolean running = true;
    private volatile static boolean paused = false;
    private int totalNodes;
    private Node root;
    private int[][] graf;
    private boolean modeAll = true;
    public Solver(int id, int n) throws InterruptedException {
        Random r = new Random();
        int[][] matriu = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matriu[i][j] = INFINITY;
                } else {
                    matriu[i][j] = r.nextInt(100);
                }

            }
        }
        this.graf = matriu;
        totalNodes = graf.length;
    }

    public Solver(int id, int[][] matrix) throws InterruptedException {
        this.graf = matrix;
        totalNodes = graf.length;
    }

    private static int trobarMinFila(int[][] mat, int i) {
        int min = INFINITY;
        for (int j = 0; j < mat.length; j++) {
            if (mat[i][j] < min) min = mat[i][j];
        }
        return min;
    }

    @Override
    public void run() {
        try {
            root = solve(graf);

            obtenirSolucio(root);
            System.out.println(root.cost);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    Node solve(int[][] costMatrix) throws InterruptedException {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        int[][] rootMat = deepCopy(costMatrix);
        int rootCost = calcularCostReduccio(rootMat);
        //començar la ruta
        Node root = new Node(rootMat, 0, rootCost, null, 0);
        pq.add(root);

        //BB basat en triar el node més prometedor
        while (!pq.isEmpty() && running) {
            synchronized (this) {
                while (paused) {
                    wait();  // esperar fins que es crida a resume()
                }
            }
            Node node = pq.poll();
            //hem visitat tots els nodes?

            if (Objects.requireNonNull(node).profund == totalNodes - 1) {
                int returnCost = node.matriuReduida[node.actual][0];
                node.cost += returnCost != INFINITY ? returnCost : 0;
                return node;
            }

            for (int j = 0; j < totalNodes; j++) {
                if (node.matriuReduida[node.actual][j] != INFINITY) {
                    pq.add(crearNodeFill(node, j));
                }
            }
        }
        return null;
    }


    private Node crearNodeFill(Node parent, int nextVertex) {
        int[][] matriu = deepCopy(parent.matriuReduida);
        int current = parent.actual;
        // Marcar fila y columna amb INFINTY per eliminar camins ja visitats.
        for (int k = 0; k < totalNodes; k++) {
            matriu[current][k] = INFINITY;
            matriu[k][nextVertex] = INFINITY;
        }

        int costAfegit = parent.matriuReduida[current][nextVertex];
        int costReduccio = calcularCostReduccio(matriu);
        return new Node(matriu, nextVertex, parent.cost + costAfegit + costReduccio, parent, parent.profund + 1);
    }

    private void obtenirSolucio(Node leaf) {
        LinkedList<Integer> cami = new LinkedList<>();
        while (leaf != null) {
            cami.addFirst(leaf.actual);
            leaf = leaf.pare;
        }

        Main.getInstance().getDades().guardarSolucio(cami);
    }

    private int[][] deepCopy(int[][] m) {
        int[][] c = new int[m.length][]; // Copia solo referencia a filas
        for (int i = 0; i < m.length; i++) {
            c[i] = Arrays.copyOf(m[i], m[i].length); // Copia solo contenido de cada fila
        }
        return c;
    }

    private int calcularCostReduccio(int[][] mat) {
        int cost = 0;
        // reduir files
        for (int i = 0; i < mat.length; i++) {
            int minFila = trobarMinFila(mat, i);
            if (minFila != INFINITY) {
                cost += minFila;
                for (int j = 0; j < mat.length; j++) {
                    if (mat[i][j] != INFINITY) {
                        mat[i][j] -= minFila;
                    }

                }
            }
        }

        //reduir columnes
        for (int i = 0; i < mat.length; i++) {
            int minColumna = trobatMinColumna(mat, i);
            if (minColumna != INFINITY) {
                cost += minColumna;
                for (int j = 0; j < mat.length; j++) {
                    if (mat[j][i] != INFINITY) {
                        mat[j][i] -= minColumna;

                    }

                }
            }
        }
        return cost;
    }

    private int trobatMinColumna(int[][] mat, int i) {
        int min = INFINITY;

        for (int[] ints : mat) {
            if (ints[i] < min) min = ints[i];
        }
        return min;
    }

    public void interrompre() {
        running = false;
    }

    public void resume() {
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    public void pause() {
        paused = true;

    }

    @Override
    public void comunicar(String msg) {

    }

    public static class Node implements Comparable<Node> {
        int[][] matriuReduida;
        int actual;            //vertex del node actual
        int cost;               //cost acumulat fins el node actual
        Node pare;
        int profund;

        Node(int[][] reducedMatrix, int actual, int cost, Node pare, int profund) {
            this.matriuReduida = reducedMatrix;
            this.actual = actual;
            this.cost = cost;
            this.pare = pare;
            this.profund = profund;
        }


        public int getActual() {
            return actual;
        }

        public Node getPare() {
            return pare;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.cost, o.cost);
        }
    }
}
