package model;

import controlador.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.EnumMap;

public class XarxaSolver extends Solver{

    private double[][] entradas;
    private double[][] sortides;

    public enum Colors{
        BLANC, NEGRE, BLAU, VERD_CLAR, VERD_OBSCUR, MARRO, ARENA, GROC, TARONJA, VERMELL;
    }

    Xarxa xarxa;

    public XarxaSolver(){
        File xarxaFile = new File("res/xarxa.bin");
        if(xarxaFile.exists()){
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(xarxaFile))) {
                xarxa = (Xarxa) ois.readObject();
            }catch (InvalidClassException e){
                System.err.println("Xarxa ha canviat, creant una nova");
                xarxa = new Xarxa(Colors.values().length, new int[]{6, 6}, Paisatge.values().length);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }else {
            xarxa = new Xarxa(Colors.values().length, new int[]{6, 6}, Paisatge.values().length);
        }
        File carpeta = new File(Dades.PATH_IMATGES);
        File[] fotos = carpeta.listFiles((dir, name) -> name.contains("test"));

        entradas = new double[fotos.length][Colors.values().length];
        sortides = new double[fotos.length][Paisatge.values().length];
        for (int j = 0; j < fotos.length; j++) {
            File f = fotos[j];
            double[] entrada = new double[Colors.values().length];
            double[] sortida = new double[Paisatge.values().length];
            try {
                entrada = generarEntrada(f);
                if (f.getName().contains("bosc")){
                    sortida[Paisatge.BOSC_NORDIC.ordinal()] = 1;
                }
                if (f.getName().contains("selva")){
                    sortida[Paisatge.SELVA_TROPICAL.ordinal()] = 1;
                }
                if(f.getName().contains("costa")){
                    sortida[Paisatge.PAISATGE_COSTANER.ordinal()] = 1;
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            entradas[j] = entrada;
            sortides[j] = sortida;
        }



    }

    public double[] generarEntrada(BufferedImage img){
        assert img != null;

        int pixels = img.getWidth() * img.getHeight();

        double[] entrada = new double[Colors.values().length];
        int[] colorIndex = new int[Colors.values().length];

        for (int i = 0; i < img.getHeight(); i++) {
            for (int k = 0; k < img.getWidth(); k++) {
                colorIndex[getColorIndex(img.getRGB(k, i))]++;
            }
        }
        for (int i = 0; i < colorIndex.length; i++) {
            entrada[i] = colorIndex[i] / (double) pixels;
        }

        return entrada;
    }
    public double[] generarEntrada(File f) throws IOException {
        BufferedImage img = ImageIO.read(f);
        return generarEntrada(img);
    }

    public int getColorIndex(int color){
        Color c = new Color(color, false);
        float[] HSB = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        float H = HSB[0] * 360;
        float S = HSB[1] * 100;
        float B = HSB[2] * 100;
        float A = c.getAlpha();

        if(A < 150){
            return Colors.BLANC.ordinal();
        }
        if(B < 15){
            return Colors.NEGRE.ordinal();
        }
        if(S < 17){
            return Colors.BLANC.ordinal();
        }

        if(H >= 75 && H < 155){
            if(B < 60){
                return Colors.VERD_OBSCUR.ordinal();
            }
            return Colors.VERD_CLAR.ordinal();
        }
        if(H >= 155 && H < 260){
            return Colors.BLAU.ordinal();
        }
        if(H >= 45 && H < 75){
            return Colors.GROC.ordinal();
        }
        if(H >= 20 && H < 45){
            if(S > 50){
                return Colors.TARONJA.ordinal();
            }
            if(B < 70){
                return Colors.MARRO.ordinal();
            }

            return Colors.ARENA.ordinal();
        }

        return Colors.VERMELL.ordinal();
    }


    @Override
    public void comunicar(String msg) {

    }

    @Override
    public void run() {
        double[] entrada = generarEntrada(dades.getImatge());
        double[] sortida = xarxa.predecir(entrada);

        EnumMap<Paisatge, Double> resultats = dades.getPercentatges();
        for(Paisatge paisatge : Paisatge.values()){
            resultats.put(paisatge, sortida[paisatge.ordinal()]*100);
        }
        EnumMap<Paisatge, Double> errors = dades.getMargesDeError();
        for(Paisatge paisatge : Paisatge.values()){
            errors.put(paisatge, xarxa.getErrorTotal());
        }

        Main.getInstance().getFinestra().actualitzarFinestra();
    }

    @Override
    public void entrenarXarxa(int epocs) {
        xarxa = new Xarxa(Colors.values().length, new int[]{6, 6}, Paisatge.values().length);
        xarxa.entrenar(entradas, sortides, epocs);

    }



    public double[][] getEntradas() {
        return entradas;
    }
    public double[][] getSortides(){
        return sortides;
    }
}
