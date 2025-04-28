
package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Compressor {
    private final Huffman huffman;
    private final String input;
    private final String output;
    Dades data;
    public Compressor(Huffman huffman, Dades data, String input, String output) {
        this.huffman = huffman;
        this.input = input;
        this.data = data;
        this.output = output;
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
        Map<Byte, String> table = huffman.getTable();
        //calcular la longitud de les codificaciones de cada byte
        int[] codeLengths = new int[256];
        int totalUnicSymbols = 0;
        List<Integer> symbols = new ArrayList<>();
        for (Map.Entry<Byte, String> e : table.entrySet()) {
            int sym = e.getKey() & 0xFF; //byte positiu
            codeLengths[sym] = e.getValue().length();
            symbols.add(sym);
            totalUnicSymbols++;
        }
        //generar codi canònic a partir les longituds dels símbols
        byte[][] canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);

        try (OutputStream fos = Files.newOutputStream(Path.of(output));
             BufferedOutputStream bufOut = new BufferedOutputStream(fos);
             DataOutputStream dos = new DataOutputStream(bufOut);
             BitOutputStream bitOut = new BitOutputStream(bufOut)) {

            //guardar l'extensió original de l'arxiu
            byte [] magicNumbers = data.getExtensioComprimit().getMagicBytes();
            dos.writeShort(magicNumbers.length);
            dos.write(magicNumbers);

            Path inputPath = Path.of(input);
            String[] extension = input.split("\\.", 2);
            extension[1] = "."+ extension[1];
            byte[] extensionBytes = extension[1].getBytes(StandardCharsets.UTF_8);
            //tamany de l'extensió
            dos.writeShort(extensionBytes.length);
            //l'extensió en sí
            dos.write(extensionBytes);

            dos.writeInt(totalUnicSymbols);
            // llista de (simbol, longitud)
            for (int symbol = 0; symbol < Huffman.BITSIZE; symbol++) {
                int len = codeLengths[symbol];
                if (len > 0) {
                    dos.writeByte(symbol);
                    dos.writeByte(len);
                }
            }

            int originalBytes = (int) Files.size(inputPath);
            dos.writeInt(originalBytes);
            dos.flush();

            //Escriure la codificació del contingut de l'arxiu d'entrada
            try (InputStream fis = new BufferedInputStream(Files.newInputStream(inputPath))) {
                int b;
                while ((b = fis.read()) != -1) {
                    byte[] codeBits = canonCodes[b & 0xFF];
                    for (byte codeBit : codeBits) {
                        bitOut.writeBit(codeBit == 1);
                    }
                }
            }
            bitOut.flush();
        }
    }

}
