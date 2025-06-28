package control;

import model.Huffman.Huffman;

import java.io.File;

public interface Comunicar {
    /**
     * Envia un missatge
     * @param s El missatge
     */
    public void comunicar(String s);

    default void eliminarFitxer(File file, boolean descomprimir) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    default void afegirEnEspera(int id, File file, boolean aComprimir){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void comprimir(int id, String fileIn, String folderOut, Huffman.WordSize wordSize, Huffman.TipusCua tipusCua){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void descomprimir(int id, String fileIn, String folderOut){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void arrancar(int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void finalitzar(int id){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void actualitzar(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void visualitzar(File fileCompress){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default void aturar(int id){
        throw new UnsupportedOperationException("Not supported yet.");

    }
    default void estadistiquesLLestes(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
