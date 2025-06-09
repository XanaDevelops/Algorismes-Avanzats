package model;

import controlador.Comunicar;
import controlador.Main;

import java.util.*;

public class Solver implements Runnable, Comunicar {
    private static int totalNodes;
    private static final int INFINITY = Integer.MAX_VALUE;
    private Node root;
    private volatile static  boolean running = true;
    private volatile static boolean paused = false;
    private volatile static boolean stepMode = false;
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

    public Node getRoot() {
        return root;
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

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
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
        if (Objects.requireNonNull(node).depth == totalNodes - 1) {
            int returnCost = node.reducedMatrix[node.current][0];
            node.cost +=  returnCost == INFINITY ? 0: INFINITY;
            return node;
        }

        visitats++;

        for (int j = 0; j < totalNodes; j++) {
            if (node.reducedMatrix[node.current][j] != INFINITY) {
                pq.add(createChild(node, j));
                resultat.nodesTotals++;
            }else{
                resultat.nodesDescartats++;
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
        matriu[nextVertex][0] = INFINITY;

        int addedCost = parent.reducedMatrix[current][nextVertex];
        int reductionCost = calcularCostReduccio(matriu);
        return new Node(matriu, nextVertex, parent.cost + addedCost + reductionCost, parent, parent.depth + 1);
    }

    private void obtenirSolucio(Node leaf) {
        LinkedList<Integer> path = new LinkedList<>();
        while (leaf != null) {
            path.addFirst(leaf.current);

            leaf = leaf.parent;
        }
        this.resultat.resultat = path;

        //calcular cost
        int lastC = path.getFirst();
        int totalCost = 0;
        for (int i = 1; i < path.size(); i++) {
            int c = path.get(i);
            totalCost += graf[lastC][c];
            lastC = c;
        }
        resultat.costTotal = totalCost;
        resultat.branquesExplorades = visitats;
        Main.getInstance().getDades().setResultat(resultat);

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
}
