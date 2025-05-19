package controlador;

import Model.Idioma;

public interface Comunicar {
    public void comunicar(String s);

    public default void calcular(Idioma a, Idioma b){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public default void calcularTot(){
        for(Idioma a : Idioma.values()){
            for(int i = a.ordinal()+1; i<Idioma.values().length; i++){
                Idioma b = Idioma.values()[i];
                if(a != b && a != Idioma.TOTS && b != Idioma.TOTS){
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
