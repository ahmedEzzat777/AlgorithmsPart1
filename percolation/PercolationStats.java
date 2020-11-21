import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;

public class PercolationStats {

    private int N,T;
    private double[] m_percolationArray;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if(n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        N = n;
        T = trials;
        m_percolationArray = new double[T];

        for(int i = 0; i< T; i++)
        {
            Percolation percolation = new Percolation(N);
            int rand;

            while (!percolation.percolates())
            {
                rand = StdRandom.uniform(N*N);
                percolation.open((rand/N)+1, (rand%N)+1);
            }

            m_percolationArray[i] = percolation.numberOfOpenSites()*1.0/(N*N);
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(m_percolationArray);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(m_percolationArray);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean()-1.96*stddev()/Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean()+1.96*stddev()/Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args)
    {
        if(args.length != 2)
            throw new IllegalArgumentException();

        int n = Integer.parseUnsignedInt(args[0]);
        int t = Integer.parseUnsignedInt(args[1]);

        PercolationStats stats = new PercolationStats(n,t);
        StdOut.printf("mean = %f\n", stats.mean());
        StdOut.printf("stddev = %.16f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%.16f, %.16f]\n", stats.confidenceLo(), stats.confidenceHi());
    }

}
