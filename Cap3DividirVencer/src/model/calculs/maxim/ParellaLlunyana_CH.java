package model.calculs.maxim;

import model.Resultat;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basat en l'algorisme de chan
 */
public class ParellaLlunyana_CH extends Calcul {

    private List<Punt> qhull;

    public ParellaLlunyana_CH(){
        super();
        qhull = new ArrayList<>();
        dades.setTipusCalcul(TipusCalcul.CH_MAX);
    }
    public ParellaLlunyana_CH(List<Punt> punts) {
        super();
        this.punts.clear();
        this.punts.addAll(punts);
        qhull = new ArrayList<>();

    }
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        long startTime = System.nanoTime();
        Resultat r = calcular();
        long elapsedTime = System.nanoTime() - startTime;
        System.err.println(r);
        dades.afegeixResultat(punts.size(), r.getP1(), r.getP2(), r.getDistancia(), elapsedTime, TipusCalcul.CH_MAX);
    }

    private Resultat calcular() {
        punts.sort(Comparator.comparingInt(Punt::getY));
        final Punt a = punts.getFirst();

        punts.sort((o1, o2) -> {
            int order = cross(a, o1, o2);
            if (order == 0) {
                int distA = (o1.getX() - a.getX()) * (o1.getX() - a.getX()) + (o1.getY() - a.getY()) * (o1.getY() - a.getY());
                int distB = (o2.getX() - a.getX()) * (o2.getX() - a.getX()) + (o2.getY() - a.getY()) * (o2.getY() - a.getY());
                return Integer.compare(distA, distB);
            }

            return order > 0 ? -1 : 1;
        });

        Stack<Punt> stack = new Stack<>();
        stack.push(punts.get(0));
        stack.push(punts.get(1));

        for (int i = 2; i < punts.size(); i++) {
            while (stack.size() >= 2) {
                Punt second = stack.pop();
                Punt first = stack.peek();
                if (cross(first, second, punts.get(i)) > 0) {
                    stack.push(second);
                    break;
                }
            }
            stack.push(punts.get(i));
        }

        return ParellaLlunyana_fb.calc(new ArrayList<>(stack));
    }

    // Cross product of OA and OB vectors, returns positive for counter-clockwise turn
    public static int cross(Punt O, Punt A, Punt B) {
        return (A.getX() - O.getX()) * (B.getY() - O.getY()) - (A.getY() - O.getY()) * (B.getX() - O.getX());
    }


}
