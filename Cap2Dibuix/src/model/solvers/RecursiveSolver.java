package model.solvers;

import java.lang.management.ManagementFactory;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Classe abstracta que facilita la creació d'un generador amb fils concurrents
 */
public abstract class RecursiveSolver implements Runnable {
    protected int numThreads; //nums de threads total
    protected static final int MAX_THREADS = Runtime.getRuntime().availableProcessors(); //hyperthreading

    protected final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    protected final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();

    private long sleepTime = 0; //nanos dormit


    /**
     * Permet aturar el fil d'execució
     */
    protected volatile boolean aturar = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void run();

    /**
     * Executa un nou fil, o l'encua per a posterior execució
     * @param r metode a executar
     */
    protected synchronized void runThread(Runnable r){
        if (aturar) {
            return;
        }
        numThreads++;
        Runnable rr = () -> {
            r.run();
            endThread();};

        if (numThreads < MAX_THREADS) {
            executor.execute(rr);
        }else{
            queue.add(rr);
        }
    }

    /**
     * Mètode que s'executa al finalitzar un fil.
     * Cridat internament per {@code runThread()}. <br>
     * També allibera un espai del executor i desencua un nou element
     * Si no queden elements crida {@code end()}
     */
    protected synchronized void endThread(){
        numThreads--;
        if (!queue.isEmpty() && !aturar) {
            executor.execute(queue.remove());
        }else if(numThreads == 0 && ((ThreadPoolExecutor) executor).getActiveCount() == 0){
            end();
        }


    }

    /**
     * Mètode cridat per {@code endThread()} si no queden fils a executar
     */
    protected abstract void end();


    /**
     *
     * Espera i emmagatzema els temps esperat a 'getSleepTime()'
     *
     * @param millis
     * @param nanos
     */
    protected void esperar(long millis, int nanos){
        try {
            this.sleepTime += millis*1000 + nanos;
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected long getSleepTime(){
        return sleepTime;
    }
}
