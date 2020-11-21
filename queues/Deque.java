import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        public Item item;
        public Node next;
        public Node previous;
    }

    private class DequeIterator implements Iterator<Item> {
        Node m_current = m_first;

        @Override
        public boolean hasNext() {
            return m_current != null;
        }

        @Override
        public Item next() {
            if(m_current == null)
                throw  new NoSuchElementException();

            Item item = m_current.item;

            m_current = m_current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Node m_first = null;
    private Node m_last = null;
    private int m_size = 0;

    // construct an empty deque
    public Deque() {}

    // is the deque empty?
    public boolean isEmpty() {return m_size == 0;}

    // return the number of items on the deque
    public int size() {return m_size;}

    // add the item to the front
    public void addFirst(Item item) {
        if(item == null)
            throw new IllegalArgumentException();

        Node oldFirst = m_first;
        m_first = new Node();
        m_first.item = item;
        m_first.next = oldFirst;

        if(oldFirst == null)
            m_last = m_first;
        else
            oldFirst.previous = m_first;

        m_size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if(item == null)
            throw new IllegalArgumentException();

        Node oldLast = m_last;
        m_last = new Node();
        m_last.item = item;
        m_last.previous = oldLast;

        if(oldLast == null)
            m_first = m_last;
        else
            oldLast.next = m_last;

        m_size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if(m_first == null)
            throw new NoSuchElementException();

        Item item = m_first.item;
        m_first = m_first.next;
        m_size--;

        if(m_first == null)
            m_last = null;
        else
            m_first.previous = null;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if(m_last == null)
            throw new NoSuchElementException();

        Item item = m_last.item;
        m_last = m_last.previous;
        m_size--;

        if(m_last == null)
            m_first = null;
        else
            m_last.next = null;

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {return new DequeIterator();}

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        deque.addFirst("hello");
        deque.addFirst("world");
        deque.addLast("first");
        deque.addLast("deque");

        for (String s : deque) {
            StdOut.print(s+"\n");
        }

        StdOut.print(deque.size()+"\n");

        deque.removeFirst();
        deque.removeLast();

        for (String s : deque) {
            StdOut.print(s+"\n");
        }

        StdOut.print(deque.size()+"\n");

        deque.removeFirst();
        deque.removeFirst();
        StdOut.print(deque.isEmpty()+"\n");
    }

}