package model;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;


public class Dades {

    public static final String PATH_IMATGES = "res/img/";

    private BufferedImage imatge;
    private EnumMap<Paisatge, Double> percentatges;
    private EnumMap<Paisatge, Double> margesDeError;
    private Paisatge etiqueta;
    private List<float[]> colors;
    public BufferedImage getImatge() {
        return imatge;
    }
    private List<float[]> centroids;

    public int[] getClusterCounts() {
        return clusterCounts;
    }

    public void setClusterCounts(int[] clusterCounts) {
        this.clusterCounts = clusterCounts;
    }

    public List<float[]> getCentroids() {
        return centroids;
    }

    public void setCentroids(List<float[]> centroids) {
        this.centroids = centroids;
    }

    private int[] clusterCounts;
    public List<float[]> getColors() {
        return colors;
    }

    public void setColors(List<float[]> colors) {
        this.colors = colors;
    }

    public void setImatge(BufferedImage imatge) {
        this.imatge = imatge;
    }

    public EnumMap<Paisatge, Double> getPercentatges() {
        return percentatges;
    }

    public void setPercentatges(EnumMap<Paisatge, Double> percentatges) {
        this.percentatges = percentatges;
    }

    public EnumMap<Paisatge, Double> getMargesDeError() {
        return margesDeError;
    }

    public void setMargesDeError(EnumMap<Paisatge, Double> margesDeError) {
        this.margesDeError = margesDeError;
    }

    public Paisatge getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Paisatge etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Dades() {
        this.percentatges = new EnumMap<>(Paisatge.class);
        for(Paisatge paisatge : Paisatge.values()) {
            percentatges.put(paisatge, 0.0);
        }
        this.margesDeError = new EnumMap<>(Paisatge.class);
        for(Paisatge paisatge : Paisatge.values()) {
            margesDeError.put(paisatge, 0.0);
        }
    }
}
