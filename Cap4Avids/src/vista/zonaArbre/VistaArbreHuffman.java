package vista.zonaArbre;

import model.Dades;
import model.Huffman.Decompressor;
import model.Huffman.HuffHeader;
import model.Huffman.Huffman;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class VistaArbreHuffman extends JPanel {

    private Decompressor.DecodeNode arrel;
    private final int NODE_DIAMETRE = 30;
    private final int ESPAI_VERTICAL = 60;
    private final int AMPLADA_BASE = 100;
    // Espaciamiento y márgenes para la nueva distribución
        private final int GAP_H = 100;
    private final int MARGIN_X = 100;
    private final int MARGIN_Y = 100;

    private String src;

    // Estructuras auxiliares para contar hojas y asignar X
    private Map<Decompressor.DecodeNode, Integer> hojaCount = new HashMap<>();
    private Map<Decompressor.DecodeNode, Integer> posX = new HashMap<>();
    private int contador;

    public VistaArbreHuffman(String src) {
        this.src = src;
        setArrel();
        setBackground(Color.WHITE);
    }

    @Override
    public Dimension getPreferredSize() {
        if (arrel == null) {
            return new Dimension(AMPLADA_BASE, ESPAI_VERTICAL * 2);
        }

        // Recalcular hojaCount para obtener total de hojas
        hojaCount.clear();
        contarHojas(arrel);
        int totalHojas = hojaCount.getOrDefault(arrel, 1);

        int width  = MARGIN_X * 2 + totalHojas * GAP_H;
        int profundidad = calcularProfunditat(arrel);
        int height = MARGIN_Y * 2 + profundidad * ESPAI_VERTICAL;

        return new Dimension(width, height);
    }

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
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Fase 1: contar hojas
        hojaCount.clear();
        contador = 0;
        contarHojas(arrel);

        // Fase 2: calcular posición X
        posX.clear();
        calcularX(arrel);

        // Pintar árbol desde nivel 0 y ruta vacía
        pintarArbre(g2, arrel, 0, "");
    }

    // Fase 1: contar hojas de cada subárbol
    private int contarHojas(Decompressor.DecodeNode node) {
        if (node == null) return 0;
        if (node.isLeaf()) {
            hojaCount.put(node, 1);
            return 1;
        }
        int izq = contarHojas(node.getLeft());
        int der = contarHojas(node.getRight());
        hojaCount.put(node, izq + der);
        return izq + der;
    }

    // Fase 2: asignar X mediante recorrido in-order
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

    // Pintado basado en posX y nivel
    private void pintarArbre(Graphics2D g,
                             Decompressor.DecodeNode node,
                             int nivel,
                             String path) {
        if (node == null) return;

        int idx = posX.get(node);
        int x   = MARGIN_X + idx * GAP_H;
        int y   = MARGIN_Y + nivel * ESPAI_VERTICAL;

        if (node.getLeft() != null) {
            int idxL   = posX.get(node.getLeft());
            int childX = MARGIN_X + idxL * GAP_H;
            int childY = MARGIN_Y + (nivel + 1) * ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getLeft(), nivel + 1, path + "1");
        }
        if (node.getRight() != null) {
            int idxR   = posX.get(node.getRight());
            int childX = MARGIN_X + idxR * GAP_H;
            int childY = MARGIN_Y + (nivel + 1) * ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getRight(), nivel + 1, path + "0");
        }

        // Dibujar nodo
        g.setColor(Color.CYAN);
        g.fillOval(
                x - NODE_DIAMETRE/2, y - NODE_DIAMETRE/2,
                NODE_DIAMETRE, NODE_DIAMETRE
        );
        g.setColor(Color.BLACK);
        g.drawOval(
                x - NODE_DIAMETRE/2, y - NODE_DIAMETRE/2,
                NODE_DIAMETRE, NODE_DIAMETRE
        );

        // Etiqueta: símbolo y ruta si es hoja
        String label = node.isLeaf()
                ? String.format("%s (%s)", (char) node.getSymbol(), path)
                : path;
        g.drawString(label, x - NODE_DIAMETRE/2, y - NODE_DIAMETRE);
    }

    // Lectura de archivo y construcción del árbol
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
