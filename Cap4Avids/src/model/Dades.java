package model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Dades {
    private final Map<Integer, File> aComprimir = new HashMap<>();
    private final Map<Integer, File> aDescomprimir = new HashMap<>();
    public static final String EXTENSIO = ".kib";
    public static final byte[] magicNumbers = new byte[]{0x4B, 0x49,0x42};

    private static int taskId = 0;

    public Dades(){

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
