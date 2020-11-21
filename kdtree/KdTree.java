import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;
import java.util.ArrayList;

public class KdTree {
    private class Node{
        Node right;
        Node left;
        int level;
        int children;
        Point2D point;

        Node(Point2D point)
        {
            this.point = point;
            this.level = 0;
            this.children = 0;
        }

        Node(Point2D point, int level)
        {
            this.point = point;
            this.level = level;
            this.children = 0;
        }
    }

    private class Nearest{
        Point2D point;
        double distance;
        Nearest(Point2D point,double distance)
        {
            this.point = point;
            this.distance = distance;
        }
    }

    private Node root;

    public KdTree()                               // construct an empty set of points
    {
        root = null;
    }
    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }
    public int size()                         // number of points in the set
    {
        if(root == null)
            return 0;

        return root.children + 1;
    }
    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if(p == null)
            throw new IllegalArgumentException();

        if(contains(p))
            return;

        root = insert(root, p, 0);
    }

    private Node insert(Node node, Point2D p, int level) { //recursive, update children and level, use level for comparsion at insertion point
        if(node == null)
            return new Node(p, level);

        if(node.level%2 == 0)
        {
            if(node.point.x() > p.x())
                node.left = insert(node.left, p, level+1);
            else
                node.right = insert(node.right, p, level+1);
        }
        else
        {
            if(node.point.y() > p.y())
                node.left = insert(node.left, p, level+1);
            else
                node.right = insert(node.right, p, level+1);
        }

        node.children++;
        return node;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if(p == null)
            throw new IllegalArgumentException();

        if(contains(root, p) == null)
            return false;
        return true;
    }

    private Node contains(Node node, Point2D p) {
        if(node == null)
            return null;

        if(node.point.equals(p))
            return node;

        if(node.level%2 == 0)
        {
            if(node.point.x() > p.x())
                return contains(node.left, p);
            else
                return contains(node.right, p);
        }
        else
        {
            if(node.point.y() > p.y())
                return contains(node.left, p);
            else
                return contains(node.right, p);
        }
    }

    public void draw()  // draw all points to standard draw
    {
       RectHV rect = new RectHV(0,0,1,1);
        draw(root, rect);

       /* for(Point2D p : GetKdTreeIterator())
            StdDraw.point(p.x(), p.y());*/
    }

    private void draw(Node node, RectHV rect) {
        if(node == null)
            return;

        StdDraw.setPenColor(Color.black);
        StdDraw.setPenRadius(0.02);
        StdDraw.point(node.point.x(),node.point.y());
        StdDraw.setPenRadius(0.01);
        if(node.level%2 == 0)
        {
            StdDraw.setPenColor(Color.RED);
            double midX = node.point.x();
            StdDraw.line(midX, rect.ymin(), midX, rect.ymax());
            RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), midX, rect.ymax());
            RectHV rightRect = new RectHV(midX, rect.ymin(), rect.xmax(), rect.ymax());
            draw(node.left, leftRect);
            draw(node.right, rightRect);
        }
        else
        {
            StdDraw.setPenColor(Color.BLUE);
            double midY = node.point.y();
            StdDraw.line(rect.xmin(), midY, rect.xmax(), midY);
            RectHV bottomRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), midY);
            RectHV topRect = new RectHV(rect.xmin(), midY, rect.xmax(), rect.ymax());
            draw(node.left, bottomRect);
            draw(node.right, topRect);
        }
    }

    private Iterable<Point2D> GetKdTreeIterator() {
        ArrayList<Point2D> list = new ArrayList<>(size());
        inorder(list, root);
        return list;
    }

    private void inorder(ArrayList<Point2D> list, Node node) {
        if(node == null)
            return;

        inorder(list, node.left);
        list.add(node.point);
        inorder(list, node.right);
    }

    public Iterable<Point2D> range(RectHV rect)  // all points that are inside the rectangle (or on the boundary)
    {
        if(rect == null)
            throw new IllegalArgumentException();

        ArrayList<Point2D> list = new ArrayList<>();
        RectHV searchRect = new RectHV(0,0,1,1);
        range(list, root, rect, searchRect);
        return list;
    }

    private void range(ArrayList<Point2D> list, Node node, RectHV rect, RectHV searchRect) {
        if(node == null)
            return;

        if(rect.contains(node.point))
            list.add(node.point);

        if(node.level%2 == 0)
        {
            double midX = node.point.x();
            RectHV leftRect = new RectHV(searchRect.xmin(), searchRect.ymin(), midX, searchRect.ymax());
            RectHV rightRect = new RectHV(midX, searchRect.ymin(), searchRect.xmax(), searchRect.ymax());

            if(leftRect.intersects(rect))
                range(list, node.left, rect, leftRect);
            if(rightRect.intersects(rect))
                range(list, node.right, rect, rightRect);
        }
        else
        {
            double midY = node.point.y();
            RectHV bottomRect = new RectHV(searchRect.xmin(), searchRect.ymin(), searchRect.xmax(), midY);
            RectHV topRect = new RectHV(searchRect.xmin(), midY, searchRect.xmax(), searchRect.ymax());

            if(bottomRect.intersects(rect))
                range(list, node.left, rect, bottomRect);
            if(topRect.intersects(rect))
                range(list, node.right, rect, topRect);
        }
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if(p == null)
            throw new IllegalArgumentException();

        Nearest nearest = new Nearest(null, 2);
        RectHV rect = new RectHV(0,0,1,1);
        nearest(root, p, nearest, rect);
        return nearest.point;
    }

    private void nearest(Node node, Point2D p, Nearest nearest, RectHV rect) {
        if(node == null)
            return;

        if(nearest.distance < rect.distanceTo(p))
            return;

        double dist = node.point.distanceTo(p);

        if(dist < nearest.distance)
        {
            nearest.point = node.point;
            nearest.distance = dist;
        }

        if(node.level%2 == 0)
        {
            double midX = node.point.x();
            RectHV leftRect = new RectHV(rect.xmin(), rect.ymin(), midX, rect.ymax());
            RectHV rightRect = new RectHV(midX, rect.ymin(), rect.xmax(), rect.ymax());

            if(node.point.x() > p.x())
            {
                nearest(node.left, p, nearest, leftRect);
                nearest(node.right, p, nearest, rightRect);
            }
            else
            {
                nearest(node.right, p, nearest, rightRect);
                nearest(node.left, p, nearest, leftRect);
            }
        }
        else
        {
            double midY = node.point.y();
            RectHV bottomRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), midY);
            RectHV topRect = new RectHV(rect.xmin(), midY, rect.xmax(), rect.ymax());

            if(node.point.y() > p.y())
            {
                nearest(node.left, p, nearest, bottomRect);
                nearest(node.right, p, nearest, topRect);
            }
            else
            {
                nearest(node.right, p, nearest, topRect);
                nearest(node.left, p, nearest, bottomRect);
            }
        }
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        /*
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7,0.2));
        tree.insert(new Point2D(0.5,0.4));
        tree.insert(new Point2D(0.2,0.3));
        tree.insert(new Point2D(0.4,0.7));
        tree.insert(new Point2D(0.9,0.6));
        int size = tree.size();

        boolean x = tree.contains(new Point2D(0.1, 0.7));
        boolean y = tree.contains(new Point2D(0.9, 0.9));
        tree.draw();
*/
        int N = 1000;
        KdTree set = new KdTree();

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