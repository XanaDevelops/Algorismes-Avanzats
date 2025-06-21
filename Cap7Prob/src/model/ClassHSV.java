package model;

import controlador.Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

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

            prob.put(p, probabilitat*100);
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
        dades.setColors(colors);
        //avisa a finestra
        Main.getInstance().actualitzarFinestra();
    }


    public static float[] rgbAhsv(int rgb) {
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

        return new float[]{H, S, max};
    }
}