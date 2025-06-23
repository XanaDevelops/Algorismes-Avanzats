//package model;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Decompressor {
//
//    private final String src;
//    private final String folderOutput;
//    public Decompressor(String src, String folderOutput) {
//        this.src = src;
//        this.folderOutput = folderOutput;
//    }
//
//    private static class DecodeNode {
//        DecodeNode left, right;
//        byte symbol;
//
//        boolean isLeaf() {
//            return left == null && right == null;
//        }
//    }
//
//    public void decompressFile() throws IOException {
//        Path srcPath = Path.of(src);
//        try (InputStream fis = new BufferedInputStream(Files.newInputStream(srcPath));
//             DataInputStream dis = new DataInputStream(fis)) {
////            short extensioComrpimitLength = dis.readShort();
////            byte[] extensionBytesComprimit = new byte[extensioComrpimitLength];
////
////            dis.readFully(extensionBytesComprimit);
////            if (Extensio.notValid(extensionBytesComprimit)) {
////                System.out.println("Extension not supported");
////            }
////
////            short extensionLength = dis.readShort();
////            byte[] extensionBytes = new byte[extensionLength];
////            dis.readFully(extensionBytes);
////
////            String extension = new String(extensionBytes);
//            int totalUnicSymbols = dis.readInt();
//            int[] codeLengths = new int[Huffman.BITSIZE];
//
//            List<Integer> symbols = new ArrayList<>();
//            for (int i = 0; i < totalUnicSymbols; i++) {
//                int sym = dis.readUnsignedByte();
//                int len = dis.readUnsignedByte();
//                codeLengths[sym] = len;
//                symbols.add(sym);
//            }
//
//            int originalBytes = dis.readInt();
//
//            byte[][] canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);
//            DecodeNode root = buildDecodingTree(canonCodes);
//
//            String fileName = src.split("/")[src.split("/").length - 1];
//            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
//            fileName = fileName.split(" ", 2)[1];
//            System.out.println("fileName = " + fileName);
//
//            try (BitInputStream bitIn = new BitInputStream(fis);
//                 OutputStream fosOut = new BufferedOutputStream(
//                         new FileOutputStream(folderOutput+"Decompressed"+fileName+ ".txt"))) {
//                int written = 0;
//                while (written < originalBytes) {
//                    DecodeNode node = root;
//                    while (!node.isLeaf()) {
//                        boolean bit = bitIn.readBit();
//                        node = bit ? node.right : node.left;
//                    }
//                    fosOut.write(node.symbol & 0xFF);
//                    written++;
//                }
//            }
//
//        }
//    }
//
//
//    private DecodeNode buildDecodingTree(byte[][] codes) {
//        DecodeNode root = new DecodeNode();
//        for (int sym = 0; sym < Huffman.BITSIZE; sym++) {
//            byte[] code = codes[sym];
//            if (code == null) continue;
//            DecodeNode node = root;
//            for (byte c : code) {
//                if (c == 0) {
//                    if (node.left == null) node.left = new DecodeNode();
//                    node = node.left;
//                } else {
//                    if (node.right == null) node.right = new DecodeNode();
//                    node = node.right;
//                }
//            }
//            node.symbol = (byte) sym;
//        }
//        return root;
//    }
//}

package model.Huffman;

import control.Comunicar;
import model.BitsManagement.BitInputStream;
import model.Dades;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Decompressor implements Runnable, Comunicar {

    private final String src;
    private final String outputFolder;

    private int id;
    private boolean aturar;

    public Decompressor(int id, String src, String outputFolder) {
        this.id = id;
        this.src = src;
        this.outputFolder = outputFolder;
    }

    @Override
    public void comunicar(String s) {

    }

    @Override
    public void run() {

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
            byte[] extensionBytesComprimit = new byte[Dades.magicNumbers.length];

            dis.readFully(extensionBytesComprimit);
            System.out.println(Arrays.toString(extensionBytesComprimit));

            if (!Arrays.equals(extensionBytesComprimit,Dades.magicNumbers)) {
                System.err.println("Not a valid file");
                //comunicar, etc, etc
                return;
            }

            short tamWords = dis.readShort();

            short length = dis.readShort();
            byte[] extensionBytes = new byte[length];
            dis.readFully(extensionBytes);



            String extension = new String(extensionBytes);
            int totalUnicSymbols = dis.readInt();
            Map<Long, Integer> codeLengths = new TreeMap<>();
            List<Long> symbols = new ArrayList<>();

            for (int i = 0; i < totalUnicSymbols; i++) {
                long sym = dis.readLong();
                int len = dis.readInt();
                codeLengths.put(sym, len);
                symbols.add(sym);
            }

            long originalBytes = dis.readLong();

            Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(codeLengths, symbols);
            DecodeNode root = buildDecodingTree(canonCodes);
            String fileName = src.split("/")[src.split("/").length - 1];
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
            System.out.println("fileName = " + fileName);
            try (BitInputStream bitIn = new BitInputStream(fis);
                 OutputStream fosOut = new BufferedOutputStream(
                         new FileOutputStream(outputFolder+ fileName+ "."+ extension))) {
                int written = 0;
                while (written < originalBytes) {
                    DecodeNode node = root;
                    while (!node.isLeaf()) {
                        boolean bit = bitIn.readBit();
                        node = bit ? node.right : node.left;
                    }
                    //fosOut.write(node.symbol & 0xFF);
                    //written++;
                    long aux = node.symbol;
                    for(int j = 0; j < tamWords && written < originalBytes; j++){
                        int bits = (int) ((aux & (0xFFL << (8*(tamWords-j-1)))) >> (8* (tamWords-j-1)));
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