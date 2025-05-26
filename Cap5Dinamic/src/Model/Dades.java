package Model;

import java.io.*;
import java.util.*;

public class Dades {
    public static final String csvFile = "Dades.csv";

    private final Map<Idioma, List<String>> diccionaris = new TreeMap<>();

    private static final String dictPath = "res/Diccionaris/";
    public double[][] getDistancies() {
        return distancies;
    }

    private int idCount = 0;



    private boolean exportar = false;

    private double[][] distancies;
    public Dades() {
        for (Idioma idi : Idioma.values()) {
            if (idi != Idioma.TOTS) { // TOTS idioma especial
                carregarDiccionari(idi);
            }
        }
        System.out.println("carregats idiomes");
        distancies = new double[Idioma.values().length-1][Idioma.values().length-1];


    }



    public void afegirDistancia (Idioma origen, Idioma desti, double distancia){
        distancies[origen.ordinal()][desti.ordinal()] = distancia;
    }

    public double getDistancia (Idioma origen, Idioma desti) {
        return distancies[origen.ordinal()][desti.ordinal()];
    }

    private void carregarDiccionari(Idioma idioma) {
        String nomF = dictPath + idioma.name().toUpperCase() + ".dic";

        List<String> paraulesIdioma = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomF))) {

            String paraula = br.readLine();
            while (paraula != null) {
                paraula = paraula.trim();

                if (!paraula.isEmpty()) {
                    paraulesIdioma.add(paraula);
                }

                paraula = br.readLine();
            }

        } catch (IOException e) {
            System.err.println("Error carregant " + idioma + ": " + e.getMessage());
        }

        //ordenar per tamany, despres alfabetic (sort es estable)
        paraulesIdioma.sort(Comparator.comparingInt(String::length));

        diccionaris.put(idioma, paraulesIdioma);
    }

    public Set<Idioma> getIdiomes() {

        return diccionaris.keySet();
    }

    public void exportarDades(){
        System.err.println("exportant");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(" ,");
            for (int i = 0; i < distancies.length; i++) {
                writer.write(Idioma.values()[i].name());
                if (i < distancies.length - 1) {
                    writer.write(",");
                }
            }
            writer.write(System.lineSeparator());
            for (int i = 0; i < distancies.length; i++) {
                writer.write(Idioma.values()[i].name()+", ");
                double[] row = distancies[i];
                for (int j = 0; j < row.length; j++) {
                    writer.write(Double.toString(row[j]));
                    if (j < row.length - 1) {
                        writer.write(",");
                    }
                }
                writer.write(System.lineSeparator());
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importarDades() {
        System.err.println("important");
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line = reader.readLine(); // cabecera
            int n = Idioma.values().length-1;
            distancies = new double[n][n];
            int i = 0;
            while ((line = reader.readLine()) != null && i < n) {
                String[] parts = line.split(",");
                for (int j = 1; j < parts.length && j <= n; j++) {
                    distancies[i][j-1] = Double.parseDouble(parts[j].trim());
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getIdCount(){
        return idCount++;
    }

    public final List<String> getParaules(Idioma idioma) {
        return diccionaris.getOrDefault(idioma, Collections.emptyList());
    }

    public boolean isExportar() {
        return exportar;
    }

    public void setExportar(boolean exportar) {
        this.exportar = exportar;
    }

}
