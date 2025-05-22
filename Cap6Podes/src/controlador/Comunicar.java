package controlador;

public interface Comunicar {

    void comunicar(String msg);


    default void calcular(int id) {
        throw new UnsupportedOperationException();
    }

    default void aturar(int id) {
        throw new UnsupportedOperationException();
    }

    default void pausar(int id) {
        throw new UnsupportedOperationException();
    }

    default void reanudar(int id) {
        throw new UnsupportedOperationException();
    }

    default void actualitzar(int id) {
        throw new UnsupportedOperationException();
    }
}