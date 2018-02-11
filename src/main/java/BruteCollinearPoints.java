import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments = new LineSegment[1];
    private int n;

    private void resize(int capacity) {
        assert capacity >= n;

        // Less efficient than Arrays::copyOf (but necessary per requirements)
        LineSegment[] temp = new LineSegment[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) if (point == null) throw new IllegalArgumentException();

        Arrays.sort(points);

        // Check for repeated points
        for (int i = 1; i < points.length; i++) {
            if (points[i-1].compareTo(points[i]) == 0) throw new IllegalArgumentException();
        }

        for (int aIndex = 0; aIndex < points.length; aIndex++) {
            Point a = points[aIndex];

            for (int bIndex = aIndex; bIndex < points.length; bIndex++) {
                Point b = points[bIndex];

                double ab = a.slopeTo(b);
                // If same points then skip
                if (ab == Double.NEGATIVE_INFINITY) continue;

                for (int cIndex = bIndex; cIndex < points.length; cIndex++) {
                    Point c = points[cIndex];

                    double ac = a.slopeTo(c);
                    double bc = b.slopeTo(c);
                    if (ab != bc) continue;

                    // If same points then skip
                    if (ac == Double.NEGATIVE_INFINITY) continue;
                    if (bc == Double.NEGATIVE_INFINITY) continue;

                    for (int dIndex = cIndex; dIndex < points.length; dIndex++) {
                        Point d = points[dIndex];

                        double ad = a.slopeTo(d);
                        double bd = b.slopeTo(d);
                        double cd = c.slopeTo(d);

                        if (bc != cd) continue;

                        // If same points then skip
                        if (ad == Double.NEGATIVE_INFINITY) continue;
                        if (bd == Double.NEGATIVE_INFINITY) continue;
                        if (cd == Double.NEGATIVE_INFINITY) continue;
                        
                        // We now know the 4 points are collinear
                        if (n == lineSegments.length) {
                            resize(2 * lineSegments.length);
                        }

                        LineSegment lineSegment = new LineSegment(a, d);
                        // System.out.println(lineSegment);
                        // System.out.printf("%s -> %s -> %s -> %s%n", a, b, c, d);
                        lineSegments[n++] = lineSegment;
                    }
                }
            }
        }

        // Optional?
        if (lineSegments.length != n) resize(n);
    }

    public int numberOfSegments() {
        return n;
    }

    public LineSegment[] segments() {
        return lineSegments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
