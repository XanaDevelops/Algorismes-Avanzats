package Model;

import java.util.concurrent.*;

public class CalculIdiomes {
    private final ExecutorService filsDistanci = Executors.newFixedThreadPool(2);

    public double calcularDistanciaIdiomes(Idioma a, Idioma b) {

        Callable<Double> taskAB = () -> calcularDistancia(a, b);
        Callable<Double> taskBA = () -> calcularDistancia(b, a);

        try {
            // llança fil per sentit
            Future<Double> futureAB = filsDistanci.submit(taskAB);
            Future<Double> futureBA = filsDistanci.submit(taskBA);

            // es com fer join
            double distAB = futureAB.get();
            double distBA = futureBA.get();

            double resultat = Math.sqrt(distAB*distAB + distBA*distBA);
            return rtnDouble(resultat);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Càlcul interromput per " + a + "-" + b);

            return rtnDouble(Double.NaN);

        } catch (ExecutionException e) {
            System.err.println("Error al càlcular la distància" + e);

            return rtnDouble(Double.NaN);
        }
    }

    private double rtnDouble(double x){
        shutdownFilsDistanci();
        return x;
    }

    // TODO
    private double calcularDistancia(Idioma origen, Idioma desti) throws InterruptedException {
        long retardMillis = ThreadLocalRandom.current()
                .nextLong(100, 10001);
        Thread.sleep(retardMillis);
        System.out.println("Calculant " + origen + " -> " + desti + " al fil: " + Thread.currentThread().getName());
        return ThreadLocalRandom.current().nextDouble();
    }

    public void shutdownFilsDistanci() {
        filsDistanci.shutdown();
    }

}
