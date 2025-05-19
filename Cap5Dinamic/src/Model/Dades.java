package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dades {

    private Map<Idioma, List<String>> diccionaris = new TreeMap<>();

    private static final String dictPath = "res/Diccionaris/";
    public double[][] getDistancies() {
        return distancies;
    }


    private double[][] distancies;
    private boolean parellaOTot = true; //true si es calcula la distancia entre un idioma i tota la resta o entre tots i tots
    public Dades() {
        for (Idioma idi : Idioma.values()) {
            if (idi != Idioma.TOTS) { // TOTS idioma especial
                carregarDiccionari(idi);
            }
        }
        System.out.println("carregats idiomes");
        distancies = new double[Idioma.values().length-1][Idioma.values().length-1];


    }

    public void setParellaOTot(boolean v) {
        this.parellaOTot = v;
    }
    public boolean isParellaOTot() {
        return parellaOTot;
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

        diccionaris.put(idioma, paraulesIdioma);
    }

    public Set<Idioma> getIdiomes() {

        return diccionaris.keySet();
    }



    public List<String> getParaules(Idioma idioma) {
        return diccionaris.getOrDefault(idioma, Collections.emptyList());
    }

}
