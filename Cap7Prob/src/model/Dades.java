package model;

import java.awt.image.BufferedImage;
import java.util.EnumMap;


public class Dades {

    public static final String PATH_IMATGES = "res/img/";

    private BufferedImage imatge;
    private EnumMap<Paisatge, Double> percentatges;
    private EnumMap<Paisatge, Double> margesDeError;
    private Paisatge etiqueta;

    public BufferedImage getImatge() {
        return imatge;
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
}
