
package model.calculs.kdTree;

import model.Dades;
import model.Dades.Resultat;
import model.TipoPunt;
import model.calculs.Calcul;
import model.punts.Punt;
import model.calculs.kdTree.KdArbre.Node;

import java.util.ArrayList;

public class Parella_Propera_kd extends Calcul {

    private KdArbre kdArbre;
    private Node root;
    private Node bestNode;
    private double bestDistance;

    public Parella_Propera_kd(Dades dades) {
        super(dades);

        int k = dades.getTp() == TipoPunt.p2D ? 2 : 3;
        this.kdArbre = new KdArbre(dades.getPunts(), k);

        root = kdArbre.getRoot();
    }


    private void searchNN(Node current, Punt p) {
        if (current == null) {
            return;
        }

        double d = p.distancia(current.punt);

        if (d > 0 && d < bestDistance) {
            bestDistance = d;
            bestNode = current;
        }

        int axis = current.profunditat % current.k;
        Node next, other;

        if (compareByAxis(p, current.punt, axis) < 0) {
            next = current.left;
            other = current.right;
        } else {
            next = current.right;
            other = current.left;
        }

        searchNN(next, p);

        double diff = axisDiff(p, current.punt, axis);
        if (diff < bestDistance) {
            searchNN(other, p);
        }
    }


    private int compareByAxis(Punt p1, Punt p2, int axis) {
        if (axis == 0) {
            return Double.compare(p1.getX(), p2.getX());
        } else if (axis == 1) {
            return Double.compare(p1.getY(), p2.getY());
        } else {
            return Double.compare(p1.getZ(), p2.getZ());
        }
    }


    private double axisDiff(Punt p1, Punt p2, int axis) {
        if (axis == 0) {
            return Math.abs(p1.getX() - p2.getX());
        } else if (axis == 1) {
            return Math.abs(p1.getY() - p2.getY());
        } else {
            return Math.abs(p1.getZ() - p2.getZ());
        }
    }


    private Resultat NN(Punt p) {

        bestDistance = Double.MAX_VALUE;
        bestNode = null;
        searchNN(root, p);

        return new Resultat(p, bestNode != null ? bestNode.punt : null, bestDistance, 0, "min");
    }


    public Resultat calc(ArrayList<Punt> punts) {
        long t = System.nanoTime();
        if (punts == null || punts.size() < 2) {
            return null;
        }

        double bestDist = Double.MAX_VALUE;
        Punt bestP1 = null;
        Punt bestP2 = null;

        for (Punt p : punts) {
            Resultat r = NN(p);

            if (p.equals(r.getP2())) {
                continue;
            }
            if (r.getDistancia() < bestDist) {
                bestDist = r.getDistancia();
                bestP1 = p;
                bestP2 = r.getP2();
            }
        }
        t = System.nanoTime() - t;
        return new Resultat(bestP1, bestP2, bestDist, t, "min");
    }

    @Override
    public void run() {

        Resultat res = calc(new ArrayList<>(punts));

        dades.afegeixKD(punts.size(), res.getP1(), res.getP2(), res.getDistancia(), res.getTempsNano(), "min");
    }
}
