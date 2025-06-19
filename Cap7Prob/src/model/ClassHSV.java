package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

public class ClassHSV extends Solver{

    private final int samples;
    private final double zScore =  1.96;

    public ClassHSV(double epsilon) {
        super();
        this.samples = (int)Math.ceil((zScore*zScore * 0.25)/(epsilon*epsilon));
    }

    public ClassHSV() {
        this(0.05);
    }

    @Override
    public void comunicar(String msg) {

    }

    @Override
    public void run() {
        BufferedImage img = dades.getImatge();
        int w = img.getWidth(), h = img.getHeight();
        int n = Math.min(samples, w * h);
        Random rnd = new Random();

        List<float[]> colors = new ArrayList<>();

        EnumMap<Paisatge, Integer> contadors = new EnumMap<>(Paisatge.class);
        for (Paisatge p : Paisatge.values()) {
            contadors.put(p, 0);
        }

        for (int i = 0; i < n; i++) {
            int x = rnd.nextInt(w), y = rnd.nextInt(h);
            int rgb = img.getRGB(x, y);
            float[] hvs = rgbAhsv(rgb);
            colors.add(hvs);
            float H = hvs[0], S = hvs[1], V = hvs[2];

            // Tants com paissatges hagi
            for (Paisatge p : Paisatge.values()) {
                if (p.matches(H, S, V)) {
                    contadors.put(p, contadors.get(p) + 1);
                    break;
                }
            }
        }

        EnumMap<Paisatge, Double> prob  = new EnumMap<>(Paisatge.class);
        EnumMap<Paisatge, Double> marge = new EnumMap<>(Paisatge.class);

        for (Paisatge p : Paisatge.values()) {
            int c = contadors.get(p);
            double probabilitat = c / (double) n;
            double error = zScore * Math.sqrt(probabilitat * (1 - probabilitat) / n);

            prob.put(p, probabilitat);
            marge.put(p, error);
        }

        dades.setPercentatges(prob);
        dades.setMargesDeError(marge);

        Paisatge resultat = Paisatge.BOSC_NORDIC;
        for (Paisatge p : Paisatge.values()) {
            if (prob.get(p) > prob.get(resultat)) {
                resultat = p;
            }
        }
        dades.setEtiqueta(resultat);
        kMeans(colors, 4);
    }


    private float[] rgbAhsv(int rgb) {
        float r = ((rgb >> 16) & 0xFF) / 255f;
        float g = ((rgb >> 8)  & 0xFF) / 255f;
        float b = ( rgb        & 0xFF) / 255f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        float H;
        if (delta == 0) {
            H = 0;
        } else if (max == r) {
            H = 60 * (((g - b) / delta) % 6);
        } else if (max == g) {
            H = 60 * (((b - r) / delta) + 2);
        } else {
            H = 60 * (((r - g) / delta) + 4);
        }
        if (H < 0) H += 360;

        float S = (max == 0) ? 0 : (delta / max);

        return new float[]{H, max, S};
    }

    private void kMeans(List<float[]> colors, int numGrups){
        int maxIteracions = 20;
        Random rnd = new Random();
        List<float[]> centroides = new ArrayList<>();
        for (int i = 0; i < numGrups; i++) {
            centroides.add(colors.get(rnd.nextInt(colors.size())));
        }
        int[] assignments = new int[colors.size()];

        for (int i = 0; i < maxIteracions; i++) {
                
            boolean changed = false;
            for (int j = 0; j < colors.size(); j++) {
                float[] color = colors.get(j);
                int mesCerca = -1;
                double minDist = Double.MAX_VALUE;
                for (int k = 0; k < centroides.size(); k++) {
                    float[] centroid = centroides.get(k);
                    double dist = distanceHSV(color, centroid);
                    if (dist<minDist){
                        minDist = dist;
                        mesCerca = k;
                    }
                }
                if(assignments[j] !=mesCerca){
                    assignments[j] = mesCerca;
                    changed = true;
                }
            }
            if (!changed){
                break;
            }

            //recalcular esl centeroids
            float[][] newCentroides = new float[centroides.size()][3];
            int [] counts = new int[numGrups];
            for (int j = 0; j < colors.size(); j++) {
                int cluster = assignments[j];
                float[] color = colors.get(j);
                for (int k = 0; k < 3; k++) {
                    newCentroides[cluster][k]+=color[k];
                }
                counts[cluster]++;
            }
            for (int j = 0; j < numGrups; j++) {
                if (counts[j]>0){
                    for (int k = 0; k < 3; k++) {
                        newCentroides[j][k]/=counts[j];
                    }
                    centroides.add(j, newCentroides[j]);
                }
            }
        }

        // Mostrar resultados: centroides y proporciones
        int[] clusterCounts = new int[numGrups];
        for (int cluster : assignments) {
            clusterCounts[cluster]++;
        }

        System.out.println("=== Clusters HSV ===");
        for (int j = 0; j < numGrups; j++) {
            float[] c = centroides.get(j);
            float h = c[0], s = c[1], v = c[2];
            double porcentaje = 100.0 * clusterCounts[j] / colors.size();
            System.out.printf("Cluster %d: H=%.1f S=%.2f V=%.2f -> %.2f%%\n", j, h, s, v, porcentaje);
        }


    }

    private double distanceHSV(float[] c1, float[] c2) {
        double dh = Math.min(Math.abs(c1[0] - c2[0]), 360 - Math.abs(c1[0] - c2[0])) / 180.0;
        double ds = c1[1] - c2[1];
        double dv = c1[2] - c2[2];
        return Math.sqrt(dh * dh + ds * ds + dv * dv);
    }
}
