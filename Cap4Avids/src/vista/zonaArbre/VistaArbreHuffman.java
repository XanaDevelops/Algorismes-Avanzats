package vista.zonaArbre;

import model.Huffman.Huffman;
import javax.swing.*;
import java.awt.*;

public class VistaArbreHuffman extends JPanel {

    private final Huffman.Node arrel;
    private final int NODE_DIAMETRE = 30;
    private final int ESPAI_VERTICAL = 50;
    private final int AMPLADA_BASE = 1200;

    public VistaArbreHuffman(Huffman.Node arrel) {
        this.arrel = arrel;
        setBackground(Color.WHITE);
    }

    @Override
    public Dimension getPreferredSize() {
        int profunditat = calcularProfunditat(arrel);
        int alçada = profunditat * ESPAI_VERTICAL + 100;
        int amplada = Math.max(AMPLADA_BASE, (int) Math.pow(2, profunditat) * NODE_DIAMETRE);
        return new Dimension(amplada, alçada);
    }

    private int calcularProfunditat(Huffman.Node node) {
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
            pintarArbre(g2, arrel, width / 2, 40, width / 4);
        }
    }

    private void pintarArbre(Graphics2D g, Huffman.Node node, int x, int y, int separacioX) {
        if (node == null) return;

        String label = node.isLeaf()
                ? String.format("%d (%c)", node.getVal(), (char) node.getByteVal())
                : String.valueOf(node.getVal());

        g.setColor(Color.CYAN);
        g.fillOval(x - NODE_DIAMETRE / 2, y - NODE_DIAMETRE / 2, NODE_DIAMETRE, NODE_DIAMETRE);
        g.setColor(Color.BLACK);
        g.drawOval(x - NODE_DIAMETRE / 2, y - NODE_DIAMETRE / 2, NODE_DIAMETRE, NODE_DIAMETRE);
        g.drawString(label, x - NODE_DIAMETRE / 2, y - NODE_DIAMETRE);

        if (node.getLeft() != null) {
            int childX = x - separacioX;
            int childY = y + ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getLeft(), childX, childY, separacioX / 2);
        }

        if (node.getRight() != null) {
            int childX = x + separacioX;
            int childY = y + ESPAI_VERTICAL;
            g.drawLine(x, y, childX, childY);
            pintarArbre(g, node.getRight(), childX, childY, separacioX / 2);
        }
    }
}
