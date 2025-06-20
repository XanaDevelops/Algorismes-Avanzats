package controlador;

public interface Comunicar {

    void comunicar(String msg);

    default void executarKMeans(int k, int maxIt){
        throw new UnsupportedOperationException();
    }

    default void classificarHSV() {
        throw new UnsupportedOperationException();
    }
    default void classificarXarxa() {
        throw new UnsupportedOperationException();
    }

    default void actualitzarFinestra(){
        throw new UnsupportedOperationException();
    }
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