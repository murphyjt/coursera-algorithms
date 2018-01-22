import edu.princeton.cs.algs4.WeightedQuickUnionUF;;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 */
public class Percolation {

    private int n;
    private boolean[][] open;
    private int openCount;
    private WeightedQuickUnionUF uf;

    /**
     * Create an n-by-n grid, with all sites blocked.
     * 
     * @param  n the number of sites in a row and column
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        open = new boolean[n][n];
        openCount = 0;
        // Last two sites represent virtual source and sink, respectively.
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    /**
     * Open site if it is not open already.
     */
    public void open(int row, int col) {
        validate(row, col);
        
        if (!isOpen(row, col)) {
            // Open it
            open[row - 1][col - 1] = true;
            openCount++;

            // Connect it to top neighbor or Source
            if (row == 1) {
                uf.union(to1D(row, col), getSource());
                openCount++;
            } else if (isOpen(row - 1, col)) {
                uf.union(to1D(row, col), to1D(row - 1, col));
                openCount++;
            }

            // Connect it to bottom neighbor or Sink
            if (row == n) {
                uf.union(to1D(row, col), getSink());
            } else if (isOpen(row + 1, col)) {
                uf.union(to1D(row, col), to1D(row + 1, col));
                openCount++;
            }

            // Connect it to left neighbor
            if (col != 1 && isOpen(row, col - 1)) {
                uf.union(to1D(row, col), to1D(row, col - 1));
                openCount++;
            }

            // Connect it to right neighbor
            if (col != n && isOpen(row, col + 1)) {
                uf.union(to1D(row, col), to1D(row, col + 1));
                openCount++;
            }
        }
        
    }

    /**
     * @return {@code true} if the site is open.
     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[row - 1][col - 1];
    }

    /**
     * @return {@code true} if the site is connected to the source.
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        return uf.connected(to1D(row, col), getSource());
    }

    /**
     * @return number of open sites.
     */
    public int numberOfOpenSites() {
        return openCount;
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return uf.connected(getSource(), getSink());
    }

    /**
     * Validate that the row and column exists.
     */
    private void validate(int row, int col) {
        if (n == 0 || row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Converts two dimensional 1-indexed to one dimensional 0-indexed.
     */
    private int to1D(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private int getSource() {
        return n * n;
    }

    private int getSink() {
        return n * n + 1;
    }
 
    // test client (optional)
    public static void main(String[] args) {
        
    }
 }
