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
        oos.writeInt( sb.length());  // Guardar la longitud real
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

        try {
            Map<Byte, String> taula = (Map<Byte, String>) ois.readObject();
            System.out.println("taula obtinguda : " + taula.toString());
            byte [] fileContent = (byte[]) ois.readObject();

            int realBitLength = ois.readInt();  // Leer la longitud original

            StringBuilder sb = new StringBuilder();

            for (byte b : fileContent) {

                sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            }
            sb.setLength(realBitLength); // Truncar els bits de farciment

            System.out.println("tamany sb = "+ sb.length());


            //invirtirem la taula per poder cercar per la codificacio
            Map<String, Byte> taulaIvertida = new HashMap<>();
            for (Map.Entry<Byte, String> entrada : taula.entrySet()) {
                taulaIvertida.put(entrada.getValue(), entrada.getKey());
            }

            //descomprimir els bytes
            StringBuilder sequenciaActual = new StringBuilder();
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            for (int i = 0; i < sb.length(); i++) {
                sequenciaActual.append(sb.charAt(i));
                if (taulaIvertida.containsKey(sequenciaActual.toString())) {//hem trobat una seqüència vàlida
                    bais.write(taulaIvertida.get(sequenciaActual.toString()));
                    sequenciaActual.setLength(0); //buidar la sequencia
                }
            }
            fos.write(bais.toByteArray());
            fos.flush();
            fos.close();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }


}
