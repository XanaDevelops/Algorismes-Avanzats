package model.Huffman;

import control.Comunicar;
import control.Main;
import model.Dades;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class Proces implements Runnable, Comunicar {

    protected int id;
    protected boolean aturar = false;

    protected final Dades dades = Main.instance.getDades();

    //protected final int N_THREADS = Runtime.getRuntime().availableProcessors();
    protected final int N_THREADS = HuffHeader.N_CHUNKS;

    protected final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS);
    protected final ArrayList<Future<?>> futures = new ArrayList<>();



    protected abstract void exec();

    public Proces(int id) {
        this.id = id;
    }


    protected void executar(Runnable r){
        futures.add(executor.submit(r));
    }

    protected void waitAll(){
        while(!futures.isEmpty()){
            try {
                futures.removeFirst().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        Main.instance.arrancar(id);
        exec();
        Main.instance.finalitzar(id);
    }


    @Override
    public void aturar(int id) {
        aturar = true;
    }
}
