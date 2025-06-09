package model;

import java.util.LinkedList;
import java.util.List;

public class Result {
    public LinkedList<Integer> resultat;
    public int costTotal = 0;
    public int branquesExplorades = 0;
    public int nodesDescartats = 0;

    public Result(){

    }

    @Override
    public String toString() {
        return "Result{" +
                "root=" + resultat +
                ", costTotal=" + costTotal +
                ", branquesExplorades=" + branquesExplorades +
                ", nodesDescartats=" + nodesDescartats +
                '}';
    }
}
