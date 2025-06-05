package model;

public class Dades {
    public int getIdCount() {
        return 0;
    }
    private int [][] graf;

    public static final int DEFAULT_GRAPH_SIZE = 10;

    public class Solucio {
    }

    public int guardarSolucio(Solucio sol) {
        return 0;
    }

    public void generarRandom(){
        if (graf == null) {
            graf = new int[DEFAULT_GRAPH_SIZE][DEFAULT_GRAPH_SIZE];
        }
        generarRandom(graf[0].length, graf.length);
    }

    public void generarRandom(int w, int h){
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                graf[i][j] = 1 + (int) (Math.random() * 100);
            }
        }
    }

    public int[][] getGraf(){
        return graf;
    }

    public void setGraf(int[][] graf){
        this.graf = graf;
    }
}
