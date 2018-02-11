import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;

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
        int n = 0;
        Point[] points = new Point[StdIn.readInt()];
        while (!StdIn.isEmpty()) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[n++] = new Point(x, y);
        }

        BruteCollinearPoints collinearPoints = new BruteCollinearPoints(points);
        System.out.println(collinearPoints.numberOfSegments());
        for (LineSegment lineSegment : collinearPoints.lineSegments) {
            System.out.println(lineSegment);
        }
    }

}
