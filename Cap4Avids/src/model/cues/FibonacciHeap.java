package model.cues;

import java.util.*;

public class FibonacciHeap<E extends Comparable<E>> extends AbstractQueue<E> {

    private static class Node<E>{
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
    private Node<E> root;

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
        if (size==0){
            root = merge(root, node);;
            size++;
            return true;
        }
        if (e.compareTo(root.elem) == 0) {
            return false;
        }

        size++;
        root = merge(root, node);
        return true;
    }

    private Node<E> merge(Node<E> node, Node<E> other) {
        if (node == null) return other;
        if (other == null) return node;

        Node<E> nRight = node.right;
        Node<E> rLeft = other.left;
        node.right = other;
        other.left = node;

        nRight.left = rLeft;
        rLeft.right = nRight;

        if (node.elem.compareTo(other.elem) < 0){
            return node;
        }
        return other;
    }

    private void consolidate(){
        List<Node<E>> roots = new ArrayList<>();
        Map<Integer, Node<E>> degrees = new HashMap<>();
        roots.add(root);
        while (roots.getLast().right != null && roots.getLast().right.elem.compareTo(root.elem) != 0) {
            roots.add(roots.getLast().right);
        }


        for(Node<E> node : roots){
            while(degrees.containsKey(node.degree)){
                Node<E> other = degrees.remove(node.degree);
                if(node.elem.compareTo(other.elem) > 0){
                    Node<E> t = node;
                    node = other;
                    other = t;
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

    private void link(Node<E> a, Node<E> b) {
        delink(a);
        a.left = a.right = a;
        b.child = merge(b.child, a);
        a.parent = b;
        b.degree++;
        a.marked = false;
    }

    private void delink(Node<E> node){
        node.left.right = node.right;
        node.right.left = node.left;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    @Override
    public E poll() {
        if (size == 0){
            return null;
        }
        size--;
        Node<E> oldRoot = root;

        if(oldRoot.child != null){
            List<Node<E>> children = new ArrayList<>();
            while(children.getLast().child != null && children.getLast().child.elem.compareTo(oldRoot.elem) != 0){
                children.add(children.getLast().right);
            }

            for (Node<E> node : children) {
                node.parent = null;
                node.left = node.right = null;
                root = merge(root, node);
            }
        }
        if(oldRoot.elem.compareTo(oldRoot.right.elem) == 0){
            root = null;
        }else{
            Node<E> next = oldRoot.right;
            delink(oldRoot);
            root = oldRoot;
            consolidate();
        }

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
        return root.elem;
    }
}
