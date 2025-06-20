package controlador;

public interface Comunicar {

    void comunicar(String msg);

    default void executarKMeans(int maxIt, int k){
        throw new UnsupportedOperationException();
    }

    default void carregarImatge(String ruta) {}

    default void classificar() {}

    default void progressar(double percent) {}

    default void aturar() {}
}