import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

public class PointSET {
    private class PointDistancePair implements Comparable<PointDistancePair>{
        Point2D point;
        double distance;
        PointDistancePair(Point2D point, double distance){
            this.point = point;
            this.distance = distance;
        }

        @Override
        public int compareTo(PointDistancePair o) {
            if(this.distance < o.distance)
                return -1;
            else if(this.distance > o.distance)
                return 1;
            else return 0;
        }
    }
    private TreeSet<Point2D> m_pointSET;

    public         PointSET()                               // construct an empty set of points
    {
        m_pointSET = new TreeSet<Point2D>();
    }

    public           boolean isEmpty()                      // is the set empty?
    {
        return m_pointSET.isEmpty();
    }

    public               int size()                         // number of points in the set
    {
        return m_pointSET.size();
    }

    public              void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if(p == null)
            throw new IllegalArgumentException();

        m_pointSET.add(p);
    }
    public           boolean contains(Point2D p)            // does the set contain point p?
    {
        if(p == null)
            throw new IllegalArgumentException();

        return m_pointSET.contains(p);
    }
    public              void draw()                         // draw all points to standard draw
    {
        for (Point2D p : m_pointSET)
            StdDraw.point(p.x(), p.y());
    }
    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if(rect == null)
            throw new IllegalArgumentException();

        if(m_pointSET.isEmpty())
            return null;

        ArrayList<Point2D> list = new ArrayList<>(m_pointSET.size());

        //points in set are ordered by y,then x

        for (Point2D p : m_pointSET)
            if(rect.contains(p))
                list.add(p);

        return list;
    }
    public           Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if(p == null)
            throw new IllegalArgumentException();

        if(m_pointSET.isEmpty())
            return null;

        ArrayList<PointDistancePair> list = new ArrayList<>(m_pointSET.size());

        for (Point2D point : m_pointSET)
            list.add(new PointDistancePair(point, point.distanceTo(p)));

        return Collections.min(list).point;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        int N = 1000;
        PointSET set = new PointSET();

        for(int i = 0; i < N; i++)
            set.insert(new Point2D(StdRandom.uniform(), StdRandom.uniform()));

        StdDraw.setPenRadius(0.01);
        set.draw();

        StdDraw.setPenColor(Color.green);
        RectHV rect = new RectHV(0.25,0.25,0.75,0.75);
        StdDraw.rectangle((rect.xmin()+rect.xmax())/2, ((rect.ymin()+rect.ymax())/2), rect.width()/2, rect.height()/2);

        StdDraw.setPenColor(Color.blue);
        for(Point2D pin : set.range(rect))
            StdDraw.point(pin.x(),pin.y());

        Point2D p = set.nearest(new Point2D(0.5,0.5));
        StdDraw.setPenColor(Color.RED);
        StdDraw.point(p.x(), p.y());
    }
}
