package model;

import control.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Dades {
    private final Map<Integer, File> aComprimir = new HashMap<>();
    private final Map<Integer, File> aDescomprimir = new HashMap<>();
    public static final String EXTENSIO = ".huf";
    public static final byte[] magicNumbers = new byte[]{0x4B, 0x49,0x42};
    public informacio info;
    private static int taskId = 0;

    public Dades(){

    }
    public void setInfo(informacio info){
        this.info = info;
        Main.instance.estadistiquesLLestes();
    }
    public informacio getInfo(){
        return info;
    }
    public static class informacio{
        public double entropia;
        public long tamanyOriginal;
        public final long tamanyComprimit;
        public  double taxaCompressio;
        public final int simbolsUnics;
        public  double longitudMitjana;
        public  Long tempsCompressio;
        public  Long tempsDecompressio;
        public informacio(double entropia, long tamanyOriginal, long tamanyComprimit,   int simbolsUnics, double longitudMitjana){
            this.entropia = entropia;
            this.tamanyComprimit = tamanyComprimit;
            this.tamanyOriginal = tamanyOriginal;
            this.taxaCompressio = (double)tamanyComprimit/tamanyOriginal*100;
            this.simbolsUnics = simbolsUnics;
            this.longitudMitjana = longitudMitjana;
        }
        public informacio(long tamanyOriginal, long tamanyComprimit, int simbolsUnics){
            this.tamanyOriginal= tamanyOriginal;
            this.tamanyComprimit = tamanyComprimit;
            this.simbolsUnics = simbolsUnics;

        }
        public Long getTempsCompressio(){
            return tempsCompressio;
        }
        public void setTempsCompressio(long tempsCompressio){
            this.tempsCompressio = tempsCompressio;
        }
        public void setTempsDecompressio(long tempsDecompressio){
            this.tempsDecompressio = tempsDecompressio;
        }

    }

    public void addDescomprimit(int id, File f){
        if(!aComprimir.containsValue(f)) aComprimir.put(id, f);
    }
    public void addComprimit(int id, File f){
        if(!aDescomprimir.containsValue(f)) aDescomprimir.put(id, f);
    }

    public void removeAComprimir(File f){
        for(Map.Entry<Integer, File> e : aComprimir.entrySet()){
            if(e.getValue().equals(f)){
                aComprimir.remove(e.getKey());
                break;
            }
        }
    }

    public void removeADescomprimir(File f){
        for(Map.Entry<Integer, File> e : aDescomprimir.entrySet()){
            if(e.getValue().equals(f)){
                aDescomprimir.remove(e.getKey());
                break;
            }
        }
    }

    public void clear(){
        aComprimir.clear();
        aDescomprimir.clear();
    }

    public Map<Integer, File> getADescomprimir() {
        return aDescomprimir;
    }

    public Map<Integer, File> getAComprimir() {
        return aComprimir;
    }

    public static int getTaskId(){
        return taskId++;
    }
}
