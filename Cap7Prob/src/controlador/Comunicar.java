package controlador;

public interface Comunicar {

    void comunicar(String msg);


    default void calcular(int[][] matrix, boolean stepMode) {
        throw new UnsupportedOperationException();
    }

    default void aturar(int id) {
        throw new UnsupportedOperationException();
    }

    default void step(int id){throw new UnsupportedOperationException();}

    default void actualitzar(int id) {
        throw new UnsupportedOperationException();
    }
}