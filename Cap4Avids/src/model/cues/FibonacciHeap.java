package model.cues;

import java.util.*;

public class FibonacciHeap<E extends Comparable<E>> extends AbstractQueue<E> {

    private static class Node<E> {
        private final E elem;
        private int degree = 0; //num fills
        private boolean marked = false;

        private Node<E> parent = null;
        private Node<E> child = null;
        private Node<E> left = this;
        private Node<E> right = this;

        public Node(E elem) {
            this.elem = elem;
        }
    }

    private int size = 0;
    private Node<E> root = null;

    public FibonacciHeap() {
        super();
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }


    @Override
    public int size() {
        return size;
    }

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param e the element to add
     * @return {@code true} if the element was added to this queue, else
     * {@code false}
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this queue
     * @throws NullPointerException     if the specified element is null and
     *                                  this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *                                  prevents it from being added to this queue
     */
    @Override
    public boolean offer(E e) {
        Node<E> node = new Node<>(e);
        root = merge(root, node);
        size++;
        return true;
    }

    private Node<E> merge(Node<E> a, Node<E> b) {
        if (a == null) return b;
        if (b == null) return a;

        // insert b into a's circular list
        Node<E> aRight = a.right;
        a.right = b;
        b.left = a;
        b.right = aRight;
        aRight.left = b;

        return (a.elem.compareTo(b.elem) < 0) ? a : b;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public E poll() {
        if (root == null) return null;
        Node<E> oldRoot = root;

        if (oldRoot.child != null) {
            Node<E> start = oldRoot.child;
            Node<E> curr = start;
            while (true) {
                Node<E> next = curr.right;
                curr.parent = null;
                curr.left = curr.right = curr;
                root = merge(root, curr);
                if (next == start) break;
                curr = next;
            }
        }

        // remove old root
        if (oldRoot.right == oldRoot) {
            root = null;
        } else {
            Node<E> nextRoot = oldRoot.right;
            delink(oldRoot);
            root = nextRoot;
            consolidate();
        }

        size--;
        return oldRoot.elem;
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public E peek() {
        return (root == null) ? null : root.elem;
    }

    private void consolidate() {
        if (root == null) return;

        List<Node<E>> list = new ArrayList<>();
        Node<E> start = root;
        Node<E> curr = start;
        while (true) {
            list.add(curr);
            curr = curr.right;
            if (curr == start) break;
        }

        Map<Integer, Node<E>> degrees = new HashMap<>();
        for (Node<E> node : list) {
            while (degrees.containsKey(node.degree)) {
                Node<E> other = degrees.remove(node.degree);
                if (node.elem.compareTo(other.elem) > 0) {
                    Node<E> temp = node;
                    node = other;
                    other = temp;
                }
                link(other, node);
            }
            degrees.put(node.degree, node);
        }

        root = null;
        for (Node<E> node : degrees.values()) {
            node.left = node.right = node;
            root = merge(root, node);
        }
    }

    private void link(Node<E> child, Node<E> parent) {
        delink(child);
        child.left = child.right = child;
        parent.child = merge(parent.child, child);
        child.parent = parent;
        parent.degree++;
        child.marked = false;
    }

    private void delink(Node<E> node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }
}
