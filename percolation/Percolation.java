import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N,m_numberOfOpenSites;
    private boolean[][] m_percolation;
    private WeightedQuickUnionUF m_weightedQuickUnionUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if(n<=0)
            throw new IllegalArgumentException();

        N = n;
        m_numberOfOpenSites = 0;
        m_percolation = new boolean[n][n];
        m_weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2); // n*n:top node n*n+1 bottom node
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        checkRange(row, col);

        int p = row -1;
        int q = col -1;

        if(!m_percolation[p][q])
        {
            m_percolation[p][q] = true;
            m_numberOfOpenSites++;
        }
        else
            return;

        if(p == 0)
            m_weightedQuickUnionUF.union(q, N*N);
        else if(p == N-1)
            m_weightedQuickUnionUF.union(p*N+q, N*N+1);

        for(int i = 0; i<4; i++)
        {
            //right +1
            //left -1
            //top -N
            //bottom +N
            int index;

            switch (i)
            {
                case 0:
                    index = 1;
                    break;
                case 1:
                    index = -1;
                    break;
                case 2:
                    index = N;
                    break;
                default:
                    index = -N;
                    break;
            }

            int indexOfCellToConnect = p*N+q + index;

            if(indexOfCellToConnect >= 0 && indexOfCellToConnect < N*N)
            {
                if(m_percolation[indexOfCellToConnect/N][indexOfCellToConnect%N]
                && (q != 0 || index != -1)
                && (q != N-1 || index != 1))
                    m_weightedQuickUnionUF.union(indexOfCellToConnect, p*N+q);
            }
        }
    }

    private void checkRange(int row, int col) {
        if(row < 1 || row > N || col < 1 || col > N)
            throw new IllegalArgumentException("outside of range from 1 to " + Integer.toString(N));
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        checkRange(row, col);
        return m_percolation[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        checkRange(row, col);
        return m_weightedQuickUnionUF.find((row-1)*N+(col-1)) == m_weightedQuickUnionUF.find(N*N);
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return m_numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return m_weightedQuickUnionUF.find(N*N) == m_weightedQuickUnionUF.find(N*N+1);
    }

    // test client (optional)
    public static void main(String[] args)
    {

    }
}
