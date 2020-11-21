import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private class SearchNode implements Comparable<SearchNode>{
        Board board;
        SearchNode previous;
        int moves;
        int priority;

        public SearchNode(Board board,
                SearchNode previous,
                int moves){
            this.board = board;
            this.previous = previous;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode o) {
            if(this.priority > o.priority)
                return 1;
            else if(this.priority < o.priority)
                return -1;
            else return 0;
        }
    }

    private ArrayList<Board> m_solution = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial){
        if(initial == null)
            throw new IllegalArgumentException();

        SearchNode currentSN = new SearchNode(initial, null, 0);
        SearchNode currentTwinSN = new SearchNode(initial.twin(), null, 0);
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        while(!currentSN.board.isGoal() && !currentTwinSN.board.isGoal()){
            for (Board b : currentSN.board.neighbors()){
                if(currentSN.previous == null || !b.equals(currentSN.previous.board))
                    pq.insert(new SearchNode(b, currentSN, currentSN.moves+1));
            }
            currentSN = pq.delMin();

            for (Board b : currentTwinSN.board.neighbors()){
                if(currentTwinSN.previous == null || !b.equals(currentTwinSN.previous.board))
                    pqTwin.insert(new SearchNode(b, currentTwinSN, currentTwinSN.moves+1));
            }
            currentTwinSN = pqTwin.delMin();
        }

        if(currentSN.board.isGoal()){
            m_solution = new ArrayList<>();
            while (currentSN != null){
                m_solution.add(currentSN.board);
                currentSN = currentSN.previous;
            }
            Collections.reverse(m_solution);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable(){
        return m_solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves(){
        if(!isSolvable())
            return -1;
        return m_solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution(){
        if(!isSolvable())
            return null;
        return m_solution;
    }

    // test client (see below)
    public static void main(String[] args){
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
/*
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

        Board initial = new Board(arr2D);
        //StdOut.println(initial);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
*/
    }

}
