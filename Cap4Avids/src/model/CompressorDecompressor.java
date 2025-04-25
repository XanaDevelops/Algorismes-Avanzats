
package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CompressorDecompressor {
    private final Huffman huffman;
    private final Path inputPath;
    private final Path outputPath;

    public CompressorDecompressor(Huffman huffman, String inputPath, String outputPath) {
        this.huffman = huffman;
        this.inputPath = Path.of(inputPath);
        this.outputPath = Path.of(outputPath);
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
        byte[][] canonCodes = generateCanonicalCodes(codeLengths, symbols);

        try (OutputStream fos = Files.newOutputStream(outputPath);
             BufferedOutputStream bufOut = new BufferedOutputStream(fos);
             DataOutputStream dos = new DataOutputStream(bufOut);
             BitOutputStream bitOut = new BitOutputStream(bufOut)) {


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

    private byte[][] generateCanonicalCodes(int[] lengths, List<Integer> symbolList) {

        //primer ordenar segons la longitud del símbol, i després en ordre lexicografic
        symbolList.sort(Comparator
                .comparingInt((Integer a) -> lengths[a])
                .thenComparingInt(a -> a));

        byte[][] codes = new byte[Huffman.BITSIZE][];

        int code = 0, prevLen = 0;
        //assigna codis canònics a cada símbol en funció de l'ordre establert
        for (int sym : symbolList) {
            int len = lengths[sym];
            //si la longitud aumenta, es desplaça a l'esquerre el valor de code
            code <<= (len - prevLen);

            byte[] bits = new byte[len];
            //afegir padding
            for (int i = 0; i < len; i++) {
                bits[i] = (byte) ((code >> (len - 1 - i)) & 1);

            }
            //guardar el codi canònic
            codes[sym] = bits;
            code++;
            prevLen = len;
        }
        return codes;
    }


    public void decompressFile(String src, String dest) throws IOException {
        Path srcPath = Path.of(src);
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(srcPath));
             DataInputStream dis = new DataInputStream(fis)) {

            int totalUnicSymbols = dis.readInt();
            int[] codeLengths = new int[Huffman.BITSIZE];

            List<Integer> symbols = new ArrayList<>();
            for (int i = 0; i < totalUnicSymbols; i++) {
                int sym = dis.readUnsignedByte();
                int len = dis.readUnsignedByte();
                codeLengths[sym] = len;
                symbols.add(sym);
            }

            int originalBytes = dis.readInt();

            byte[][] canonCodes = generateCanonicalCodes(codeLengths, symbols);
            DecodeNode root = buildDecodingTree(canonCodes);

            try (BitInputStream bitIn = new BitInputStream(fis);
                 OutputStream fosOut = new BufferedOutputStream(
                         new FileOutputStream(dest))) {
                int written = 0;
                while (written < originalBytes) {
                    DecodeNode node = root;
                    while (!node.isLeaf()) {
                        boolean bit = bitIn.readBit();
                        node = bit ? node.right : node.left;
                    }
                    fosOut.write(node.symbol & 0xFF);
                    written++;
                }
            }

        }
    }


    private DecodeNode buildDecodingTree(byte[][] codes) {
        DecodeNode root = new DecodeNode();
        for (int sym = 0; sym < Huffman.BITSIZE; sym++) {
            byte[] code = codes[sym];
            if (code == null) continue;
            DecodeNode node = root;
            for (byte c : code) {
                if (c == 0) {
                    if (node.left == null) node.left = new DecodeNode();
                    node = node.left;
                } else {
                    if (node.right == null) node.right = new DecodeNode();
                    node = node.right;
                }
            }
            node.symbol = (byte) sym;
        }
        return root;
    }

    private static class DecodeNode {
        DecodeNode left, right;
        byte symbol;

        boolean isLeaf() {
            return left == null && right == null;
        }
    }


}
