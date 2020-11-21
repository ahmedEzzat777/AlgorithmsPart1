import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> m_segments;

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        if(points == null)
            throw new IllegalArgumentException();

        m_segments = new ArrayList<>();

        Arrays.sort(points);
        Point[] sortedArray = new Point[points.length];
        double[] slopes = new double[points.length];

        for (int i = 1; i<points.length; i++)
            if(points[i].compareTo(points[i-1]) == 0)
                throw new IllegalArgumentException();

        for(Point p : points)
        {
            for (int i = 0; i < points.length; i++)
                sortedArray[i] = points[i];

            Arrays.sort(sortedArray, p.slopeOrder());

            for(int i = 0; i<sortedArray.length; i++)
                slopes[i] = p.slopeTo(sortedArray[i]);

            int count = 0;
            int idx = -1;

            for(int i=1;i<slopes.length;i++)
            {
                if(slopes[i] == slopes[i-1])
                {
                    count++;
                    idx = i;
                }
                else if(count != 0)
                {
                    if(count >= 2) // from idx-count to idx are colinear with p
                    {
                        if(p.compareTo(sortedArray[idx - count]) < 0)
                            m_segments.add(new LineSegment(p, sortedArray[idx]));
                    }
                    count = 0;
                    idx = -1;
                }
                if(count >= 2 && i == slopes.length - 1) // from idx-count to idx are colinear with p
                    if(p.compareTo(sortedArray[idx - count]) < 0)
                        m_segments.add(new LineSegment(p, sortedArray[idx]));
            }

        }
    }
    public           int numberOfSegments()        // the number of line segments
    {
        return m_segments.size();
    }
    public LineSegment[] segments()                // the line segments
    {
        return  m_segments.toArray(new LineSegment[m_segments.size()]);
    }
/*
    public static void main(String[] args) {
        int n = 4;
        Point[] points = new Point[n*n];
        int idx = 0;

        for (int i=0; i<n;i++)
            for (int j=0;j<n;j++)
                points[idx++] = new Point(i, j);

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        LineSegment[] segments = collinear.segments();
        StdOut.print(segments.length + "\n");
        for(int i = 0; i < segments.length; ++i) {
            LineSegment segment = segments[i];
            StdOut.println(segment);
        }

    }*/

}