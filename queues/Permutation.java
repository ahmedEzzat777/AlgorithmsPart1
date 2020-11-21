import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        if(args.length != 1)
            throw new IllegalArgumentException();

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty())
            queue.enqueue(StdIn.readString());

        int i = 0;

        for (String s : queue)
            if(i++ < k)
                StdOut.print(s + "\n");
            else
                break;
    }
}