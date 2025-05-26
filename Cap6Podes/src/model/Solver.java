package model;

import controlador.Comunicar;

import java.util.*;
import java.util.concurrent.Callable;

public class Solver implements Callable<Solver.Node>, Comunicar {
    private static int totalNodes;
    private static final int INFINITY = Integer.MAX_VALUE;
    private Node root;
    private volatile static  boolean running = true;
    private volatile static boolean paused = false;

    public Solver(int id, int n) throws InterruptedException {
        Random r = new Random();
        int [][] matriu = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                 if (i == j) {
                     matriu[i][j] = INFINITY;
                 }else{
                     matriu[i][j] = r.nextInt(100);
                 }
                ;
            }
        }
        root = solve(matriu);
        //primer s'ha de generar la matriu d'adjaçencia

    }
    public Solver (int id, int[][] matrix) throws InterruptedException {
         root = solve(matrix);
    }

    public Node getRoot() {
        return root;
    }



    @Override
    public Node call() throws Exception {
        return root;
    }


    public static class Node implements Comparable<Node> {
        int[][] reducedMatrix;
        int current;            //vertex del node actual
        int cost;               //cost acumulat fins el node actual
        Node parent;
        int depth;

        Node(int[][] reducedMatrix, int current, int cost, Node parent, int depth) {
            this.reducedMatrix = reducedMatrix;
            this.current = current;
            this.cost = cost;
            this.parent = parent;
            this.depth = depth;

        }


        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.cost, o.cost);
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
            if (node.depth == totalNodes - 1) {
                int returnCost = node.reducedMatrix[node.current][0];
                node.cost +=  returnCost == INFINITY ? 0: INFINITY;
                return node;
            }


            for (int j = 0; j < totalNodes; j++) {
                if (node.reducedMatrix[node.current][j] != INFINITY) {
                    pq.add(createChild(node, j));
                }
            }
        }
        return null;
    }



    static Node createChild(Node parent, int nextVertex) {
        int[][] matriu = deepCopy(parent.reducedMatrix);
        int current = parent.current;
        // Marcar fila y columna amb INFINTY per eliminar camins ja visitats.
        for (int k = 0; k < totalNodes; k++) {
            matriu[current][k] = INFINITY;
            matriu[k][nextVertex] = INFINITY;
        }
        // Impedir retorno al inicio prematuro
        matriu[nextVertex][0] = INFINITY;

        int addedCost = parent.reducedMatrix[current][nextVertex];
        int reductionCost = calcularCostReduccio(matriu);
        return new Node(matriu, nextVertex, parent.cost + addedCost + reductionCost, parent, parent.depth + 1);
    }

    static void imprimirCami(Node leaf) {
        LinkedList<Integer> path = new LinkedList<>();
        while (leaf != null) {
            path.addFirst(leaf.current);
            leaf = leaf.parent;
        }
        for (Integer integer : path) {
            System.out.println(integer);
        }

    }



    static int[][] deepCopy(int[][] m) {
        int[][] c = new int[totalNodes][totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            System.arraycopy(m[i], 0, c[i], 0, totalNodes);
        }
        return c;
    }


    static int calcularCostReduccio(int[][] mat) {
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

    private static int trobatMinColumna(int[][] mat, int i) {
        int min = INFINITY;

        for (int[] ints : mat) {
            if (ints[i] < min) min = ints[i];

        }
        return min;
    }

    private static int trobarMinFila(int[][] mat, int i) {
        int min = INFINITY;
        for (int j = 0; j < mat.length; j++) {
            if (mat[i][j] < min) min = mat[i][j];
        }
        return min;
    }



    public void interrompre() {
        running = false;
    }

    public void resume() {
        paused= false;
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
}
