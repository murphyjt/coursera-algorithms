import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[1];
    private int n;

    public FastCollinearPoints(Point[] pointsArg) {
        if (pointsArg == null) throw new IllegalArgumentException();
        for (Point point : pointsArg) if (point == null) throw new IllegalArgumentException();
        
        Point[] points = new Point[pointsArg.length];
        for (int i = 0; i < pointsArg.length; i++) {
            points[i] = pointsArg[i];
        }

        Arrays.sort(points);

        // Check for repeated points
        for (int i = 1; i < points.length; i++) {
            if (points[i-1].compareTo(points[i]) == 0) throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            Point origin = points[i];
            Point[] sortedPoints = sort(points, origin.slopeOrder());

            // Saves a call to origin.slopeTo(origin)
            double currentSlope = Double.NEGATIVE_INFINITY;
            
            // Always start from origin
            int groupOffset = 0;

            // sortedPoints[0] is always the origin, skip it
            for (int j = 1; j < sortedPoints.length; j++) {
                Point test = sortedPoints[j];
                double slope = origin.slopeTo(test);

                // Iterate as long as possible until grouping breaks
                if (slope == currentSlope) continue;
                
                // Example: [3] - [0] is length 4
                if (j - groupOffset >= 3) {
                    // Only keep line segments starting from bottom to top of plane
                    // TODO verify this behavior
                    if (origin.compareTo(sortedPoints[j-1]) > 0) {
                        // Same as below (Set up for the next group)
                        groupOffset = j;
                        currentSlope = slope;
                        continue;
                    }
                    boolean wrongOrder = false;
                    for (int k = groupOffset + 1; k < j; k++) {
                        if (sortedPoints[k-1].compareTo(sortedPoints[k]) > 0) {
                            // Same as below (Set up for the next group)
                            groupOffset = j;
                            currentSlope = slope;
                            wrongOrder = true;
                            break;
                        }
                    }
                    if (wrongOrder) continue;
                    // We now know the at least 4 points are collinear
                    if (n == lineSegments.length) {
                        resize(2 * lineSegments.length);
                    }
                    lineSegments[n++] = new LineSegment(origin, sortedPoints[j-1]);
                }
                // Set up for the next group
                groupOffset = j;
                currentSlope = slope;
            }

            // Handle vertical lines (will always be at end of sorted array – Double.POSITIVE_INFINITY)
            if (sortedPoints.length - groupOffset >= 3) {
                // Only keep line segments starting from bottom to top of plane
                // TODO verify this behavior
                if (origin.compareTo(sortedPoints[sortedPoints.length-1]) > 0) {
                    // Same as below (Set up for the next group)
                    continue;
                }
                boolean wrongOrder = false;
                for (int k = groupOffset + 1; k < sortedPoints.length; k++) {
                    if (sortedPoints[k-1].compareTo(sortedPoints[k]) > 0) {
                        wrongOrder = true;
                        break;
                    }
                }
                if (wrongOrder) continue;
                // We now know the at least 4 points are collinear
                if (n == lineSegments.length) {
                    resize(2 * lineSegments.length);
                }
                lineSegments[n++] = new LineSegment(origin, sortedPoints[sortedPoints.length-1]);
            }
        }

        // Optional?
        if (lineSegments.length != n) resize(n);
    }

    private void resize(final int capacity) {
        assert capacity >= n;

        // Less efficient than Arrays::copyOf (but necessary per requirements)
        LineSegment[] temp = new LineSegment[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }

    private static Point[] sort(final Point[] array, final Comparator<Point> comparator) {
        Point[] copy = new Point[array.length];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i];
        }
        Arrays.sort(copy, comparator);
        return copy;
    }

    public int numberOfSegments() {
        return n;
    }

    public LineSegment[] segments() {
        LineSegment[] temp = new LineSegment[n];
        for (int i = 0; i < n; i++) {
            temp[i] = lineSegments[i];
        }
        return temp;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
    
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
