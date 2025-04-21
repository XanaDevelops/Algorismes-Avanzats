package model;
import model.Huffman.Node;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Comprimidor {

//    private final Node root;

    private final String fileName;
    public Comprimidor(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Recorre el text original i assigna la codificació a cada byte
     * segons l'arbre de Huffman.
     */
    public void comprimir(Map<Byte, String> taula, String fileOutputName, boolean esSeriablitzable) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileOutputName));
        //guardam primer la taula de codificació + delimitador
        oos.writeObject(taula);
        StringBuilder sb = new StringBuilder();
        int b;
        if (esSeriablitzable) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));

            while( (b = ois.read())!=-1){
                sb.append(taula.get((byte)b));
            }
            ois.close();
        }else{
            FileInputStream fis = new FileInputStream(fileName);
            while((b = fis.read())!=-1){
                sb.append(taula.get((byte)b));
            }
            fis.close();

        }



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < sb.length(); i+=8) {
            StringBuilder current = new StringBuilder(sb.substring(i, Math.min(i + Byte.SIZE, sb.length())));
            while(current.length() < Byte.SIZE){
                current.append("0");
            }
            baos.write((byte) Integer.parseInt(current.toString(), 2));
        }
        oos.writeObject(baos.toByteArray());
        oos.flush();
        oos.close();
    }


    /**
     * Llegeix un fitxer amb la codificació binària del text, acompanyat de la taula de codificació
     * @throws IOException
     */
    public void Descomprimir(String inputFile, String outputFile) throws IOException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));

        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));


        try {
            Map<Byte, String> taula = (Map<Byte, String>) ois.readObject();
            byte [] fileContent = (byte[]) ois.readObject();
            StringBuilder sb = new StringBuilder();
            //esborrar els 0 de relleno
            for (int i = 0; i < fileContent.length; i++) {
                sb.append(String.format("%8s", Integer.toBinaryString(fileContent[i])).replace(" ", "0"));
            }
            //invirtirem la taula per poder cerca per la codificacio
            Map<String, Byte> taulaIvertida = new HashMap<>();
            for (Map.Entry<Byte, String> entrada : taula.entrySet()) {
                taulaIvertida.put(entrada.getValue(), entrada.getKey());
            }

            //descomprimir els bytes
            StringBuilder sequenciaActual = new StringBuilder();
            for (int i = 0; i < sb.length(); i++) {
                sequenciaActual.append(sb.charAt(i));
                if (taulaIvertida.containsKey(sequenciaActual.toString())) {//hem trobat una seqûència vàlida
                    fos.write(taulaIvertida.get(sequenciaActual.toString()));
                    sequenciaActual.setLength(0); //buidar la sequencia
                }
            }
            fos.flush();
            fos.close();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }




    }
}
