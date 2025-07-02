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

    private final int DIAMETRE_NODE = 30;
    private final int ESPAI_VERTICAL = 60;
    private final int AMPLADA_BASE = 100;
    private final int GAP_H = 100;
    private final int MARGIN_X = 100;
    private final int MARGIN_Y = 100;
    private Decompressor.DecodeNode arrel;
    private String src;

    private Map<Decompressor.DecodeNode, Integer> contadorNodes = new HashMap<>();
    private Map<Decompressor.DecodeNode, Integer> posX = new HashMap<>();
    private int contador;

    public VistaArbreHuffman(String src) {
        this.src = src;
        setArrel();
        setBackground(Color.WHITE);
    }

    /**
     * Retorna la mida preferida del panell segons la mida de l'arbre.
     *
     * @return dimensió preferida del panell
     */
    @Override
    public Dimension getPreferredSize() {
        if (arrel == null) {
            return new Dimension(AMPLADA_BASE, ESPAI_VERTICAL * 2);
        }
        contadorNodes.clear();
        contarFulles(arrel);
        int totalHojas = contadorNodes.getOrDefault(arrel, 1);

        int width = MARGIN_X * 2 + totalHojas * GAP_H;
        int profundidad = calcularProfunditat(arrel);
        int height = MARGIN_Y * 2 + profundidad * ESPAI_VERTICAL;

        return new Dimension(width, height);
    }

    /**
     * Calcula la profunditat de l'arbre a partir d’un node.
     *
     * @param node node arrel
     * @return profunditat màxima
     */
    private int calcularProfunditat(Decompressor.DecodeNode node) {
        if (node == null) return 0;
        return 1 + Math.max(
                calcularProfunditat(node.getLeft()),
                calcularProfunditat(node.getRight())
        );
    }

    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        if (arrel == null) return;

        Graphics2D g2 = (Graphics2D) gg;
        g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);

        contadorNodes.clear();
        contador = 0;
        contarFulles(arrel);

        //calcualr posicions horizontals
        posX.clear();
        calcularX(arrel);
        pintarArbre(g2, arrel, 0, "");
    }

    /**
     * Compta el nombre de fulles a partir d’un node donat
     * i guarda el nombre en el map contadorNodes.
     *
     * @param node node actual
     * @return nombre de fulles descendents
     */
    private int contarFulles(Decompressor.DecodeNode node) {
        if (node == null) return 0;
        if (node.isLeaf()) {
            contadorNodes.put(node, 1);
            return 1;
        }
        int esquerra = contarFulles(node.getLeft());
        int dreta = contarFulles(node.getRight());
        contadorNodes.put(node, esquerra + dreta);
        return esquerra + dreta;
    }

    /**
     * Calcula la posició X dels nodes en l’arbre (ordenació horitzontal).
     *
     * @param node node actual
     */
    private void calcularX(Decompressor.DecodeNode node) {
        if (node == null) return;

        if (node.isLeaf()) {
            posX.put(node, contador++);
        } else {
            calcularX(node.getLeft());
            calcularX(node.getRight());
            int xIzq = posX.get(node.getLeft());
            int xDer = posX.get(node.getRight());
            posX.put(node, (xIzq + xDer) / 2);
        }
    }

    /**
     * Pinta l’arbre recursivament a partir d’un node.
     * Mostra les connexions, els cercles dels nodes i les etiquetes.
     *
     * @param g     graphics2D
     * @param node  node actual
     * @param nivel profunditat actual
     * @param path  cadena binària del camí recorregut
     */
    private void pintarArbre(Graphics2D g, Decompressor.DecodeNode node, int nivel, String path) {
        if (node == null) return;

        int idx = posX.get(node);
        int x = MARGIN_X + idx * GAP_H;
        int y = MARGIN_Y + nivel * ESPAI_VERTICAL;

        if (node.getLeft() != null) {
            int idxL = posX.get(node.getLeft());
            int childX = MARGIN_X + idxL * GAP_H;
            int childY = MARGIN_Y + (nivel + 1) * ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getLeft(), nivel + 1, path + "1");
        }
        if (node.getRight() != null) {
            int idxR = posX.get(node.getRight());
            int childX = MARGIN_X + idxR * GAP_H;
            int childY = MARGIN_Y + (nivel + 1) * ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getRight(), nivel + 1, path + "0");
        }

        //dibuixar el node
        g.setColor(Color.CYAN);
        g.fillOval(x - DIAMETRE_NODE / 2, y - DIAMETRE_NODE / 2, DIAMETRE_NODE, DIAMETRE_NODE);
        g.setColor(Color.BLACK);
        g.drawOval(x - DIAMETRE_NODE / 2, y - DIAMETRE_NODE / 2, DIAMETRE_NODE, DIAMETRE_NODE);

        // Etiqueta: simbol(si és fulla) i la seva codificació
        String label = node.isLeaf()
                ? String.format("%s (%s)", (char) node.getSymbol(), path)
                : path;
        g.drawString(label, x - DIAMETRE_NODE / 2, y - DIAMETRE_NODE);
    }

    /**
     * Construeix l’arbre de descompressió de Huffman a partir
     * del header del fitxer especificat. Actualitza el valor d'arrel
     */
    private void setArrel() {
        Path srcPath = Path.of(src);
        Decompressor d = new Decompressor();
        try (
                InputStream fis = new BufferedInputStream(Files.newInputStream(srcPath));
                DataInputStream dis = new DataInputStream(fis)
        ) {
            HuffHeader h = HuffHeader.read(dis);
            if (!Arrays.equals(Objects.requireNonNull(h).magicN, Dades.magicNumbers)) {
                System.err.println("Not a valid file");
                return;
            }
            List<Long> symbolList = new ArrayList<>(h.codeLengths.keySet());
            symbolList.sort(Long::compareUnsigned);
            Map<Long, byte[]> canonCodes = Huffman.generateCanonicalCodes(h.codeLengths, symbolList);
            arrel = d.buildDecodingTree(canonCodes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
