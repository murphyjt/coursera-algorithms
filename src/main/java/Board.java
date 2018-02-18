import java.util.Arrays;
import java.util.Iterator;

public class Board {

    private final int n;
    private final int[][] tiles;
    private final int[][] twin;
    private final int hamming;
    private final int manhattan;

    /**
     * Construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j).
     */
    public Board(int[][] blocks) {
        if (blocks == null) throw new IllegalArgumentException();

        n = blocks.length;

        int hammingCount = 0;
        int manhattanCount = 0;

        // Defensive copy
        tiles = new int[n][n];
        twin = new int[n][n];
        for (int row = 0; row < n; row++) {
            assert blocks[row].length == n;
            for (int col = 0; col < n; col++) {
                int tile = blocks[row][col];

                // Assignment
                tiles[row][col] = tile;
                twin[row][col] = tile;

                // Skip blank square
                if (tile == 0) continue;

                // Calculate goal for this [row, col]
                int goal = row * n + col + 1;

                // Compute hamming distance
                if (tile != goal) hammingCount++;

                // Compute manhattan distance
                int goalRow = (goal - 1) / n;
                int goalCol = (goal - 1) % n;
                manhattanCount += Math.abs(goalRow - row);
                manhattanCount += Math.abs(goalCol - col);
            }
        }

        // Assemble twin by attempting to exchange first two, otherwise exchange last two
        if (twin[0][0] != 0 && twin[0][1] != 0) {
            int temp = twin[0][0];
            twin[0][0] = twin[0][1];
            twin[0][1] = temp;
        } else {
            int temp = twin[n - 1][n - 1];
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

        Board that = (Board) other;
        if (that.n != this.n) return false;
        if (that.hamming != this.hamming) return false;
        if (that.manhattan != this.manhattan) return false;

        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    public Iterable<Board> neighbors() {
        return new Iterable<Board>() {
        
            @Override
            public Iterator<Board> iterator() {
                return null;
            }
        };
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
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

    }

}
