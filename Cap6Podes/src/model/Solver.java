package model;

import controlador.Comunicar;
import controlador.Main;

import java.util.*;

public class Solver implements Runnable, Comunicar {
    private static final int INFINITY = Integer.MAX_VALUE;
    private volatile static boolean running = true;
    private volatile static boolean paused = false;
    private volatile static boolean stepMode = false;
    private int totalNodes;
    private Node root;
    private int[][] graf;

    private int visitats = 0;

    private Result resultat;

    public Solver (int id, int[][] matrix, boolean stepMode) throws InterruptedException {
        this.graf = matrix;
        this.resultat = new Result();
        Solver.stepMode = stepMode;

        if(stepMode) {
            resultat.costTotal = -1;
        }

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
        resultat.nodesTotals++;
        //BB basat en triar el node més prometedor
        while (!pq.isEmpty() && running) {
            Node node = step(pq);

            if (node != null) return node;

            if(stepMode){
                synchronized (this) {
                    while (paused) {
                        wait();  // esperar fins que es crida a resume()
                    }
                    obtenirSolucio(pq.peek());
                    Main.getInstance().getFinestra().step(0);
                    paused = true;
                }
            }

        }
        return null;
    }

    private Node step(PriorityQueue<Node> pq) {
        Node node = pq.poll();
        //hem visitat tots els nodes?

        if (Objects.requireNonNull(node).profund == totalNodes - 1) {
            int returnCost = node.matriuReduida[node.actual][0];
            node.cost += returnCost != INFINITY ? returnCost : 0;
            return node;
        }
        visitats++;

        for (int j = 0; j < totalNodes; j++) {
            if (node.matriuReduida[node.actual][j] != INFINITY) {
                pq.add(crearNodeFill(node, j));
                resultat.nodesTotals++;
            }else{
                resultat.nodesDescartats++;

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

        //matriu[nextVertex][0] = INFINITY;

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
        this.resultat.resultat = cami;

        //calcular cost
        int lastC = cami.getFirst();
        int totalCost = 0;
        for (int i = 1; i < cami.size(); i++) {
            int c = cami.get(i);
            totalCost += graf[lastC][c];
            lastC = c;
        }
        resultat.costTotal = totalCost;
        resultat.branquesExplorades = visitats;

        Main.getInstance().getDades().setResultat(resultat);
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


    @Override
    public void aturar(int id) {
        running = false;
    }

    @Override
    public void step(int id) {
        paused= false;
        synchronized (this) {
            notify();
        }
    }



    @Override
    public void comunicar(String msg) {
        System.err.println("msg: " + msg);
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
