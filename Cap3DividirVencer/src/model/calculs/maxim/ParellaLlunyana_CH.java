package model.calculs.maxim;

import model.Resultat;
import model.TipoPunt;
import model.TipusCalcul;
import model.calculs.Calcul;
import model.punts.Punt;
import model.punts.Punt2D;

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
        Resultat r;
        if (dades.getTp() == TipoPunt.p2D){
            r = calcular(); //Utilitza Graham
        }else{
            ParellaLlunyana_QH qh = new ParellaLlunyana_QH(); //Utilitza QuickHull3D
            r = qh.calcular();
        }
        long elapsedTime = System.nanoTime() - startTime;
        System.err.println(r);
        dades.afegeixResultat(punts.size(), r.getP1(), r.getP2(), r.getDistancia(), elapsedTime, TipusCalcul.CH_MAX);
    }

    private Resultat calcular() {
        punts.sort(Comparator.comparingInt(Punt::getY));
        final Punt a = punts.getFirst();

        punts.sort(new Comparator<Punt>() {
            @Override
            public int compare(Punt o1, Punt o2) {
                if (o1.equals(o2)){
                    return 0;
                }
                double ta = Math.atan2(o1.getY() - a.getY(), o1.getX() - a.getX());
                double tb = Math.atan2(o2.getY() - a.getY(), o2.getX() - a.getX());

                if (ta < tb){
                    return -1;
                }
                if (ta > tb){
                    return 1;
                }
                //formen una linea
                return 0;
            }
        });

        Stack<Punt> stack = new Stack<>();
        stack.push(punts.get(0));
        stack.push(punts.get(1));

        for (int i = 2; i < punts.size(); i++){
            Punt aux = punts.get(i);
            Punt m = stack.pop();
            Punt t = stack.peek();

            int auxT = getTurn(t, m, aux);
            if (auxT == 1){
                stack.push(m);
                stack.push(aux);
            }
            if (auxT == -1){
                i--;
            }
            if (auxT == 0){
                stack.push(aux);
            }
        }

        return ParellaLlunyana_fb.calc(new ArrayList<>(stack));
    }


    private int getTurn(Punt a, Punt b, Punt c) {

        // use longs to guard against int-over/underflow
        long crossProduct = (((long)b.getX() - a.getX()) * ((long)c.getY() - a.getY())) -
                (((long)b.getY() - a.getY()) * ((long)c.getX() - a.getX()));

        if(crossProduct > 0) {
            return 1;
        }
        else if(crossProduct < 0) {
            return -1;
        }
        else {
            return 0;
        }
    }




}
