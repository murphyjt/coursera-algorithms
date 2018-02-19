import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // private static final Comparator<SearchNode> HAMMING = (left, right) -> {
    //     if (left.board.hamming() + left.moves == right.board.hamming() + right.moves) return 0;
    //     return left.board.hamming() + left.moves - right.board.hamming() + right.moves;
    // };

    private static final Comparator<SearchNode> MANHATTAN = (left, right) -> {
        if (left.board.manhattan() + left.moves == right.board.manhattan() + right.moves) return 0;
        return left.board.manhattan() + left.moves - right.board.manhattan() + right.moves;
    };

    private final MinPQ<SearchNode> queue = new MinPQ<>(MANHATTAN);
    private final MinPQ<SearchNode> twinQueue = new MinPQ<>(MANHATTAN);

    private final class SearchNode {
        final Board board;
        final int moves;
        private final SearchNode predecessor;

        SearchNode(Board board, int moves, SearchNode predecessor) {
            this.board = board;
            this.moves = moves;
            this.predecessor = predecessor;
        }

        Iterable<Board> path() {
            final Stack<Board> boards = new Stack<>();
            SearchNode currentNode = queue.min();

            // Step backwards pushing each board onto the stack
            while (currentNode != null) {
                boards.push(currentNode.board);
                currentNode = currentNode.predecessor;
            }

            return boards;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        queue.insert(new SearchNode(initial, 0, null));
        twinQueue.insert(new SearchNode(initial.twin(), 0, null));

        while (!queue.min().board.isGoal() && !twinQueue.min().board.isGoal()) {
            SearchNode searchNode = queue.delMin();
            for (final Board neighbor : searchNode.board.neighbors()) {
                // Critical optimization
                if (searchNode.predecessor != null && neighbor.equals(searchNode.predecessor.board)) continue;

                // Enqueue neighbors
                queue.insert(new SearchNode(neighbor, searchNode.moves + 1, searchNode));
            }

            searchNode = twinQueue.delMin();
            for (final Board neighbor : searchNode.board.neighbors()) {
                // Critical optimization
                if (searchNode.predecessor != null && neighbor.equals(searchNode.predecessor.board)) continue;

                // Enqueue neighbors
                twinQueue.insert(new SearchNode(neighbor, searchNode.moves + 1, searchNode));
            }
        }

    }

    public boolean isSolvable() {
        return queue.min().board.isGoal();
    }

    /**
     * Minimum number of moves to solve initial board; -1 if unsolvable.
     */
    public int moves() {
        return isSolvable() ? queue.min().moves : -1;
    }

    public Iterable<Board> solution() {
        return isSolvable() ? queue.min().path() : null;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
    
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
    }
}
