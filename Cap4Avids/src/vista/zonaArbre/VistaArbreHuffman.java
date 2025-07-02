package vista.zonaArbre;

import model.Dades;
import model.Huffman.Decompressor;
import model.Huffman.HuffHeader;
import model.Huffman.Huffman;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class VistaArbreHuffman extends JPanel {

    //    private final Huffman.Node arrel;
    private   Decompressor.DecodeNode arrel;
    private final int NODE_DIAMETRE = 30;
    private final int ESPAI_VERTICAL = 50;
    private final int AMPLADA_BASE = 1200;
    private  String src;

    public VistaArbreHuffman(Decompressor.DecodeNode arrel) {
        this.arrel = arrel;
        setBackground(Color.WHITE);
    }

    public VistaArbreHuffman(String src) {
        this.arrel = null;
        this.src = src;
        setArrel();
        setBackground(Color.WHITE);
    }


    @Override
    public Dimension getPreferredSize() {
        int profunditat = calcularProfunditat(arrel);
        int alçada = profunditat * ESPAI_VERTICAL + 100;
        int amplada = Math.max(AMPLADA_BASE, (int) Math.pow(2, profunditat) * NODE_DIAMETRE);
        return new Dimension(amplada, alçada);
    }

    private int calcularProfunditat(Decompressor.DecodeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(calcularProfunditat(node.getLeft()), calcularProfunditat(node.getRight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (arrel != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getPreferredSize().width;
//            pintarArbre(g2, arrel, width / 2, 40, width / 4);
            pintarArbre(g2, arrel, width / 2, 40, width / 6,"0");
        }
    }

    private void pintarArbre(Graphics2D g, Decompressor.DecodeNode node, int x, int y, int separacioX, String path) {
        if (node == null) return;



        String label = node.isLeaf()
                ? String.format("%d (%s)", node.getSymbol(), path)
                : String.valueOf(path);

        g.setColor(Color.CYAN);
        g.fillOval(x - NODE_DIAMETRE / 2, y - NODE_DIAMETRE / 2, NODE_DIAMETRE, NODE_DIAMETRE);
        g.setColor(Color.BLACK);
        g.drawOval(x - NODE_DIAMETRE / 2, y - NODE_DIAMETRE / 2, NODE_DIAMETRE, NODE_DIAMETRE);
        g.drawString(label, x - NODE_DIAMETRE / 2, y - NODE_DIAMETRE);

        if (node.getLeft() != null) {
            int childX = x - separacioX;
            int childY = y + ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getLeft(), childX, childY, separacioX / 2, path+"1");
        }

        if (node.getRight() != null) {
            int childX = x + separacioX;
            int childY = y + ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getRight(), childX, childY, separacioX / 2, path+"0");
        }
    }

    private void setArrel() {
        Path srcPath = Path.of(src);
        Decompressor d = new Decompressor();
        try (InputStream fis = new BufferedInputStream(Files.newInputStream(srcPath));
             DataInputStream dis = new DataInputStream(fis)) {

            HuffHeader h = HuffHeader.read(dis);

            if (!Arrays.equals(Objects.requireNonNull(h).magicN, Dades.magicNumbers)) {
                System.err.println("Not a valid file");
                //comunicar, etc, etc
                return;
            }

            List<Long> symbolList = new ArrayList<>(h.codeLengths.keySet());
            symbolList.sort(Long::compareUnsigned);
            Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(h.codeLengths, symbolList);
            this.arrel = d.buildDecodingTree(canonCodes);

//            Map<Long, String> huffmanCodes = new HashMap<>();
//            generateHuffmanCodification(root, "",huffmanCodes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void generateHuffmanCodification(Decompressor.DecodeNode node, String path, Map<Long, String> huffmanCodes) {
        {
            if (node == null) return;

            // node fulla : assignar-li la seva codificació
            if (node.isLeaf()) {
                huffmanCodes.put(node.getSymbol(), path);
                return;
            }

            generateHuffmanCodification(node.getLeft(), path + "0", huffmanCodes);
            generateHuffmanCodification(node.getRight(), path + "1", huffmanCodes);
        }

    }
}
