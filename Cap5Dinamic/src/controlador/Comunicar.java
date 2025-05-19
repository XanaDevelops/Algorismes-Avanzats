package controlador;

import Model.Idioma;

import java.util.concurrent.Callable;

public interface Comunicar {
    public void comunicar(String s);

    public default void calcular(Idioma a, Idioma b) {
        calcular(a, b, -1);
    }
    public default void calcular(Idioma a, Idioma b, int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void calcularTot() {
        for(Idioma a : Idioma.values()){
            for(Idioma b : Idioma.values()){
                if(a != b && a != Idioma.TOTS && b != Idioma.TOTS){
                    try {
                        this.calcular(a,b);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public default void aturar(int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void actualitzar(int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
