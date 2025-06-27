
package model.Huffman;

import model.BitsManagement.BitInputStream;
import model.Dades;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Decompressor extends Proces {

    private final String src;
    private final String outputFolder;

    public Decompressor(int id, String src, String outputFolder) {
        super(id);
        this.src = src;
        this.outputFolder = outputFolder;
    }



    @Override
    protected void exec() {
        try {
            decompressFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class DecodeNode {
        DecodeNode left, right;
        long symbol;

        boolean isLeaf() {
            return left == null && right == null;
        }
    }

    public void decompressFile() throws IOException {
        Path srcPath = Path.of(src);
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(srcPath));
             DataInputStream dis = new DataInputStream(fis)) {

            HuffHeader h = HuffHeader.read(dis);
//            byte[] extensionBytesComprimit = new byte[Dades.magicNumbers.length];

//            dis.readFully(extensionBytesComprimit);
//            System.out.println(Arrays.toString(extensionBytesComprimit));

            if (!Arrays.equals(Objects.requireNonNull(h).magicN,Dades.magicNumbers)) {
                System.err.println("Not a valid file");
                //comunicar, etc, etc
                return;
            }

//            short tamWords = dis.readShort();
//
//            short length = dis.readShort();
//            byte[] extensionBytes = new byte[length];
//            dis.readFully(extensionBytes);
//
//
//
//            String extension = new String(extensionBytes);
//            int totalUnicSymbols = dis.readInt();
//            Map<Long, Integer> codeLengths = new TreeMap<>();
//            List<Long> symbols = new ArrayList<>();
//
//            for (int i = 0; i < totalUnicSymbols; i++) {
//                long sym = dis.readLong();
//                int len = dis.readInt();
//                codeLengths.put(sym, len);
//                symbols.add(sym);
//            }

//            long originalBytes = dis.readLong();
            List<Long> symbolList = new ArrayList<>(h.codeLengths.keySet());

            Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(h.codeLengths, symbolList);
            DecodeNode root = buildDecodingTree(canonCodes);
            String fileName = src.split("/")[src.split("/").length - 1];
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            System.out.println("fileName = " + fileName);
            try (BitInputStream bitIn = new BitInputStream(fis);
                 OutputStream fosOut = new BufferedOutputStream(
                         new FileOutputStream(outputFolder+"/"+ fileName+ "."+ new String(h.originalExtension)))) {
                int written = 0;
                while (written < h.originalBytes) {
                    DecodeNode node = root;
                    while (!node.isLeaf()) {
                        boolean bit = bitIn.readBit();
                        node = bit ? node.right : node.left;
                    }
                    //fosOut.write(node.symbol & 0xFF);
                    //written++;
                    long aux = node.symbol;
                    for(int j = 0; j < h.byteSize && written < h.originalBytes; j++){
                        int bits = (int) ((aux & (0xFFL << (8*(h.byteSize-j-1)))) >> (8* (h.byteSize-j-1)));
                        fosOut.write(bits);
                        written++;
                    }
                    //DEBUG
                    fosOut.flush();
                }
            }
        }
    }


    private DecodeNode buildDecodingTree(Map<Long, byte[]> codes) {
        DecodeNode root = new DecodeNode();
        for (Map.Entry<Long, byte[]> entry : codes.entrySet()) {
            byte[] code = entry.getValue();
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
            node.symbol = entry.getKey();
        }
        return root;
    }

    @Override
    public void aturar(int id){
        aturar = true;
    }
}