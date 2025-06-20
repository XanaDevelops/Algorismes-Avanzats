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
        default void carregarImatge(String ruta) {}

        default void classificar() {}

        default void progressar(double percent) {}

        default void aturar() {}


}