package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dades {

    private Map<Idioma, List<String>> diccionaris = new HashMap<>();

    private static final String dictPath = "res/Diccionaris/";
    private double[][] distancies;
    public Dades() {
        for (Idioma idi : Idioma.values()) {
            if (idi != Idioma.TOTS) { // TOTS idioma especial
                carregarDiccionari(idi);
                System.out.println("Carregat :" + idi);
            }
        }
        System.out.println("carregats idiomes");
        distancies = new double[Idioma.values().length][Idioma.values().length];
    }

    public void afegirDistancia (Idioma origen, Idioma desti, double distancia){
        distancies[origen.ordinal()][desti.ordinal()] = distancia;
    }

    public Double getDistancia (Idioma origen, Idioma desti) {
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
                    paraulesIdioma.add(paraula.toLowerCase());
                }

                paraula = br.readLine();
            }

        } catch (IOException e) {
            System.err.println("Error carregant " + idioma + ": " + e.getMessage());
        }
        Comparator<String> comp = Comparator.comparingInt(String::length);
        paraulesIdioma.sort(comp);
        diccionaris.put(idioma, paraulesIdioma);
    }

    public Set<Idioma> getIdiomes() {
        return diccionaris.keySet();
    }

    public double[][] getDistancies() { return distancies;}

    public List<String> getParaules(Idioma idioma) {
        return diccionaris.getOrDefault(idioma, Collections.emptyList());
    }

}
