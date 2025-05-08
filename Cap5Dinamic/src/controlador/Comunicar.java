package controlador;

import Model.Idioma;

public interface Comunicar {
    public void comunicar(String s);

    public default void calcular(Idioma a, Idioma b){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void calcularTot(){
        for(Idioma a : Idioma.values()){
            for(Idioma b : Idioma.values()){
                if(a.compareTo(b) != 0){
                    calcular(a,b);
                }
            }
        }
    }

    public default void aturar(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void actualitzar(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
