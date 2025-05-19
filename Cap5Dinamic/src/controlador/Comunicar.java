package controlador;

import Model.Idioma;

public interface Comunicar {
    public void comunicar(String s);

    public default void calcular(Idioma a, Idioma b, int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void calcularTot(int id){
        for(Idioma a : Idioma.values()){
            for(Idioma b : Idioma.values()){
                if(a != b && a != Idioma.TOTS && b != Idioma.TOTS){
                    calcular(a,b, id++);
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
