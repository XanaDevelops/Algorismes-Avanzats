package model;

import control.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dades {
    private final Map<Integer, File> descomprimits = new HashMap<>();
    private final Map<Integer, File> comprimits = new HashMap<>();
    public static final String EXTENSIO = ".kib";
    public static final byte[] magicNumbers = new byte[]{0x4B, 0x49,0x42};

    public static int taskId = 0;

    public Dades(){

    }

    public void addDescomprimit(int id, File f){
        if(!descomprimits.containsValue(f))descomprimits.put(id, f);
    }
    public void addComprimit(int id, File f){
        if(!comprimits.containsValue(f))comprimits.put(id, f);
    }

    public void removeDescomprimit(File f){
        for(Map.Entry<Integer, File> e : descomprimits.entrySet()){
            if(e.getValue().equals(f)){
                descomprimits.remove(e.getKey());
                break;
            }
        }
    }

    public void removeComprimit(File f){
        for(Map.Entry<Integer, File> e : comprimits.entrySet()){
            if(e.getValue().equals(f)){
                comprimits.remove(e.getKey());
                break;
            }
        }
    }

    public void clear(){
        descomprimits.clear();
        comprimits.clear();
    }

    public Map<Integer, File> getComprimits() {
        return comprimits;
    }

    public Map<Integer, File> getDescomprimits() {
        return descomprimits;
    }
}
