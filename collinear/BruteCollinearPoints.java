import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> m_segments;
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if(points == null)
            throw new IllegalArgumentException();

        m_segments = new ArrayList<>();
        Arrays.sort(points);
        for(int i = 0; i < points.length; i++)
            for (int j = i+1; j < points.length;j++ )
                for (int k = j+1; k < points.length; k++)
                    for (int l = k+1; l < points.length; l++)
                    {
                        double slope0 = points[i].slopeTo(points[j]);
                        double slope1 = points[i].slopeTo(points[k]);
                        double slope2 = points[i].slopeTo(points[l]);
                        if(slope0 == slope1 && slope1 == slope2)
                            m_segments.add(new LineSegment(points[i],points[l]));
                    }

    }
    public           int numberOfSegments()        // the number of line segments
    {
        return m_segments.size();
    }
    public LineSegment[] segments()                // the line segments
    {
        return m_segments.toArray(new LineSegment[m_segments.size()]);
    }
}
