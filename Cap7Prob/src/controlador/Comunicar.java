package controlador;

import java.nio.file.Path;

public interface Comunicar {

    void comunicar(String msg);

    default void carregarImatge(Path ruta) {}

    default void classificar(int id) {}

    default void aturar(int id) {}

    default void progressar(int id, double percent) {}

    default void resultat(int id) {}

}