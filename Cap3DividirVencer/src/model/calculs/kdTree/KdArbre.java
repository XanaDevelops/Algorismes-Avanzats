package model.calculs.kdTree;

import model.punts.Punt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class KdArbre {

    private int k;
    private Node root = null;

    public KdArbre(List<Punt> punts, int k) {
        this.k = k;
        root = crearNode(punts, k, 0);
    }

    public Node getRoot() {
        return root;
    }

    private static Comparator<Punt> comparatorX= new Comparator<Punt>() {
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

    private static Comparator<Punt> comparatorY= new Comparator<Punt>() {
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

    private static Comparator<Punt> comparatorZ= new Comparator<Punt>() {
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

    private Node crearNode(List<Punt> punts, int k, int profunditat) {
        if (punts.isEmpty()) {
            return null;
        }
        int axis = profunditat % k;
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

        Node node = null;
        List<Punt> puntsInferiors = new ArrayList<>();
        List<Punt> puntsSuperiors = new ArrayList<>();


        int mitat = punts.size() / 2;
        node = new Node(punts.get(mitat), k, profunditat);

        //afegir els punts inferiors i superiors resprecte de node a les llistes
        //corresponents
        for (int i = 0; i < punts.size(); i++) {
            if (i == mitat) {
                continue;
            }
            Punt puntActual = punts.get(i);
            if (Node.compareTo(profunditat, k, puntActual, node.punt) <= 0) {
                puntsInferiors.add(puntActual);
            } else {
                puntsSuperiors.add(puntActual);
            }

        }
        if (mitat - 1 >= 0 && !puntsInferiors.isEmpty()) {
            node.left = crearNode(puntsInferiors, k, profunditat + 1);
            if (node.left != null) {
                node.left.pare = node;
            }

        }
        if (mitat <= punts.size() - 1 && !puntsInferiors.isEmpty()) {
            node.right = crearNode(puntsSuperiors, k, profunditat + 1);
          if ( node.right != null) {
              node.right.pare = node;
          }
        }


        return node;

    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        recorrerToString(root, sb, 0);
//        return sb.toString();
        return getString(root, "", true);
    }

    private static String getString(Node node, String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();

        if (node.pare != null) {
            String side = "left";
            if (node.pare.right != null && node.punt.equals(node.pare.right.punt))
                side = "right";
            builder.append(prefix + (isTail ? "└── " : "├── ") + "[" + side + "] " + "depth=" + node.profunditat + " id="
                    + node.punt + "\n");
        } else {
            builder.append(prefix + (isTail ? "└── " : "├── ") + "depth=" + node.profunditat + " id=" + node.punt + "\n");
        }
        List<Node> children = null;
        if (node.left != null || node.right != null) {
            children = new ArrayList<Node>(2);
            if (node.left != null)
                children.add(node.left);
            if (node.right != null)
                children.add(node.right);
        }
        if (children != null) {
            for (int i = 0; i < children.size() - 1; i++) {
                builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
            }
            if (children.size() >= 1) {
                builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "),
                        true));
            }
        }

        return builder.toString();
    }


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


    public static class Node {

        Node pare = null;
        Node left;
        Node right;
         Punt punt;
        int k;
        int profunditat;

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
//                    "pare=" + pare +
                    ", left=" + left +
                    ", right=" + right +
                    ", punt=" + punt +
                    ", k=" + k +
                    ", profunditat=" + profunditat +
                    '}';
        }
    }


}
