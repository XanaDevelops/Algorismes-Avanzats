package model;

import control.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Dades {
    private final Main principal;

    private final List<File> descomprimits = new ArrayList<>();
    private final List<File> comprimits = new ArrayList<>();
    private Extensio extensioComprimit;
    public Extensio getExtensioComprimit() {
        return extensioComprimit;
    }

    public void setExtensioComprimit(Extensio extensioComprimit) {
        this.extensioComprimit = extensioComprimit;
    }

    public Dades(){
        principal = Main.instance;
    }

    public void addDescomprimit(File f){
        if(!descomprimits.contains(f))descomprimits.add(f);
    }
    public void addComprimit(File f){
        if(!comprimits.contains(f))comprimits.add(f);
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

    public List<File> getComprimits() {
        return comprimits;
    }

    public List<File> getDescomprimits() {
        return descomprimits;
    }
}
