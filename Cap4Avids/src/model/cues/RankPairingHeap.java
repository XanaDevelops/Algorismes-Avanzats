
package model.cues;

import java.util.*;

public class RankPairingHeap<E extends Comparable<E>> extends AbstractQueue<E> {

    private final List<Node<E>> roots;
    private int size;
    private Node<E> minRoot;

    public static class Node<E> {
        E key;
        Node<E> firstChild;
        Node<E> nextSibling;
        int rank;

        public Node(E key) {
            this.key = key;
            this.firstChild = null;
            this.nextSibling = null;
            this.rank = 0;
        }
    }

    public RankPairingHeap() {
        this.roots = new ArrayList<>();
        this.size = 0;
        this.minRoot = null;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.size;
    }

    /**
     * Afegeix l'element e a la cua. Actualitza el valor de minRoot si cal.
     *
     */
    @Override
    public boolean offer(E e) {
        Node<E> newNode = new Node<>(e);
        roots.add(newNode);
        if (minRoot == null || newNode.key.compareTo(minRoot.key) < 0) {
            minRoot = newNode;
        }
        size++;
        return true;
    }

    /**
     * Recupera i elimina l'arrel d'aquesta cua, o retorna null si aquesta cua està buida.
     *
     * @return la capçalera de la cua (valor mínim) o nul si la cua és nul·la
     */
    @Override
    public E poll() {
        if (minRoot == null) {
            return null;
        }
        roots.remove(minRoot);
        size--;

        // S'afegeixen els fills de minRoot a la llista de roots
        Node<E> child = minRoot.firstChild;
        while (child != null) {
            Node<E> next = child.nextSibling;
            child.nextSibling = null;
            roots.add(child);
            child = next;
        }
        E minKey = minRoot.key;
        consolidate();
        return minKey;
    }

    /**
     * Recupera, però no elimina, la capçalera d'aquesta cua o retorna null si aquesta cua està buida.
     *
     * @return el cap de la cua, o null si la cua està buida
     */
    @Override
    public E peek() {
        return (minRoot == null) ? null : minRoot.key;
    }

    /**
     * Consolida les arrels fusionant subcues amb el mateix rang, i actualitzant el minRoot.
     *
     */
    private void consolidate() {
        if (roots.size() <= 1) {
            minRoot = roots.isEmpty() ? null : roots.get(0);
            return;
        }
        // Calcular el rang màxim esperat (O(log n))
        int maxRank = (int) Math.ceil(Math.log(size + 1) / Math.log(2)) + 1;
        @SuppressWarnings("unchecked")
        Node<E>[] bucket = (Node<E>[]) new Node[maxRank];

        for (Node<E> node : roots) {
            node.nextSibling = null;
            int r = node.rank;
            while (bucket[r] != null) {
                Node<E> other = bucket[r];
                bucket[r] = null;
                node = link(node, other);
                r = node.rank;
            }
            bucket[r] = node;
        }
        roots.clear();
        minRoot = null;
        for (Node<E> node: bucket) {
            if (node != null) {
                roots.add(node);
                if (minRoot == null || node.key.compareTo(minRoot.key) < 0) {
                    minRoot = node;
                }
            }
        }
    }

    /**
     * Uneix dos nodes de rang igual.
     * El node amb la clau menor es converteix en arrel i l'altre
     * s'afegeix com a primer fill. S'actualitza el rang dels nodes.
     */

    private Node<E> link(Node<E> node1, Node<E> node2) {
        if (node1.key.compareTo(node2.key) <= 0) {
            node2.nextSibling = node1.firstChild;
            node1.firstChild = node2;
            node1.rank++;
            return node1;
        } else {
            node1.nextSibling = node2.firstChild;
            node2.firstChild = node1;
            node2.rank++;
            return node2;
        }
    }
}
