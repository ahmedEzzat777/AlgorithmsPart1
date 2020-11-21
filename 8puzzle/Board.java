import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public final class Board {
    private final class Position{
        private final int x;
        private final int y;

        public Position(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    private final int[][] m_board;
    private final int m_n;
    private int m_hamming = -1;
    private int m_manhattan = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles){
        if(tiles == null)
            throw new IllegalArgumentException();

        m_n = tiles.length;

        if(m_n > 127 || m_n < 2)
            throw new IllegalArgumentException();
        //validate(tiles);
        m_board = tiles;
    }
/*
    private void validate(int[][] tiles) {
        // validate that
        // 1-array is n*n
        // 2-tiles are from 0 to n*n-1
        MinPQ<Integer> pq = new MinPQ<>(m_n*m_n);

        for (int i = 0; i < m_n; i++)
        {
            if(tiles[i].length != m_n)
                throw new IllegalArgumentException();
            for(int j = 0; j < m_n; j++)
                pq.insert(tiles[i][j]);
        }
        int counter = 0;
        for(int p: pq)
            if(p != counter++)
                throw new IllegalArgumentException();
    }
*/
    // string representation of this board
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(m_n + "\n");
        for (int i = 0; i < m_n; i++) {
            for (int j = 0; j < m_n; j++)
                builder.append(m_board[i][j] + " ");

            builder.append("\n");
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension(){
        return m_n;
    }

    // number of tiles out of place
    public int hamming(){
        if(m_hamming != -1)
            return m_hamming;

        m_hamming = 0;
        for (int i = 0; i < m_n; i++) {
            for (int j = 0; j < m_n; j++)
            {
                if(!(i == m_n - 1 && j == m_n - 1))
                {
                    if(m_board[i][j] != i*m_n+j + 1)
                        m_hamming++;
                }
            }
        }
        return m_hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        if(m_manhattan != -1)
            return m_manhattan;

        m_manhattan = 0;
        for (int i = 0; i < m_n; i++){
            for (int j = 0; j < m_n; j++){
                if(m_board[i][j] != 0)
                {
                    int goalI = (m_board[i][j]-1)/m_n;
                    int goalJ = (m_board[i][j]-1)%m_n;
                    m_manhattan += Math.abs(goalI - i) + Math.abs(goalJ - j);
                }
            }
        }
        return m_manhattan;
    }

    // is this board the goal board?
    public boolean isGoal(){
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y){
        if(y == null)
            return false;
        if(this == y)
            return true;
        if(this.getClass() != y.getClass())
            return false;

        Board compared = (Board)y;

        if(this.m_n != compared.m_n)
            return false;

        for (int i = 0; i < this.m_n; i++)
        {
            for(int j = 0; j < this.m_n; j++)
                if(this.m_board[i][j] != compared.m_board[i][j])
                    return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        ArrayList<Board> boards = new ArrayList<>(4);

        int[][] clone = new int[m_n][m_n];
        Position pos = cloneAndFindZeroPosition(clone);

        if(pos.x != 0)
        {
            exchange(clone, pos.x, pos.y, pos.x-1, pos.y);
            boards.add(new Board(clone));
        }

        if(pos.y != 0)
        {
            clone = new int[m_n][m_n];
            cloneAndFindZeroPosition(clone);
            exchange(clone, pos.x, pos.y, pos.x, pos.y-1);
            boards.add(new Board(clone));
        }


        if(pos.x != m_n - 1)
        {
            clone = new int[m_n][m_n];
            cloneAndFindZeroPosition(clone);
            exchange(clone, pos.x, pos.y, pos.x + 1, pos.y);
            boards.add(new Board(clone));
        }

        if(pos.y != m_n - 1)
        {
            clone = new int[m_n][m_n];
            cloneAndFindZeroPosition(clone);
            exchange(clone, pos.x, pos.y, pos.x, pos.y + 1);
            boards.add(new Board(clone));
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int[][] clone = new int[m_n][m_n];
        int idx = cloneAndFindZeroIndex(clone);

        //swap first two elemennts after the zero

        int first = 0;
        int second = 2;

        if(first == idx)
            first++;

        if(second == idx)
            second++;

        exchange(clone, first, second);
        return new Board(clone);
    }

    private void exchange(int[][] a,int x0, int y0, int x1, int y1){
        int temp = a[x0][y0];
        a[x0][y0] = a[x1][y1];
        a[x1][y1] = temp;
    }

    private void exchange(int[][] a, int p0, int p1){
        int temp = a[p0/m_n][p0%m_n];
        a[p0/m_n][p0%m_n] = a[p1/m_n][p1%m_n];
        a[p1/m_n][p1%m_n] = temp;
    }


    private int cloneAndFindZeroIndex(int[][] clone) {
        int idx = -1;

        for (int i = 0; i < m_n; i++){
            for (int j = 0; j < m_n; j++){
                clone[i][j] = m_board[i][j];

                if(m_board[i][j] == 0)
                    idx = i*m_n + j;
            }
        }
        return idx;
    }

    private Position cloneAndFindZeroPosition(int[][] clone) {
        Position pos = null;

        for (int i = 0; i < m_n; i++){
            for (int j = 0; j < m_n; j++){
                clone[i][j] = m_board[i][j];

                if(m_board[i][j] == 0)
                    pos = new Position(i, j);
            }
        }

        return pos;
    }


    // unit testing (not graded)
    public static void main(String[] args){
        int N = 3;
        int[] arr = new int[N*N];
        for (int i =0;i<N*N;i++)
        {
            arr[i] = i;
        }
        StdRandom.shuffle(arr);

        int [][] arr2D = new int[N][N];
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                arr2D[i][j] = arr[i*N+j];
            }
        }

        Board b = new Board(arr2D);

        StdOut.println(b);
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
        StdOut.println(b.isGoal());
        StdOut.println(b.twin());

        for (Board p:b.neighbors()) {
            StdOut.println(p);
        }
    }

}