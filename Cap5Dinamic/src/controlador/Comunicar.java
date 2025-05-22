package controlador;

import Model.Idioma;

import java.util.concurrent.Callable;

public interface Comunicar {
    public void comunicar(String s);

    public default void calcular(Idioma a, Idioma b) {
        calcular(a, b, -1, false, -1);
    }
    public default void calcular(Idioma a, Idioma b, int id){
        calcular(a, b, id, false, -1);
    }
    public default void calcular(Idioma a, Idioma b, boolean prob, int percent){
        calcular(a, b, -1, prob, percent);
    }
    public default void calcular(Idioma a, Idioma b, int id, boolean prob, int percent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void calcularTot(boolean prob, int percent) {
        for(Idioma a : Idioma.values()){
            for(int i = a.ordinal()+1; i<Idioma.values().length; i++){
                Idioma b = Idioma.values()[i];
                if(a != b && a != Idioma.TOTS && b != Idioma.TOTS){
                    try {
                        this.calcular(a,b, prob, percent);
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

    public default void pasarTemps(int id, long nanos){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
