
package model.Huffman;

import model.BitsManagement.BitInputStream;
import model.Dades;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Decompressor extends Proces {

    private  String src;
    private  String outputFolder;

    private final ByteArrayOutputStream[] fileChunks = new ByteArrayOutputStream[N_THREADS];

    public Decompressor(int id, String src, String outputFolder) {
        super(id);
        this.src = src;
        this.outputFolder = outputFolder;
    }

    public Decompressor() {
        super(1);
    }


    @Override
    protected void exec() {
        try {
            decompressFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class DecodeNode {
        DecodeNode left, right;
        long symbol;

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public long getSymbol() {
            return symbol;
        }

        public void setSymbol(long symbol) {
            this.symbol = symbol;
        }

        public DecodeNode getRight() {
            return right;
        }

        public void setRight(DecodeNode right) {
            this.right = right;
        }

        public DecodeNode getLeft() {
            return left;
        }

        public void setLeft(DecodeNode left) {
            this.left = left;
        }
    }

    public void decompressFile() throws IOException {
        long time = System.nanoTime();

        Path srcPath = Path.of(src);
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(srcPath));
             DataInputStream dis = new DataInputStream(fis)) {

            HuffHeader h = HuffHeader.read(dis);

            if (!Arrays.equals(Objects.requireNonNull(h).magicN,Dades.magicNumbers)) {
                System.err.println("Not a valid file");
                //comunicar, etc, etc
                return;
            }


            List<Long> symbolList = new ArrayList<>(h.codeLengths.keySet());
            symbolList.sort(Long::compareUnsigned);
            Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(h.codeLengths, symbolList);
            DecodeNode root = buildDecodingTree(canonCodes);


            File inputFile = new File(src);
            String fileName = inputFile.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));

            System.out.println("fileName = " + fileName);
            String outputFile = outputFolder+"/"+ fileName+ "."+ new String(h.originalExtension);

            try (OutputStream fosOut = new BufferedOutputStream(
                    new FileOutputStream(outputFile))) {
                byte[] bytes = fis.readAllBytes();
                int lastIni = 0;
                for (int i = 0; i < h.bitTamChunks.length; i++) {
                    if(h.bitTamChunks[i] == 0){
                        continue;
                    }
                    int byteIni = lastIni;
                    int finalI = i;
                    executar(() -> {
                        decode(finalI, bytes, byteIni, root, h.bitTamChunks[finalI], h.byteSize);
                    });
                    lastIni = (h.bitTamChunks[i] >> 3) + byteIni + ((h.bitTamChunks[i] % 8) == 0 ? 0 : 1) ;
                }
                waitAll();

                long remainWrite = h.originalBytes;
                for(ByteArrayOutputStream fileChunk : fileChunks) {
                    if(fileChunk != null){
                        remainWrite -= fileChunk.size();
                        if(remainWrite < 0){
                            byte[] chunk = fileChunk.toByteArray();
                            fosOut.write(chunk, 0, (int) (chunk.length + remainWrite));
                        }else {
                            fileChunk.writeTo(fosOut);
                        }
                    }
                }

                fosOut.flush();
            }
            time = System.nanoTime()-time;
            Dades.Informacio info = new Dades.Informacio(Files.size(Path.of(outputFile)),Files.size(srcPath),h.uniqueSymbols);
            info.setTempsDecompressio(time);
            dades.setInfo(info);
        }
    }

    private void decode(int id, byte[] arr, int ini, DecodeNode root, int bitCap, int byteSize) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try(ByteArrayInputStream bais = new ByteArrayInputStream(arr);
            BitInputStream bis = new BitInputStream(bais)) {
            bais.skip(ini);
            int read = 0;
            while (read < bitCap) {
                DecodeNode node = root;
                while (!node.isLeaf() && read < bitCap) {
                    boolean bit = bis.readBit();
                    read++;
                    node = bit ? node.right : node.left;
                }

                long aux = node.symbol;
                for (int i = 0; i < byteSize; i++) {
                    int bits = (int) ((aux & (0xFFL << (8 * (byteSize - i - 1)))) >> (8 * (byteSize - i - 1)));
                    bos.write(bits);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            fileChunks[id] = bos;
        }
    }


    public DecodeNode buildDecodingTree(Map<Long, byte[]> codes) {
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