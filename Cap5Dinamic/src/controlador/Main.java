package controlador;

import Model.Dades;
import Model.Idioma;
import Vista.Finestra;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
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
        dades = new Dades();
    }

    private void init(){

        SwingUtilities.invokeLater(() -> finestra = new Finestra());
    }


    @Override
    public void comunicar(String s) {

    }

    @Override
    public void calcular(Idioma a, Idioma b){
        System.out.println("Calculant D("+a+"-"+b);
        Runnable r = () -> {
            System.err.println("Calculant D("+a+"-"+b);
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