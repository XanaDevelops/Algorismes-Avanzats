package Model;

import controlador.Comunicar;
import controlador.Main;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class CalculIdiomes implements Comunicar, Runnable{
    ExecutorService filsDistanci;
    private final Dades dades;
    private final Idioma A;
    private final Idioma B;
    public CalculIdiomes( Idioma A, Idioma B) {
        this.dades = Main.getInstance().getDades();
        this.A = A;
        this.B = B;
    }

    @Override
    public void run()  {
        filsDistanci = Executors.newFixedThreadPool(2);
//        return calcularDistanciaIdiomes(A,B);
        dades.afegirDistancia(A, B, calcularDistanciaIdiomes(A,B));
    }

    private double calcularDistanciaIdiomes(Idioma a, Idioma b) {

        Callable<Double> taskAB = () -> calcularDistancia(a, b);
        Callable<Double> taskBA = () -> calcularDistancia(b, a);

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
        }finally {
            filsDistanci.shutdown();
        }
    }


    private double calcularDistancia(Idioma origen, Idioma desti) {
        List<String> paraulesA = dades.getParaules(origen);
        List<String> paraulesB = dades.getParaules(desti);

        double suma = 0.0;

        for (String paraulaA : paraulesA) {
            double minim = Double.MAX_VALUE;

            for (String paraulaB : paraulesB) {

                int dist = CalculLevenshtein.calcularDistanciaLevenshtein(paraulaA, paraulaB);
                double distNorm = (double) dist / Math.max(paraulaA.length(), paraulaB.length());

                if (distNorm < minim) {
                    minim = distNorm;
                    if (minim == 0.0) break;
                }
            }

            suma += minim;
        }
        return suma / paraulesA.size();
    }

    private double calcularDistanciaOrdenat(Idioma origen, Idioma desti) {
        List<String> paraulesA = dades.getParaules(origen);
        List<String> paraulesB = dades.getParaules(desti);

        double suma = 0;
        for (String w : paraulesA) {
            suma += distanciaMin(w, paraulesB);
        }
        return suma / paraulesA.size();
    }

    private double distanciaMin(String w, List<String> B) {
        double minim = Double.MAX_VALUE;

        int n = B.size();
        int pos = Collections.binarySearch(B, w);
        int lenW = w.length();


        for (int i = 0; i < n; i++) {
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
        }
        return minim;
    }



    @Override
    public void comunicar(String s) {

    }

    @Override
    public void aturar(){
        filsDistanci.shutdown();
    }


}
