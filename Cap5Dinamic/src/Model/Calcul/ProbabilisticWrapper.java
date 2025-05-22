package Model.Calcul;

import Model.Idioma;
import controlador.Main;

import java.util.*;
import java.util.concurrent.Executors;

public class ProbabilisticWrapper extends CalculIdiomes{

    public static final int MONTECARLO_ITER = 5;

    private final double percent;

    public ProbabilisticWrapper(Idioma A, Idioma B, int id, double percent) {
        super(A, B, id);
        assert (percent >= 0 && percent <= 1);
        this.percent = percent;
    }

    @Override
    protected List<String> getParaules(Idioma x){
        List<String> orig = super.getParaules(x);
        List<String> paraulesRandom = new ArrayList<String>();
        Set<Integer> indexos = new TreeSet<>();
        Random r = new Random();
        while(indexos.size() < orig.size()*percent)
            indexos.add(r.nextInt(orig.size()));

        for(Integer i : indexos){
            paraulesRandom.add(orig.get(i));
        }
        return paraulesRandom;
    }

    @Override
    protected double innerRun() {
        double dist = 0;
        for (int i = 0; i < MONTECARLO_ITER; i++) {
            dist += calcularDistanciaIdiomes(A, B);
        }
        dist /= MONTECARLO_ITER;
        return dist;
    }
}
