import java.util.Arrays;

import edu.princeton.cs.algs4.Stack;

public class Board {

    private final int n;
    private final int[][] tiles;
    private final int[][] twin;
    private final int hamming;
    private final int manhattan;
    private int blankRow;
    private int blankCol;

    /**
     * Construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j).
     */
    public Board(final int[][] blocks) {
        if (blocks == null) throw new IllegalArgumentException();

        n = blocks.length;

        int hammingCount = 0;
        int manhattanCount = 0;

        // Defensive copy
        tiles = new int[n][n];
        twin = new int[n][n];
        for (int row = 0; row < n; row++) {
            if (blocks[row] == null) throw new IllegalArgumentException();
            if (blocks[row].length != n) throw new IllegalArgumentException();
            for (int col = 0; col < n; col++) {
                final int tile = blocks[row][col];

                // Assignment
                tiles[row][col] = tile;
                twin[row][col] = tile;

                // Skip blank square but note location
                if (tile == 0) {
                    blankRow = row;
                    blankCol = col;
                    continue;
                }

                // Calculate goal for this [row, col]
                final int goal = row * n + col + 1;

                // Compute hamming distance
                if (tile != goal) hammingCount++;

                // Compute manhattan distance
                final int correctRow = (tile - 1) / n;
                final int correctCol = (tile - 1) % n;
                manhattanCount += Math.abs(correctRow - row);
                manhattanCount += Math.abs(correctCol - col);
            }
        }

        // Assemble twin by attempting to exchange first two, otherwise exchange last two
        if (twin[0][0] != 0 && twin[0][1] != 0) {
            final int temp = twin[0][0];
            twin[0][0] = twin[0][1];
            twin[0][1] = temp;
        } else {
            final int temp = twin[n - 1][n - 1];
            twin[n - 1][n - 1] = twin[n - 1][n - 2];
            twin[n - 1][n - 2] = temp;
        }

        hamming = hammingCount;
        manhattan = manhattanCount;
    }
    
    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }
    
    public Board twin() {
        return new Board(twin);
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        final Board that = (Board) other;
        if (that.n != this.n) return false;
        if (that.hamming != this.hamming) return false;
        if (that.manhattan != this.manhattan) return false;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    private static int[][] copyOf(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int row = 0; row < array.length; row++) {
            copy[row] = new int[array[row].length];
            for (int col = 0; col < array[row].length; col++) {
                copy[row][col] = array[row][col];
            }
        }
        return copy;
    }

    private static int[][] swap(int[][] array, int rowA, int colA, int rowB, int colB) {
        int[][] copy = copyOf(array);
        int temp = copy[rowA][colA];
        copy[rowA][colA] = copy[rowB][colB];
        copy[rowB][colB] = temp;
        return copy;
    }

    /**
     * Any board may have 2, 3, or 4 neighbors.
     */
    public Iterable<Board> neighbors() {
        final boolean onLeft   = blankCol == 0;
        final boolean onTop    = blankRow == 0;
        final boolean onRight  = blankCol == n - 1;
        final boolean onBottom = blankRow == n - 1;

        final Stack<Board> neighbors = new Stack<>();

        if (!onLeft) neighbors.push(new Board(swap(this.tiles, blankRow, blankCol, blankRow, blankCol - 1)));
        if (!onTop) neighbors.push(new Board(swap(this.tiles, blankRow, blankCol, blankRow - 1, blankCol)));
        if (!onRight) neighbors.push(new Board(swap(this.tiles, blankRow, blankCol, blankRow, blankCol + 1)));
        if (!onBottom) neighbors.push(new Board(swap(this.tiles, blankRow, blankCol, blankRow + 1, blankCol)));

        return neighbors;
    }

    public String toString() {
        final StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        final Board board = new Board(new int[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
        });
        System.out.println("  Board  ");
        System.out.println("=========");
        System.out.println();
        System.out.println(board);
        System.out.println("Hamming " + board.hamming());
        System.out.println("Manhattan " + board.manhattan());
        System.out.println();
        System.out.println("Neighbors");
        System.out.println("=========");
        System.out.println();
        for (final Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
            System.out.println("Hamming " + neighbor.hamming());
            System.out.println("Manhattan " + neighbor.manhattan());
        }
    }

}
