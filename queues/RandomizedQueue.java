import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] m_current;
        int m_next = 0;

        RandomizedQueueIterator() {
            m_current = (Item[]) new Object[m_size];
            int j = 0;

            for(Item item : m_items) {
                if(item != null)
                    m_current[j++] = item;
            }

            // shuffle current array
            for (int i = 0; i < m_current.length; i++)
            {
                int r = StdRandom.uniform(i + 1);
                Item temp = m_current[i];
                m_current[i] = m_current[r];
                m_current[r] = temp;
            }
        }

        @Override
        public boolean hasNext() {
            return m_next < m_current.length;
        }

        @Override
        public Item next() {
            if(m_next >= m_current.length)
                throw new NoSuchElementException();

            return m_current[m_next++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Item[] m_items;
    private int m_size = 0;
    private int m_position = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        m_items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {return m_size == 0;}

    // return the number of items on the randomized queue
    public int size() {return m_size;}

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if(m_size + 1 > m_items.length || m_position + 1 > m_items.length)
            resizeArray(m_items.length*2);

        if(m_items[m_position] != null)
            resizeArray(m_items.length*2);

        m_items[m_position++] = item;
        m_size++;
    }

    private void resizeArray(int capacity) {
        Item[] array = (Item[]) new Object[capacity];
        int j = 0;

        for(Item item : m_items) {
            if(item != null)
                array[j++] = item;
        }

        m_items = array;
        m_position = j;
    }

    // remove and return a random item
    public Item dequeue() {
        if(m_size == 0)
            throw new NoSuchElementException();

        int random = StdRandom.uniform(m_items.length);

        while (m_items[random] == null)
            random = StdRandom.uniform(m_items.length);

        Item item = m_items[random];
        m_items[random] = null;
        m_size--;

        if(m_size < m_items.length / 2)
            resizeArray(m_items.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if(m_size == 0)
            throw new NoSuchElementException();

        int random = StdRandom.uniform(m_items.length);

        while (m_items[random] == null)
            random = StdRandom.uniform(m_items.length);

        return m_items[random];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {return new RandomizedQueueIterator();}

    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        for(int i = 0; i < 10; i++)
            queue.enqueue(Integer.toString(i));

        StdOut.print("\n" + queue.size() + "\n");

        for(String s : queue)
            StdOut.print(s);

        queue.dequeue();
        queue.dequeue();

        StdOut.print("\n" + queue.size() + "\n");

        for(String s : queue)
            StdOut.print(s);

        StdOut.print("\n" +queue.sample());
        StdOut.print("\n" + queue.size() + "\n");
        StdOut.print(queue.isEmpty());
    }

}