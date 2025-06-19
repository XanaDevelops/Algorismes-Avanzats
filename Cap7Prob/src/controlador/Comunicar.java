package controlador;

import java.nio.file.Path;

public interface Comunicar {

    void comunicar(String msg);

    default void carregarImatge(String ruta) {}

    default void classificar() {}

    default void aturar() {}

    default void progressar(double percent) {}

    default void resultat() {}

}