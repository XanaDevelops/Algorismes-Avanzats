package Model;

import controlador.Comunicar;
import controlador.Main;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.*;

public class CalculIdiomes implements Comunicar, Runnable{
    ExecutorService filsDistanci;
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();

    private final ThreadPoolExecutor executorA = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS/2),
            executorB = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS/2);

    private final Dades dades;
    private final Idioma A;
    private final Idioma B;

    private int id;

    private boolean aturar = false;

    public CalculIdiomes(Idioma A, Idioma B, int id) {
        this.dades = Main.getInstance().getDades();
        this.A = A;
        this.B = B;
        this.id = id;
        System.err.println(N_THREADS);
    }

    @Override
    public void run()  {
        Main.getInstance().getFinestra().calcular(A, B, id);

        filsDistanci = Executors.newFixedThreadPool(2);
        dades.afegirDistancia(A, B, calcularDistanciaIdiomes(A,B));
        if(!aturar)
            Main.getInstance().actualitzar(id);
    }

    private double calcularDistanciaIdiomes(Idioma a, Idioma b) {

        Callable<Double> taskAB = () -> calcularDistanciaOrdenat(a, b, true);
        Callable<Double> taskBA = () -> calcularDistanciaOrdenat(b, a, false);

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
            filsDistanci.shutdown();
        }
    }

    private double calcularDistanciaOrdenat(Idioma origen, Idioma desti, boolean isA) throws ExecutionException, InterruptedException {
        List<String> paraulesA = dades.getParaules(origen);
        List<String> paraulesB = dades.getParaules(desti);

        double[] acumulator = new double[N_THREADS/2];
        ThreadPoolExecutor executor = isA ? executorA : executorB;
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
        executor.shutdown();
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
                        double distNorm = (double) dist / Math.max(lenW, lenX);
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
