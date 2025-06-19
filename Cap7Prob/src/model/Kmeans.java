package model;

import controlador.Comunicar;
import controlador.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kmeans  implements Runnable, Comunicar {

    private List<float[]> colors;
    private int     K;
    private int maxIterations;
    List<float[]> centroids;
    int[] clusterCounts;
    private Dades dades;
    public Kmeans(int K, int maxIterations) {
        this.dades = Main.getInstance().getDades();
        this.K = K;
        this.maxIterations = maxIterations;
    }
    private double distanceHSV(float[] c1, float[] c2) {
        double dh = Math.min(Math.abs(c1[0] - c2[0]), 360 - Math.abs(c1[0] - c2[0])) / 180.0;
        double ds = Math.abs(c1[1] - c2[1]);
        double dv = (c1[2] - c2[2])/255.0;
        return Math.sqrt(dh * dh + ds * ds + dv * dv);
    }


    private void kMeans() {

        Random rnd = new Random();
        colors = dades.getColors();
       //triar els centeroides de forma aleatoria
        centroids = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            float[] randomColor = colors.get(rnd.nextInt(colors.size()));
            centroids.add(randomColor);
        }
        //assignar cada color a un cluster
        int[] assignments = new int[colors.size()];

        for (int iter = 0; iter < maxIterations; iter++) {
            boolean changed = false;

            for (int i = 0; i < colors.size(); i++) {
                float[] color = colors.get(i);
                int closest = -1;
                double minDist = Double.MAX_VALUE;

                for (int j = 0; j < centroids.size(); j++) {
                    float[] centroid = centroids.get(j);
                    double dist = distanceHSV(color, centroid);
                    if (dist < minDist) {
                        minDist = dist;
                        closest = j;
                    }
                }

                if (assignments[i] != closest) {
                    assignments[i] = closest;
                    changed = true;
                }
            }

            //convergencia
            if (!changed) break;

            //promig dels centeroides
            float[][] newCentroids = new float[K][3];
            int[] counts = new int[K];

            for (int i = 0; i < colors.size(); i++) {
                int cluster = assignments[i];
                float[] color = colors.get(i);
                for (int k = 0; k < 3; k++) {
                    newCentroids[cluster][k] += color[k];
                }
                counts[cluster]++;
            }

            for (int j = 0; j < K; j++) {
                if (counts[j] > 0) {
                    for (int k = 0; k < 3; k++) {
                        newCentroids[j][k] /= counts[j];
                    }
                    centroids.set(j, newCentroids[j]);
                }
            }
        }

      clusterCounts = new int[K];
        for (int cluster : assignments) {
            clusterCounts[cluster]++;
        }

    }



    @Override
    public void comunicar(String msg) {
    }


    @Override
    public void run() {
        kMeans();
        dades.setCentroids(centroids);
        dades.setClusterCounts(clusterCounts);
    }


}
