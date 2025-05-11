package controlador;

import Model.CalculIdiomes;
import Model.Dades;
import Model.Idioma;
import Vista.Finestra;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main implements Comunicar{
    private static Main instance = null;
    private Comunicar finestra;
    private Dades dades;

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);
    private final List<Runnable> runnables = new ArrayList<>();

    public static void main(String[] args) {
        (new Main()).init();
    }

    public Main(){
        if (instance == null){
            instance = this;
        }else{
            return;
        }
       // dades = new Dades();
    }

    private void init(){

        SwingUtilities.invokeLater(() -> finestra = new Finestra());

    }


    @Override
    public void comunicar(String s) {

    }

    @Override
    public void calcular(Idioma a, Idioma b){
        System.err.println("Calculant D("+a+"-"+b+")");
        //es suposa que TOTS-TOTS es crida des de calcularTots()
        //també es suposa que TOTS es gestiona a Finestra, però per si de cas...
        if (a == Idioma.TOTS){
            for(Idioma idioma : Idioma.values()){
                if(idioma == Idioma.TOTS){
                    continue;
                }
                addAndExec(idioma, b);
            }
        }else if(b == Idioma.TOTS){
            for(Idioma idioma : Idioma.values()){
                if(idioma == Idioma.TOTS){
                    continue;
                }
                addAndExec(idioma, a);
            }
        }else{
            addAndExec(a, b);
        }

    }

    private void addAndExec(Idioma a, Idioma b){
        Runnable r = () -> {
            double d =0.0;
            System.err.println("INACABAT! "+a+ "-" +b);
            CalculIdiomes c = new CalculIdiomes(dades,a,b); //TODO: FER RUNNABLE
            try {
                 d = c.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally {
                System.err.println("POSAR A DADES: "+ d); //TODO: GUARDAR A DADES!
            }

        };
        executor.execute(r);
        runnables.add((r));
    }

    @Override
    public void aturar(){
        for(Runnable r : runnables){
            ((Comunicar) r).aturar();
        }
        runnables.clear();
    }

    @Override
    public void actualitzar(){
        finestra.actualitzar();
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