package Model.Calcul;

import Model.Dades;
import Model.Idioma;
import controlador.Comunicar;
import controlador.Main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CalculIdiomes implements Comunicar, Runnable{
    private final static Logger logger = Logger.getLogger(CalculIdiomes.class.getName());

    static{
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String logFileName = "execution_" + timestamp + ".log";
            FileHandler fh = new FileHandler(logFileName, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new RuntimeException("Error configuring logger", e);
        }
    }

    private void log(double dist, long time){
        logger.info(new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + "D("+A+"<->"+B+ "): "+ dist + "T: " + time);
    }
    protected ExecutorService filsDistanci;
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();

    protected final ThreadPoolExecutor executorA = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS/2),
            executorB = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS/2);

    private final Dades dades;
    protected final Idioma A;
    protected final Idioma B;

    protected int id;

    protected boolean aturar = false;

    public CalculIdiomes(Idioma A, Idioma B, int id) {
        this.dades = Main.getInstance().getDades();
        this.A = A;
        this.B = B;
        this.id = id;
        System.err.println(N_THREADS);
    }

    protected double innerRun(){
        return calcularDistanciaIdiomes(A,B);
    }

    @Override
    public void run()  {
        Main.getInstance().getFinestra().calcular(A, B, id);

        filsDistanci = Executors.newFixedThreadPool(2);
        long startTime = System.nanoTime();
        double dist = innerRun();
        if (dist==Double.POSITIVE_INFINITY || Double.isNaN(dist)){
            dist = 0.0;
        }
        long endTime = System.nanoTime();
        long time = endTime - startTime;

        log(dist, time);

        Main.getInstance().pasarTemps(id, time);

        //Al final "dist" es la distancia final A<->B, no A->B o B->A...
        dades.afegirDistancia(A, B, dist);
        dades.afegirDistancia(B, A, dist);
        if(!aturar)
            Main.getInstance().actualitzar(id);
        filsDistanci.shutdown();
        executorA.shutdown();
        executorB.shutdown();
    }

    protected double calcularDistanciaIdiomes(Idioma a, Idioma b) {
        List<String> parA = getParaules(a);
        List<String> parB = getParaules(b);

        Callable<Double> taskAB = () -> calcularDistanciaOrdenat(parA, parB, executorA);
        Callable<Double> taskBA = () -> calcularDistanciaOrdenat(parB, parA, executorB);

        try {
            // llança fil per sentit
            Future<Double> futureAB = filsDistanci.submit(taskAB);
            Future<Double> futureBA = filsDistanci.submit(taskBA);

            // es com fer join
            double distAB = futureAB.get();
            double distBA = futureBA.get();

            return Math.sqrt(distAB * distAB + distBA * distBA);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.err.println("Càlcul interromput per " + a + "-" + b);
            return Double.NaN;

        } catch (ExecutionException e) {
            System.err.println("Error al càlcular la distància" + e);
            return Double.NaN;
        }catch(RejectedExecutionException e){
            if(!aturar)
                System.err.println("Error amb l'executor");
            return Double.NaN;
        }finally {
        }
    }

    protected List<String> getParaules(Idioma x){
        return dades.getParaules(x);
    }

    private double calcularDistanciaOrdenat(List<String> paraulesA, List<String> paraulesB, ThreadPoolExecutor executor) throws ExecutionException, InterruptedException {
        double[] acumulator = new double[N_THREADS/2];

        List<Future<?>> tasks = new ArrayList<>();

        int size = paraulesA.size()/ (N_THREADS/2);

        for(int i = 0; i < acumulator.length-1 && !aturar; i++){
            int finalI = i;

            tasks.add(executor.submit(() -> {
                calcularConcurrent(paraulesA.subList(finalI * size, (finalI +1)* size), paraulesB, finalI, acumulator);

            }));
        }
        //final
        tasks.add(executor.submit(() -> {
            calcularConcurrent(paraulesA.subList((acumulator.length-1) * size, paraulesA.size()), paraulesB, acumulator.length-1, acumulator);

        }));

        while (!tasks.isEmpty() && !aturar) {
            tasks.getFirst().get();
            tasks.removeFirst();
        }
        double suma = 0;
        for (double i : acumulator) {
            suma += i;
        }

        return suma / paraulesA.size();
    }

    private void calcularConcurrent(List<String> slice, List<String> paraulesB, int id, double[] acumulators){
        double aux = 0;
        for (String w : slice) {
            aux += distanciaMin(w, paraulesB);
        }
        acumulators[id] = aux;
    }

    private double distanciaMin(String w, List<String> B) {
        double minim = Double.MAX_VALUE;

        int n = B.size();

        Comparator<String> comp = Comparator.comparingInt(String::length);
        int pos = Collections.binarySearch(B, w, comp);
        if (pos < 0) {
            pos = -pos - 1;
        }

        int lenW = w.length();


        for (int i = 0; i < n; i++) {
            if (aturar){
                return Double.MAX_VALUE;
            }
            boolean capEsq = false, capDret = false, esDreta = true;

            for (int j = 0; j < 2; j++) {
                int costat =  esDreta ? 1 : -1;
                esDreta = !esDreta;

                int newI = pos + costat * i;

                if (newI < 0) capEsq = true;
                else if (newI >= n) capDret = true;
                else {
                    String x = B.get(newI);
                    int lenX = x.length();

                    double difDistancia = (double) Math.abs(lenW - lenX) / Math.max(lenW, lenX);

                    if (difDistancia >= minim) {
                        if (esDreta) capDret = true;
                        else capEsq = true;
                    } else {
                        int dist = CalculLevenshtein.calcularDistanciaLevenshtein(w, x);
                        double distNorm = ((double) dist) / Math.max(lenW, lenX);
                        minim = Math.min(minim, distNorm);
                    }
                }
            }
            if (capEsq && capDret) break;
            if(minim==0){
                break;
            }
        }
        return minim;
    }



    @Override
    public void comunicar(String s) {

    }

    @Override
    public void aturar(int id){
        if(!aturar && filsDistanci != null)
            filsDistanci.shutdown();
        aturar = true;

    }


}
