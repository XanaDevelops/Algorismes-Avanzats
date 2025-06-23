package model.Huffman;

import control.Comunicar;
import control.Main;
import model.Dades;

public abstract class Proces implements Runnable, Comunicar {

    protected int id;
    protected boolean aturar = false;

    protected final Dades dades = Main.instance.getDades();

    protected abstract void exec();

    public Proces(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        Main.instance.arrancar(id);
        exec();
        Main.instance.finalitzar(id);
    }

    @Override
    public void comunicar(String s) {
        System.err.println(s);
    }

    @Override
    public void aturar(int id) {
        aturar = true;
    }
}
