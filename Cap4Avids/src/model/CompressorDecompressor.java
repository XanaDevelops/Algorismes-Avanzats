

package model;

import java.io.*;
import java.util.*;

import model.Huffman.Node;

public class CompressorDecompressor {

    private Huffman huffman;
    private  String inputPath;
    private String outputPath;

    public CompressorDecompressor(Huffman huffman, String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.huffman = huffman;
    }

    /**
     * Recorre el text original i assigna la codificació a cada byte
     * segons l'arbre de Huffman.
     */
    public List<Object> getHuffmanCodification(boolean esSeriablitzable) throws IOException {
        Map<Byte, String > taula = huffman.getTable();

        List<Object> returns = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int totalBytes = 0;
        int b;
        if (esSeriablitzable) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputPath));

            while( (b = ois.read())!=-1){
                totalBytes += 1;
                sb.append(taula.get((byte)b));
            }
            returns.add(totalBytes);
            returns.add(sb);

            ois.close();
        }else{
            FileInputStream fis = new FileInputStream(inputPath);
            while((b = fis.read())!=-1){
                sb.append(taula.get((byte)b));
                totalBytes += 1;
            }
            returns.add(totalBytes);
            returns.add(sb);
            fis.close();

        }

        return returns;


    }



    public void compressFile() throws IOException {
        Node root = huffman.getTree();

        byte[] header = compressHuffmanTree(root);
        int headerLen       = header.length;
        List<Object> returns = getHuffmanCodification(false);

        int originalLength = (int) returns.getFirst();
        StringBuilder sb = (StringBuilder) returns.getLast();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < sb.length(); i+=8) {
            StringBuilder current = new StringBuilder(sb.substring(i, Math.min(i + Byte.SIZE, sb.length())));
            while(current.length() < Byte.SIZE){
                current.append("0");
            }


            baos.write((byte) Integer.parseInt(current.toString(), 2));
        }
        byte[] compressedData = baos.toByteArray();


        int dataLen         = compressedData.length;
        int totalCompressed = headerLen + dataLen;
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputPath));
        //additional unsigned numbers
        dos.writeInt(totalCompressed);
        dos.writeInt(headerLen);
        dos.writeInt(originalLength);
        //compressed tree
        dos.write(header);
        //compressed data
        dos.write(compressedData);

    }

    private byte[] compressHuffmanTree(Node root) {
        int totalBits = countBits(root) + 1;       // +1 for terminator 0
        BitSet bitSet = new BitSet(totalBits);
        int[] cursor = {0};

        packTreeRecursivuly(root, bitSet, cursor);
        bitSet.set(cursor[0]++, false);            // terminator (bit a 0)

        int byteLen = (totalBits + 7) / 8;
        byte[] out = new byte[byteLen];
        for (int i = 0; i < totalBits; i++) {
            if (bitSet.get(i)) {
                int bIndex = i / 8;
                int shift  = 7 - (i % 8);
                out[bIndex] |= (byte) (1 << shift);
            }
        }
        return out;
    }

    private static int countBits(Node node) {
        if (node.isLeaf()) {
            return 1 + 8;//1 for terminator
        } else {
            return countBits(node.getLeft())
                    + countBits(node.getRight())
                    + 1; //current
        }
    }

    private void packTreeRecursivuly(Node current, BitSet bits, int[]cursor){

        if (current.isLeaf()) {
            bits.set(cursor[0]++, true);           // bit 1
            byte b = (byte) current.bval;
            for (int i = 7; i >= 0; i--) {
                bits.set(cursor[0]++, ((b >> i) & 1) == 1);
            }
        } else {
            packTreeRecursivuly(current.getLeft(),  bits, cursor);
            packTreeRecursivuly(current.getRight(), bits, cursor);
            bits.set(cursor[0]++, false);          // bit 0
        }
    }

    public  void decompressFile(String compressdPath, String destPath) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(compressdPath))) {

            int totalCompressed = (int)Integer.toUnsignedLong(dis.readInt());
            int headerLen       = (int)Integer.toUnsignedLong(dis.readInt());
            int originalLen     = (int)Integer.toUnsignedLong(dis.readInt());
            System.out.println("total compressed: " + totalCompressed);
            System.out.println("header len: " + headerLen);
            System.out.println("original len: " + originalLen);
            // 1) Reconstruir headerBits
            byte[] headerBytes = new byte[headerLen];
            dis.readFully(headerBytes);
            BitSet headerBits = bytesToBitSet(headerBytes, headerLen * 8);

            // 2) Desempacar árbol
            Node treeRoot = unpackTreeFromPostorder(headerBits);

            // 3) Leer datos comprimidos
            int dataLen       = totalCompressed - headerLen;
            byte[] dataBytes  = new byte[dataLen];
            dis.readFully(dataBytes);
            BitSet dataBits   = bytesToBitSet(dataBytes, dataLen * 8);

            // 4) Recorrer bits para decodificar
            try (FileOutputStream fos = new FileOutputStream(destPath)) {
                int written = 0, bitPos = 0;
                while (written < originalLen) {
                    Node curr = treeRoot;
                    while (!curr.isLeaf()) {
                        boolean b = dataBits.get(bitPos++);
                        curr = b ? curr.getRight() : curr.getLeft();
                    }
                    fos.write(curr.bval);
                    written++;
                }
            }
        }
    }

    /** Convierte un array de bytes a un BitSet (MSB-first). */
    private static BitSet bytesToBitSet(byte[] buf, int totalBits) {
        BitSet bits = new BitSet(totalBits);
        for (int i = 0; i < totalBits; i++) {
            if ((buf[i/8] & (1 << (7 - (i%8)))) != 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    /** Reconstruye el árbol leyendo bits de un BitSet en post‑order. */
    private static Node unpackTreeFromPostorder(BitSet bits) {
        Deque<Node> stack = new ArrayDeque<>();
        int pos = 0;
        while (true) {
            boolean bit = bits.get(pos++);
            if (bit) {
                // hoja
                int b = 0;
                for (int j = 0; j < 8; j++, pos++) {
                    if (bits.get(pos)) b |= (1 << (7-j));
                }
                stack.push(new Node(0, b));
            } else {
                // interno o terminador
                if (stack.size() >= 2) {
                    Node right = stack.pop();
                    Node left  = stack.pop();
                    Node p = new Node(0, Integer.MIN_VALUE);
                    p.setLeft(left);
                    p.setRight(right);
                    stack.push(p);
                } else {
                    // terminador
                    break;
                }
            }
        }
        return stack.pop();
    }


}
