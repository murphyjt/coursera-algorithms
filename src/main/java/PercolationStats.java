import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96d;

    private int trialsPerformed;
    private final double[] thresholds;

    /**
     * Perform trials independent experiments on an n-by-n grid.
     */
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new IllegalArgumentException();
        thresholds = new double[trials];

        int numberOfSites = n * n;
        while (trialsPerformed < trials) {
            // StdOut.printf("Performing trial %d of %d%n", trialsPerformed + 1, trials);
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                // StdOut.printf("Opening (%d, %d) site %d of %d%n", row, col, (row - 1) * n + (col - 1), numberOfSites);
                percolation.open(row, col);
            }

            thresholds[trialsPerformed++] = percolation.numberOfOpenSites() / (double) numberOfSites;
            // StdOut.printf("Trial %d threshold at %f%n", trialsPerformed, thresholds[trialsPerformed - 1]);
        }
    }

    /**
     * Sample mean of percolation threshold.
     */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /**
     * Sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return trialsPerformed > 1 ? StdStats.stddev(thresholds) : Double.NaN;
    }

    /**
     * low  endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        double numerator = CONFIDENCE_95 * (trialsPerformed > 1 ? StdStats.stddev(thresholds) : Double.NaN);
        return StdStats.mean(thresholds) - numerator / Math.sqrt(trialsPerformed);
    }

    /**
     * High endpoint of 95% confidence interval.
     */
    public double confidenceHi() {
        double numerator = CONFIDENCE_95 * (trialsPerformed > 1 ? StdStats.stddev(thresholds) : Double.NaN);
        return StdStats.mean(thresholds) + numerator / Math.sqrt(trialsPerformed);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.printf("%-24s= %.17f%n", "mean", stats.mean());
        StdOut.printf("%-24s= %.17f%n", "stddev", stats.stddev());
        StdOut.printf("%-24s= [%.17f, %.17f]%n", "95% confidence interval", stats.confidenceLo(), stats.confidenceHi());
    }

 }
