package model;

public class Result {
    public Solver.Node root = null;
    public int costTotal = 0;
    public int branquesExplorades = 0;
    public int nodesDescartats = 0;

    public Result(){

    }

    @Override
    public String toString() {
        return "Result{" +
                "root=" + root +
                ", costTotal=" + costTotal +
                ", branquesExplorades=" + branquesExplorades +
                ", nodesDescartats=" + nodesDescartats +
                '}';
    }
}
