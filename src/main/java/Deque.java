import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;

    private class Node {
        private Node next;
        private Node back;
        private Item item;
    }

    private int n = 0;

    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of elements in this deque.
     *
     * @return the number of elements in this deque
     */
    public int size() {
        return n;
    }

    /**
     * Inserts the specified element at the front of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method {@link #offerFirst}.
     *
     * @param e the element to add
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null and this
     *         deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    public void addFirst(Item e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }

        Node node = new Node();
        node.item = e;

        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.back = node;
            first = node;
        }
        n++;
    }

    /**
     * Inserts the specified element at the end of this deque if it is
     * possible to do so immediately without violating capacity restrictions,
     * throwing an {@code IllegalStateException} if no space is currently
     * available.  When using a capacity-restricted deque, it is generally
     * preferable to use method {@link #offerLast}.
     *
     * <p>This method is equivalent to {@link #add}.
     *
     * @param e the element to add
     * @throws IllegalStateException if the element cannot be added at this
     *         time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this deque
     * @throws NullPointerException if the specified element is null and this
     *         deque does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *         element prevents it from being added to this deque
     */
    public void addLast(Item e) {
        if (e == null) {
            throw new IllegalArgumentException();
        }

        Node node = new Node();
        node.item = e;

        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            node.back = last;
            last.next = node;
            last = node;
        }
        n++;
    }

    /**
     * Retrieves and removes the first element of this deque.  This method
     * differs from {@link #pollFirst pollFirst} only in that it throws an
     * exception if this deque is empty.
     *
     * @return the head of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // nextNode can be null
        Node nextNode = first.next;

        // If nextNode is not null, remove the reference to the node-to-be-removed
        if (nextNode != null) {
            nextNode.back = null;
        }

        // Maintain reference to item and remove references from node-to-be-removed
        Item removedItem = first.item;
        first.item = null;
        first.next = null;

        // Decrementing early for easier isEmpty logic
        n--;

        // Reset pointers
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            first = nextNode;
        }

        return removedItem;
    }

    /**
     * Retrieves and removes the last element of this deque.  This method
     * differs from {@link #pollLast pollLast} only in that it throws an
     * exception if this deque is empty.
     *
     * @return the tail of this deque
     * @throws NoSuchElementException if this deque is empty
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // backNode can be null
        Node backNode = last.back;

        // If backNode is not null, remove the reference to the node-to-be-removed
        if (backNode != null) {
            backNode.next = null;
        }

        // Maintain reference to item and remove references from node-to-be-removed
        Item removedItem = last.item;
        last.item = null;
        last.back = null;

        // Decrementing early for easier isEmpty logic
        n--;

        // Reset pointers
        if (isEmpty()) {
            first = null;
            last = null;
        } else {
            last = backNode;
        }

        return removedItem;
    }

    // an iterator, doesn't implement remove() since it's optional
    private class FrontToEndIterator implements Iterator<Item> {
        private Node next = first;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = next.item;
            next = next.next;
            return item;
        }
    }
    
    /**
     * Returns an iterator over the elements in this deque in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * @return an iterator over the elements in this deque in proper sequence
     */
    @Override
    public Iterator<Item> iterator() {
        return new FrontToEndIterator();
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        System.out.printf("size %d%n", deque.size());
        deque.addFirst("b");
        deque.addFirst("a");
        deque.addLast("c");
        System.out.printf("size %d%n", deque.size());
        for (String var : deque) {
            System.out.printf("val %s%n", var);
        }
        System.out.println(deque.removeLast());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeFirst());
        System.out.printf("size %d%n", deque.size());
        deque.addFirst("Test");
        System.out.println(deque.removeLast());
        System.out.printf("size %d%n", deque.size());
    }

}
