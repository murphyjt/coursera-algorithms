import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items = (Item[]) new Object[1];
    private int n = 0;

    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity >= n;

        // Less efficient than Arrays::copyOf (but necessary per requirements)
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (n == items.length) {
            resize(2 * items.length);
        }
        // System.out.println("Items length " + items.length);
        items[n++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(n);
        Item item = items[random];

        // Replace removed item with the last item (safe for first & last item)
        items[random] = items[--n];

        // Set what was the last item to null
        items[n] = null;

        if (items.length > 1 && n <= items.length / 4) {
            resize(items.length / 2);
        }

        // System.out.println("Items length " + items.length);

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(n)];
    }

    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }
    
    private class RandomizedIterator implements Iterator<Item> {

        private final Item[] randomizedItems = (Item[]) new Object[n];
        private int next = n - 1;

        public RandomizedIterator() {
            for (int i = 0; i < n; i++) {
                randomizedItems[i] = items[i];
            }
            StdRandom.shuffle(randomizedItems);
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return next >= 0;
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
            return randomizedItems[next--];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.isEmpty();
        rq.size();
        rq.size();
        rq.enqueue(15);
        rq.enqueue(35);
        rq.dequeue();
        rq.dequeue();
        rq.enqueue(32);
        rq.dequeue();
        rq.enqueue(45);
        // System.out.printf("size %d%n", queue.size());
        // queue.enqueue(1);
        // queue.enqueue(2);
        // queue.enqueue(3);
        // queue.enqueue(4);
        // queue.enqueue(5);
        // queue.enqueue(6);
        // System.out.printf("size %d%n", queue.size());

        // System.out.println("Iterating");
        // queue.forEach(number -> System.out.println(number));

        // System.out.println("Nested foreach");
        // for (int outer : queue) {
        //     System.out.println("Outer " + outer);
        //     for (int inner : queue) {
        //         System.out.println("Inner " + inner);
        //     }
        // }

        // System.out.println("Manual dequeue");
        // System.out.printf("size %d%n", queue.size());
        // System.out.println(queue.dequeue());
        // System.out.println(queue.dequeue());
        // System.out.println(queue.dequeue());

        // System.out.println("Iterating");
        // queue.forEach(number -> System.out.println(number));

        // System.out.println("Continuing manual dequeue");
        // System.out.printf("size %d%n", queue.size());
        // System.out.println(queue.dequeue());
        // System.out.println(queue.dequeue());
        // System.out.println(queue.dequeue());
        // System.out.printf("size %d%n", queue.size());

        // System.out.println("Requeing");
        // queue.enqueue(1);
        // queue.enqueue(2);
        // queue.enqueue(3);
        // queue.enqueue(4);
        // System.out.printf("size %d%n", queue.size());

        // System.out.println("Iterating");
        // queue.forEach(number -> System.out.println(number));
    }
 }
 