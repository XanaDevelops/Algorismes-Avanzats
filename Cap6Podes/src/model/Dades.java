package model;

import java.util.LinkedList;

import java.io.*;
import java.util.ArrayList;

public class Dades {
    public int getIdCount() {
        return 0; //realment executam només una
    }
    private int [][] graf;

    private Result resultat = new Result(); //conté les ciutats representades pel seu índex en la matriu

    public Dades() {

    }
    public Dades(int [][] graf) {
        this.graf = graf;
    }

    public static final int DEFAULT_GRAPH_SIZE = 10;

    public Result getResultat() {
        return resultat;
    }
    public void setResultat(Result resultat) {
        this.resultat = resultat;
    }

    public LinkedList<Integer> getSolucio() {
        return resultat.resultat;
    }

    public void guardarSolucio(LinkedList<Integer> solucio) {
        this.resultat.resultat = solucio;
    }

    public void generarRandom(){

        generarRandom(DEFAULT_GRAPH_SIZE);
    }

    public void generarRandom(int n){

        graf = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i==j){ //per calcul distancies
                    graf[i][j] = Integer.MAX_VALUE;
                }else
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
