//
//package model;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.*;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadPoolExecutor;
//
//public class Compressor {
//    private final Huffman huffman;
//    private final String input;
//    private final String folderOutput;
//
//    Dades data;
//    private static final int N_THREADS = 8;
//    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_THREADS);
//
//    public Compressor(Huffman huffman, Dades data, String input, String folderOutput) {
//        this.huffman = huffman;
//        this.input = input;
//        this.data = data;
//        this.folderOutput = folderOutput;
//    }
//
//    /**
//     * Comprimeix el fitxer d'entrada.
//     * La capçalera té la següent forma
//     * (Byte) --> nombre de caracters únics de l'arxiu
//     * Conjunt de bytes (Simbol, longitud) --> forma canònica de la codificació de huffman
//     * A baix de la capçalera s'escriu el contingut codificat de huffman
//     *
//     * @throws IOException
//     */
//
//    public void compressFile() throws IOException {
//        Map<Byte, String> table = huffman.getTable();
//        //calcular la longitud de les codificaciones de cada byte
//        int[] codeLengths = new int[Huffman.BITSIZE];
//        int totalUnicSymbols = 0;
//        List<Integer> symbols = new ArrayList<>();
//        for (Map.Entry<Byte, String> e : table.entrySet()) {
//            int sym = e.getKey() & 0xFF; //byte positiu
//            codeLengths[sym] = e.getValue().length();
//            symbols.add(sym);
//            totalUnicSymbols++;
//        }
//        //generar codi canònic a partir les longituds dels símbols
//        byte[][] canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);
////        byte [] magicNumbers = data.getExtensioComprimit().getMagicBytes();
//
//        //afegir la signatura de l'extensió manualment
//        String fileName = input.split("/")[input.split("/").length - 1];
//        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
//        try (OutputStream fos = Files.newOutputStream(Path.of(folderOutput+ "Compressed "+fileName+".txt"));
//             BufferedOutputStream bufOut = new BufferedOutputStream(fos);
//             DataOutputStream dos = new DataOutputStream(bufOut);
//             BitOutputStream bitOut = new BitOutputStream(bufOut)) {
//
//            //guardar l'extensió original de l'arxiu
////            dos.writeShort(magicNumbers.length);
////            dos.write(magicNumbers);
////
//            Path inputPath = Path.of(input);
////            String[] extension = input.split("\\.", 2);
////            extension[1] = "."+ extension[1];
////            byte[] extensionBytes = extension[1].getBytes(StandardCharsets.UTF_8);
////            //tamany de l'extensió
////            dos.writeShort(extensionBytes.length);
////            //l'extensió en sí
////            dos.write(extensionBytes);
//
//            dos.writeInt(totalUnicSymbols);
//            // llista de (simbol, longitud)
//            for (int symbol = 0; symbol < Huffman.BITSIZE; symbol++) {
//                int len = codeLengths[symbol];
//                if (len > 0) {
//                    dos.writeByte(symbol);
//                    dos.writeByte(len);
//                }
//            }
//
//            int originalBytes = (int) Files.size(inputPath);
//            dos.writeInt(originalBytes);
//            dos.flush();
//
//
//
//            //Escriure la codificació del contingut de l'arxiu d'entrada
//            try (InputStream fis = new BufferedInputStream(Files.newInputStream(inputPath))) {
//                int b;
//                while ((b = fis.read()) != -1) {
//
//                        byte[] codeBits = canonCodes[b & 0xFF];
//                        if (codeBits !=null) {
//                        for (byte codeBit : codeBits) {
//                            bitOut.writeBit(codeBit == 1);
//                        }
//                    }
//                }
//            }
//            bitOut.flush();
//
//        }
//    }
//
//}
//


package model.Huffman;

import model.BitsManagement.BitOutputStream;
import model.Dades;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Compressor {
    private final Huffman huffman;
    private final String inputPath;
    private final String outputFolder;
    Dades data;
    public Compressor(Huffman huffman, Dades data, String inputPath, String outputFolder) {
        this.huffman = huffman;
        this.data = data;
        this.inputPath = inputPath;
        this.outputFolder = outputFolder;
    }

    /**
     * Comprimeix el fitxer d'entrada.
     * La capçalera té la següent forma
     * (Byte) --> nombre de caracters únics de l'arxiu
     * Conjunt de bytes (Simbol, longitud) --> forma canònica de la codificació de huffman
     * A baix de la capçalera s'escriu el contingut codificat de huffman
     *
     * @throws IOException
     */

    public void compressFile() throws IOException {
        Map<Long, String> table = huffman.getTable();
        long time = System.nanoTime();
        //calcular la longitud de les codificaciones de cada byte
        Map<Long, Integer> codeLengths = new TreeMap<>();
        int totalUnicSymbols = 0;
        List<Long> symbols = new ArrayList<>();
        for (Map.Entry<Long, String> e : table.entrySet()) {
            long sym = e.getKey(); //byte positiu
            codeLengths.put(sym, e.getValue().length());
            symbols.add(sym);
            totalUnicSymbols++;
        }
        //generar codi canònic a partir les longituds dels símbols
        Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);

        //afegir la signatura de l'extensió manualment
        String fileName = inputPath.split("/")[inputPath.split("/").length - 1];
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        System.out.println("File:" +outputFolder + fileName + Dades.EXTENSIO);
        try (OutputStream fos = Files.newOutputStream(Path.of(outputFolder + fileName + Dades.EXTENSIO));
             BufferedOutputStream bufOut = new BufferedOutputStream(fos);
             DataOutputStream dos = new DataOutputStream(bufOut);
             BitOutputStream bitOut = new BitOutputStream(bufOut)) {

            /// HEADER
            //guardar l'extensió original de l'arxiu
            dos.write(Dades.magicNumbers);

            //guardar el tamany de les paraules comprimides
            dos.writeShort(huffman.getByteSize());

            Path inputPath = Path.of(this.inputPath);
            String[] extension = this.inputPath.split("\\.", 2);
            byte[] extensionBytes = extension[1].getBytes(StandardCharsets.UTF_8);
            //tamany de l'extensió
            dos.writeShort(extensionBytes.length);
            //l'extensió en sí
            dos.write(extensionBytes);

            dos.writeInt(totalUnicSymbols);
            // llista de (simbol, longitud)
            for (Map.Entry<Long, Integer> e : codeLengths.entrySet()) {
                dos.writeLong(e.getKey());
                dos.writeInt(e.getValue());
            }

            long originalBytes = Files.size(inputPath);
            dos.writeLong(originalBytes);
            dos.flush();

            //Escriure la codificació del contingut de l'arxiu d'entrada
            try (InputStream fis = new BufferedInputStream(Files.newInputStream(inputPath))) {
                long b;
                while ((b = fis.read()) != -1) {
                    for (int j = 1; j < huffman.getByteSize(); j++) {
                        b = b<<8;
                        int aux = fis.read();
                        b |= aux != -1 ? aux : 0;
                    }
                    byte[] codeBits = canonCodes.get(b);
                    assert codeBits != null;
                    for (byte codeBit : codeBits) {
                        bitOut.writeBit(codeBit == 1);
                    }

                }
            }
            bitOut.flush();
        }


        time = System.nanoTime() - time;
//        data.addTempsCompressio(time, fileName, huffman.getTipusCua);
    }


}