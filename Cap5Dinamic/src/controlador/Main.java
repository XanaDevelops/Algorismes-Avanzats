package controlador;

import Model.CalculIdiomes;
import Model.Dades;
import Model.Idioma;
import Vista.Finestra;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar{
    private static Main instance = null;
    private Comunicar finestra;
    private Dades dades;

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    private final Map<Integer, Comunicar> runnables = new TreeMap<>();

    public static void main(String[] args) {
        if (args.length == 0)
            (new Main()).init();
        else
            new Main();
    }

    public Main(){
        if (instance == null){
            instance = this;
        }else{
            return;
        }
        dades = new Dades();
    }

    private void init(){

        SwingUtilities.invokeLater(() -> finestra = new Finestra());

    }


    @Override
    public void comunicar(String s) {
        String[] args = s.split(":");

//        System.err.println("MAIN: missatge? " + s);
    }

    @Override
    public void calcular(Idioma a, Idioma b){
        //es suposa que TOTS-TOTS es crida des de calcularTots()
        //també es suposa que TOTS es gestiona a Finestra, però per si de cas...
        if (a == Idioma.TOTS){
            for(Idioma idioma : Idioma.values()){
                if(idioma == Idioma.TOTS || idioma == b){
                    continue;
                }
                addAndExec(idioma, b, dades.getIdCount()); //per favor, que tots es fasi des de la Finestra...
            }
        }else if(b == Idioma.TOTS){
            for(Idioma idioma : Idioma.values()){
                if(idioma == Idioma.TOTS || idioma == a){
                    continue;
                }
                addAndExec(idioma, a, dades.getIdCount());
            }
        }else{
            addAndExec(a, b, dades.getIdCount());
        }

    }

    private void addAndExec(Idioma a, Idioma b, int id){
        if(a==b){
            System.err.println("S'ha intentat calcular la distancia entre iguals "+a+"=="+b);
        }
        System.err.println("Calculant D("+a+"-"+b+") "+id);

        CalculIdiomes c = new CalculIdiomes(a,b, id);

        executor.execute(c);
        runnables.put(id, c);
        finestra.calcular(a,b, id);
    }

    @Override
    public void aturar(int id){
        Comunicar c = runnables.remove(id);
        if(c != null){
            c.aturar(id);
            finestra.aturar(id);
        }


    }

    @Override
    public void actualitzar(int id){
        finestra.actualitzar(id);
    }

    public static Main getInstance() {
        return instance;
    }

    public Comunicar getFinestra() {
        return finestra;
    }
    public Dades getDades() {
        return dades;
    }
}