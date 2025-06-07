package model;

import java.io.*;
import java.util.ArrayList;

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

        generarRandom(DEFAULT_GRAPH_SIZE, DEFAULT_GRAPH_SIZE);
    }

    public void generarRandom(int w, int h){

        graf = new int[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                graf[i][j] = 1 + (int) (Math.random() * 100);
            }
        }
    }

    public void setVal(int x, int y, int val){
        graf[y][x] = val;
    }

    public void importarDades(String file) throws RuntimeException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            ArrayList<int[]> aux = new ArrayList<>();
            while ((line = reader.readLine()) != null ) {
                String[] parts = line.split(",");
                int[] auxLine = new int[parts.length];
                for (int j = 0; j < parts.length; j++) {
                    auxLine[j] = Integer.parseInt(parts[j].trim());
                }
                aux.add(auxLine);
            }
            graf = new int[aux.size()][aux.getFirst().length];
            for (int i = 0; i < aux.size(); i++) {
                graf[i] = aux.get(i);
            }
        }
    }

    public void exportarDades(String file) throws RuntimeException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int[] row : graf) {
                for (int j = 0; j < row.length; j++) {
                    writer.write(Integer.toString(row[j]));
                    if (j < row.length - 1) {
                        writer.write(",");
                    }
                }
                writer.write(System.lineSeparator());
            }
            writer.flush();
        }
    }

    public int[][] getGraf(){
        return graf;
    }

    public void setGraf(int[][] graf){
        this.graf = graf;
    }
}
