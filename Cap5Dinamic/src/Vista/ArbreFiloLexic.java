package Vista;

import Model.Dades;
import Model.Idioma;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import java.util.List;

public class ArbreFiloLexic extends JPanel {
    private final Dades dades;
    private final int width;
    private final int height;
    private Node root;
    public ArbreFiloLexic(Dades dades) {
        this.dades = dades;
        //s'han de passar per parametre o es poden obtenir de la classe Dades
        this.width = 300;
        this.height = 300;
        setPreferredSize(new Dimension(width, height));
        this.root = construirArbre();
        asignarPosFulles(root);
    }

    private void asignarPosFulles(Node raiz) {

        List<Node> leaves = new ArrayList<>();
        obtenirFulles(raiz, leaves);

        int n = leaves.size();
        int marginX = (int)(width * 0.05);
        int marginY = (int)(height * 0.05);


        int availableWidth = width - 2*marginX;
        int offsetX = n > 1 ? availableWidth / (n - 1) : 0;

        int baseY = height - marginY;
        for (int i = 0; i < n; i++) {
            Node leaf = leaves.get(i);
            leaf.pos = new Point(marginX + i * offsetX, baseY);
        }
        //cercar l'altura de l'arbre
        double maxDist = findMaxDistance(raiz);

        assignarPosInterns(raiz, maxDist, marginY, baseY);
    }

    private void assignarPosInterns(Node node, double maxDist, int topMargin, int baseY) {
        if (node == null || node.isLeaf()) return;

        assignarPosInterns(node.left,  maxDist, topMargin, baseY);
        assignarPosInterns(node.right, maxDist, topMargin, baseY);

        int x = (node.left.pos.x + node.right.pos.x) / 2;

        int availableHeight = baseY - topMargin;
        int y = baseY - (int)((node.profunditat / maxDist) * availableHeight);

        node.pos = new Point(x, y);
    }

    private double findMaxDistance(Node node) {
        if (node == null || node.isLeaf()) return 0;
        double l = findMaxDistance(node.left);
        double r = findMaxDistance(node.right);
        return Math.max(node.profunditat, Math.max(l, r));
    }


    private void obtenirFulles(Node node, List<Node> hojas) {
        if (node == null) return;
        if (node.isLeaf()) {
            hojas.add(node);
        } else {
            obtenirFulles(node.left, hojas);
            obtenirFulles(node.right, hojas);
        }
    }
    private Node construirArbre() {
        double[][] distancies = dades.getDistancies();
        int n = distancies.length;
        Llista clusters = new Llista(distancies);

        for (int i = 0; i < n; i++) {
            clusters.add(new Node(Idioma.values()[i]));
        }

        while (clusters.size() > 1) {
            double min = Double.MAX_VALUE;
            int leftIndex = -1, rightIndex = -1;
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = 0; j < i; j++) {
                    double d = clusters.getDistance(clusters.get(i), clusters.get(j));
                    if (d < min) {
                        min = d;
                        leftIndex = i;
                        rightIndex = j;
                    }
                }
            }


            Node A = clusters.get(leftIndex);
            Node B = clusters.get(rightIndex);

            Node merged = new Node(A, B);
            //eliminar en ordre
            if (leftIndex > rightIndex) {
                clusters.remove(clusters.get(leftIndex));
                clusters.remove(clusters.get(rightIndex));
            } else {
                clusters.remove(clusters.get(rightIndex));
                clusters.remove(clusters.get(leftIndex));
            }
            clusters.add(merged);
        }
        return clusters.get(0);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTreeRec(g, root);

    }


    private void drawTreeRec(Graphics g, Node node) {
        if (node == null) return;

        if (!node.isLeaf()) {
            int parentY = node.pos.y;
            int leftX = node.left.pos.x;
            int leftY = node.left.pos.y;
            int rightX = node.right.pos.x;
            int rightY = node.right.pos.y;

            //linies verticals
            g.drawLine(leftX, leftY, leftX, parentY);
            g.drawLine(rightX, rightY, rightX, parentY);
            //linea horizontal
            g.drawLine(leftX, parentY, rightX, parentY);
            //petit node al mig de la linea horizontal
            g.fillOval(node.pos.x - 4, node.pos.y - 4, 8, 8);

            drawTreeRec(g, node.left);
            drawTreeRec(g, node.right);
        } else {
            String txt = node.idioma.name();
            int tx = node.pos.x - g.getFontMetrics().stringWidth(txt) / 2;
            int ty = node.pos.y + 15;
            g.drawString(txt, tx, ty);
        }
    }


    public static class Node {
        Node left, right;
        Idioma idioma;
        Point pos;
        int profunditat;
        //Node fulla
        public Node(Idioma idioma) {
            this.idioma = idioma;
            this.left = null;
            this.right = null;
            this.profunditat = 0;
            this.pos = new Point(0, 0);
        }

        //Node intern
        public Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.pos = new Point(0, 0);
            this.profunditat = 1+ Math.max(left.profunditat, right.profunditat);
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public List<Idioma> getLeaves() {
            List<Idioma> leaves = new ArrayList<>();
            if (isLeaf()) {
                leaves.add(this.idioma);
            } else {
                leaves.addAll(left.getLeaves());
                leaves.addAll(right.getLeaves());
            }
            return leaves;
        }
    }

    private static class Llista {
        List<Node> llista;
        double[][] distancies;
        double[][] clusterDistances;

        public Llista(double[][] distancies) {
            llista = new ArrayList<>();
            this.distancies = distancies;

        }

        public void add(Node n) {
            llista.add(n);
        }

        public int size() {
            return llista.size();
        }

        public Node get(int i) {
            return llista.get(i);
        }

        public void remove(Node n) {
            llista.remove(n);
        }


        //calcula la distancia entre dos nodes
        public double getDistance(Node n1, Node n2) {
            List<Idioma> leaves1 = n1.getLeaves();
            List<Idioma> leaves2 = n2.getLeaves();
            double sum = 0.0;
            for (Idioma a : leaves1) {
                for (Idioma b : leaves2) {
                    int idxA = a.ordinal();
                    int idxB = b.ordinal();
                    double d = (idxA > idxB) ? distancies[idxA][idxB] : distancies[idxB][idxA];
                    sum += d;
                }
            }
            int count = leaves1.size() * leaves2.size();
            return count > 0 ? sum / count : Double.MAX_VALUE;
        }


    }
}
