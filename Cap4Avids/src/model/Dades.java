package model;

import control.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dades {
    private final Main principal;

    private final Map<Integer, File> descomprimits = new HashMap<>();
    private final Map<Integer, File> comprimits = new HashMap<>();
    public static final String EXTENSIO = ".kib";
    public static final byte[] magicNumbers = new byte[]{0x4B, 0x49,0x42};

    public static int taskID = 0;

    public Dades(){
        principal = Main.instance;
    }

    public void addDescomprimit(File f){
        if(!descomprimits.containsValue(f))descomprimits.put(taskID++, f);
    }
    public void addComprimit(File f){
        if(!comprimits.containsValue(f))comprimits.put(taskID, f);
    }

    public void removeDescomprimit(File f){
        descomprimits.remove(f);
    }

    public void removeComprimit(File f){
        comprimits.remove(f);
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
