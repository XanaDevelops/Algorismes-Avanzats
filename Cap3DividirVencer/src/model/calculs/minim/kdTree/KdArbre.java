package model.calculs.minim.kdTree;

import model.punts.Punt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe representativa d'un arbre kd que permet un emmagatzematge eficient d'una llista de punts, facilitant
 * la recerca de distàncies mínimes entre parelles de punts.
 */
public class KdArbre {

    /**
     * Nombre de dimensions dels punts de l'arbre
     */
    private int k;
    /**
     * Node arrel de l'arbre
     */
    private Node root = null;

    /**
     * Constructor de classe. Genera l'arbre kd a partir de la llista de punts que rep.
     * @param punts llista de punts
     * @param k nombre de dimensions
     */
    public KdArbre(List<Punt> punts, int k) {
        this.k = k;
        root = crearNode(punts, k, 0);
    }

    public Node getRoot() {
        return root;
    }

    /**
     * Comparator per a la coordenada X del punt
     */
    private static final Comparator<Punt> comparatorX= new Comparator<Punt>() {
        @Override
        public int compare(Punt o1, Punt o2) {
            if (o1.getX() > o2.getX()) {
                return 1;
            } else if (o1.getX() < o2.getX()) {
                return -1;
            }
            return 0;
        }
    };
    /**
     * Comparator per a la coordenada Y del punt
     */
    private static final Comparator<Punt> comparatorY= new Comparator<Punt>() {
        @Override
        public int compare(Punt o1, Punt o2) {
            if (o1.getY() > o2.getY()) {
                return 1;
            } else if (o1.getY() < o2.getY()) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Comparator per a la coordenada Z del punt
     */
    private static final Comparator<Punt> comparatorZ= new Comparator<Punt>() {
        @Override
        public int compare(Punt o1, Punt o2) {
            if (o1.getZ() > o2.getZ()) {
                return 1;
            } else if (o1.getZ() < o2.getZ()) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Mètode recursiu que genera l'arbre kd.
     * En cada nivell es tria una coordenada a partir del qual s'insereixen els punts als subarbres corresponents
     *
     * @param punts llista de punts
     * @param k dimensió dels punts
     * @param profunditat profunditat actual
     * @return node arrel
     */
    private Node crearNode(List<Punt> punts, int k, int profunditat) {
        if (punts.isEmpty()) {
            return null;
        }
        //determinar l'eix de comparació
        int axis = profunditat % k;

        //ordenar la llista de punts segons l'eix de comparació d'aquest nivell de profunditat
        switch (axis) {
            case 0:
                punts.sort(comparatorX);
                break;
            case 1:
                punts.sort(comparatorY);
                break;
            case 2:
                punts.sort(comparatorZ);
                break;
        }

        int mitat = punts.size() / 2;
        //Node central de la llista de punts
        Node node = new Node(punts.get(mitat), k, profunditat);

        //llista de punts coordenada inferior a la del node central
        List<Punt> puntsInferiors = new ArrayList<>();

        //llista de punts coordenada superior a la del node central
        List<Punt> puntsSuperiors = new ArrayList<>();


        // Omplir les llistes de punts superiors i inferiors
        for (int i = 0; i < punts.size(); i++) {
            if (i == mitat) { //no s'afegeix el node central als subarbres
                continue;
            }
            Punt puntActual = punts.get(i);
            if (Node.compareTo(profunditat, k, puntActual, node.punt) <= 0) {
                puntsInferiors.add(puntActual);
            } else {
                puntsSuperiors.add(puntActual);
            }

        }

        //Construcció recursiva del subarbre esquerre amb la llista de punts inferiors
        if (mitat - 1 >= 0 && !puntsInferiors.isEmpty()) {
            node.left = crearNode(puntsInferiors, k, profunditat + 1);
            if (node.left != null) {
                node.left.pare = node; //Establir el node central com a pare del node esquerre
            }
        }
        //Construcció recursiva del subarbre dreta amb la llista de punts superiors

        if (mitat <= punts.size() - 1 && !puntsInferiors.isEmpty()) {
            node.right = crearNode(puntsSuperiors, k, profunditat + 1);
          if (node.right != null) {
              node.right.pare = node;  //Establir el node central com a pare del node dreta
          }
        }

        return node;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        recorrerToString(root, sb, 0);
        return sb.toString();
    }

    /**
     * Mètode que recorre l'arbre recursivament per imprimir-lo.
     * @param node node actual de l'arbre
     * @param sb la cadena actual
     * @param nivell nivell de profunditat de l'arbre
     */
    private void recorrerToString(Node node, StringBuilder sb, int nivell) {
        if (node == null) {
            return;
        }
        // Indentació segons el nivell del node
        sb.append("  ".repeat(nivell))
                .append(node.punt)
                .append(" (profunditat: ")
                .append(node.profunditat)
                .append(")\n");
        recorrerToString(node.left, sb, nivell + 1);
        recorrerToString(node.right, sb, nivell + 1);
    }


    /**
     * Classe del node de l'arbre-kd.
     */
    public static class Node {

        protected Node pare = null;
        protected Node left;
        protected Node right;
        protected Punt punt;
        protected int k;
        protected int profunditat;

        public Node(Punt punt, int k, int profunditat) {
            this.punt = punt;
            this.k = k;
            this.profunditat = profunditat;
        }

        public static int compareTo(int profunditat, int k, Punt p1, Punt p2) {
            int axis = profunditat % k;
            int returnValor = 0;
            returnValor = switch (axis) {
                case 0 ->  comparatorX.compare(p1, p2);
                case 1 -> comparatorY.compare(p1, p2);
                case 2 -> comparatorZ.compare(p1, p2);
                default -> returnValor;
            };
            return returnValor;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "pare=" + pare +
                    ", left=" + left +
                    ", right=" + right +
                    ", punt=" + punt +
                    ", k=" + k +
                    ", profunditat=" + profunditat +
                    '}';
        }
    }


}
