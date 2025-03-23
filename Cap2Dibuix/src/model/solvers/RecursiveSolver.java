package model.solvers;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class RecursiveSolver implements Runnable {
    protected int numThreads;
    protected static final int MAX_THREADS = 16;
    protected final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    protected final Deque<Runnable> queue = new ConcurrentLinkedDeque<>();

    private long sleepTime = 0; //nanos dormit

    @Override
    public abstract void run();

    protected synchronized void runThread(Runnable r){
        numThreads++;
        if (numThreads < MAX_THREADS) {
            executor.execute(r);
        }else{
            queue.add(r);
        }
    }

    protected synchronized void endThread(){
        numThreads--;
        if (!queue.isEmpty()) {
            executor.execute(queue.remove());
        }else if(numThreads == 0){
            end();
        }


    }

    protected abstract void end();


    protected void esperar(long millis, int nanos){
        try {
            this.sleepTime += millis*1000000 + nanos;
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected long getSleepTime(){
        return sleepTime;
    }
}
