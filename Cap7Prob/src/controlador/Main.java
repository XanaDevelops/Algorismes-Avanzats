package controlador;

import model.Dades;
import model.Solver;
import vista.Finestra;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

public class Main implements Comunicar {
    private static Main instance;

    private Dades dades;
    private Finestra finestra;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        if(instance == null){
            instance = this;
        }else{
            return;
        }

        dades = new Dades();
        finestra = new Finestra();

    }

    public Dades getDades(){
        return dades;
    }

    public Comunicar getFinestra(){
        return finestra;
    }

    public static Main getInstance(){
        return instance;
    }

    @Override
    public void comunicar(String msg) {
        System.err.println(msg);
    }
}
