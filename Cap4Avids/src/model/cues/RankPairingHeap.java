
package model.cues;

import java.util.*;

public class RankPairingHeap<E extends Comparable<E>> extends AbstractQueue<E> {

    private List<Node<E>> roots; // Root nodes of the heap
    private int size;

    public static class Node<E> {
        E key;
        List<Node<E>> children;
        int rank;

        public Node(E key) {
            this.key = key;
            this.children = new ArrayList<>();
            this.rank = 0;
        }

    }

    public RankPairingHeap() {
        super();
        this.roots = new ArrayList<>();
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
     * Adds e to the queue and consolidates the queue after the insertion
     *
     * @param e the element to add
     * @return true
     */
    @Override

    public boolean offer(E e) {
        Node<E> newNode = new Node<>(e);
        if (roots.isEmpty()) {
            roots.add(newNode);
        } else {

            roots.add(newNode);
            if (newNode.key.compareTo(roots.getFirst().key) < 0) {
                Collections.swap(roots, 0, roots.size() - 1);
            }
        }
        size++;
        consolidate();
        return true;
    }


    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of the queue (minimum value) or null if the queue is null
     */
    @Override
    public E poll() {
        if (roots.isEmpty()) return null;
        //the node to return
        Node<E> minNode = roots.getFirst();
        roots.remove(minNode);
        size--;
        // Merge children of minNode
        roots.addAll(minNode.children);

        consolidate();
        return minNode.key;
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of the queue, or null if the queue is empty
     */
    @Override
    public E peek() {
        return roots.isEmpty() ? null : roots.getFirst().key;
    }

    /**
     * Consolidates the roots by merging sub queues with the same rank
     *
     */
    private void consolidate() {
        if (roots.size() <= 1) return;

        Map<Integer, Node<E>> bucket = new HashMap<>();

        for (Node<E> node : roots) {
            int rank = node.rank;
            while (bucket.containsKey(rank)) {
                Node<E> other = bucket.get(rank);
                //links or merges two subtrees with the same rank
                node = link(node, other);
                bucket.remove(rank);
                rank++;
            }
            //add the new subtree merged
            bucket.put(rank, node);
        }
        //update the roots list
        roots = new ArrayList<>(bucket.values());
        moveMinToFront();
    }

    /**
     * Moves the minimum value to the first positions en the roots list
     */
    private void moveMinToFront() {
        if (roots.isEmpty()) return;
        int minIndex = 0;
        for (int i = 1; i < roots.size(); i++) {
            if (roots.get(i).key.compareTo(roots.get(minIndex).key) < 0) {
                minIndex = i;
            }
        }
        if (minIndex != 0) {
            Collections.swap(roots, 0, minIndex);
        }
    }


    /**
     * Links two given nodes.
     * If node1 < node2 --> node1 becomes the father of node2.
     *
     * @param node1
     * @param node2
     * @return  root of the merged nodes
     */
    private Node<E> link(Node<E> node1, Node<E> node2) {

        if (node1.key.compareTo(node2.key) <= 0) {
            node1.children.add(node2);
            node1.rank++;
            return node1;
        } else {
            node2.children.add(node1);
            node2.rank++;
            return node2;
        }
    }
}