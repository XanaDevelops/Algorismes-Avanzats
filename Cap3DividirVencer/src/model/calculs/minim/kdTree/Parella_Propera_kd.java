
package model.calculs.minim.kdTree;

import controlador.Main;
import model.Resultat;
import model.TipoPunt;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;
import model.calculs.minim.kdTree.KdArbre.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Parella_Propera_kd extends Calcul {

    private final KdArbre kdArbre;
    private final Node root;
//    private Node millorNode;
//    private double minDistancia;

    public Parella_Propera_kd() {
        super();

        int k = dades.getTp() == TipoPunt.p2D ? 2 : 3;
        this.kdArbre = new KdArbre((ArrayList<Punt>) dades.getPunts(), k);

        root = kdArbre.getRoot();
    }


    /**
     * Mètode que cerca el veïnat més proper del punt p a partir del node actual de l'arbre.
     * Actualitza les variables de classe (millorNode i minDistancia) si ha trobat millors resultats.
     *
     * @param actual
     * @param p
     */
    private Resultat searchNN(Node actual, Punt p, NNResult nn) {
        if (actual == null) {
            return new Resultat(punts.size(), p, nn.bestNode.punt, nn.bestDist, 0);
        }

        double d = p.distancia(actual.punt);

        if (d > 0 && d < nn.bestDist) {
            nn.bestDist = d;
            nn.bestNode = actual;
        }

        int axis = actual.profunditat % actual.k;
        Node seguent1, seguent2;

        // Decidir quina branca explorar primer segons la posició de p respecte al pla de divisió
        if (compareByAxis(p, actual.punt, axis) < 0) {
            seguent1 = actual.left;
            seguent2 = actual.right;
        } else {
            seguent1 = actual.right;
            seguent2 = actual.left;
        }

        Resultat r1 = searchNN(seguent1, p, nn);
        //S'ha d'explorar l'altra branca de l'arbre?
        double diff = axisDiff(p, actual.punt, axis);
        if (diff < r1.getDistancia()) { // Sí. l'hiperplà pot contenir punts més propers
            return searchNN(seguent2, p,nn);
        }
        return r1;
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

    /**
     * Compara dos punts en funció d'un eix de coordenades: 0 per X, 1 per Y i 2 per Z.
     * @param p1
     * @param p2
     * @param axis
     * @return
     */
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
        Resultat nn = searchNN(root, p, new NNResult(Double.MAX_VALUE, null));
        return new Resultat(punts.size(), p, nn.getP2() != null ? nn.getP2() : null, nn.getDistancia(), 0);
    }


    /**
     * Es calcula el veïnat més proper a cada punt de la llista per determinar la parella propera.
     * @param punts
     * @return
     */
    public Resultat calc2(ArrayList<Punt> punts) {
        long t = System.nanoTime();
        if (punts == null || punts.size() < 2) {
            return null;
        }

        List<Future<Resultat>> futures = new ArrayList<>();
        for (Punt p : punts) {
            futures.add(Main.instance.getExecutor().submit(() -> {
                Resultat r = NN(p);
                if (!p.equals(r.getP2())) {
                    return new Resultat(punts.size(), p, r.getP2(), r.getDistancia(), 0);
                }
                return null;
            }));
        }
        double bestDist = Double.MAX_VALUE;
        Punt bestP1 = null;
        Punt bestP2 = null;

        for (Future<Resultat> future : futures) {
            try {
                Resultat r = future.get();
                if (r != null && r.getDistancia() < bestDist) {
                    bestDist = r.getDistancia();
                    bestP1 = r.getP1();
                    bestP2 = r.getP2();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        t = System.nanoTime() - t;
        return new Resultat(punts.size(), bestP1, bestP2, bestDist, t);
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

            if (p.equals(r.getP2())) { // punts duplicats
                continue;
            }
            if (r.getDistancia() < bestDist) {
                bestDist = r.getDistancia();
                bestP1 = p;
                bestP2 = r.getP2();
            }
        }
        t = System.nanoTime() - t;
        return new Resultat(punts.size(), bestP1, bestP2, bestDist, t);
    }

    @Override
    public void run() {

        Resultat res ;
        if (Main.instance.isModeConcurrentOn()){
            res = calc2((ArrayList<Punt>) punts);
        }else{
            res = calc((ArrayList<Punt>) punts);
        }


        dades.afegeixResultat(punts.size(), res.getP1(), res.getP2(), res.getDistancia(), res.getTempsNano(), TipusCalcul.KD_MIN);
    }
    private static class NNResult {
        double bestDist;
        Node bestNode;

        public NNResult(double bestDist, Node bestNode) {
            this.bestDist = bestDist;
            this.bestNode = bestNode;
        }
    }

}
