import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private final static boolean VERTICAL = true;
    private final static boolean HORIZONTAL = false;
    private Node root;
    private int n;
    
    // construct an empty set of points 
    public KdTree()             
    {
        root = null;
        n = 0;
    }
    
    private class Node
    {
        private Point2D point;
        private boolean direction;
        private Node left, right;
    }
    
    // is the set empty? 
    public boolean isEmpty()   
    {
        return n == 0;
    }
    
    // number of points in the set 
    public int size()       
    {                 
        return n;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)        
    {
        if (root == null) root = makeNode(null, p);
        else insert(root, p);
    }
    
    private void insert(Node parent, Point2D p) {
        if (less(p, parent)) 
        {
            if (parent.left == null) parent.left = makeNode(parent, p);
            else insert(parent.left, p);
        }
        else
        {
            if (parent.right == null) parent.right = makeNode(parent, p);
            else insert(parent.right, p);
        }
    }

    private Node makeNode(Node parent, Point2D p) {
        Node newNode = new Node();
        newNode.point = p;
        if (parent == null) newNode.direction = VERTICAL;
        else newNode.direction = !parent.direction;
        
        return newNode;
    }

    private boolean less(Point2D p1, Node parent) {
        if (parent.direction == VERTICAL) return (p1.x() < parent.point.x());
        else return (p1.y() < parent.point.y());
    }

    // does the set contain point p? 
    public boolean contains(Point2D p)  
    {
        if (p == null) throw new NullPointerException();
        return points.contains(p);
    }
    
    // draw all points to standard draw 
    public void draw()        
    {
        for (Point2D p : points)
        {
            StdDraw.point(p.x(), p.y());
        }
    }
    
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect)      
    {
        if (rect == null) throw new NullPointerException();
        
        SET<Point2D> rangePoints = new SET<Point2D>();
        
        for (Point2D p : points)
        {
            if (rect.contains(p)) rangePoints.add(p);
        }
        return rangePoints;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)   
    {
        if (p == null) throw new NullPointerException();
        double distance = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D pItem : points)
        {
            double thisDistance = p.distanceTo(pItem);
            if (distance > thisDistance) 
            {
                distance = thisDistance;
                nearestPoint = pItem;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args)    
    {
        In in = new In(args[0]);
        KdTree set = new KdTree();
        StdOut.println("set is empty: " + set.isEmpty());
        double[] coord = in.readAllDoubles();
        Point2D p2 = null;
        for (int i = 0; i < coord.length-1; i+=2)
        {
            Point2D p = new Point2D(coord[i], coord[i+1]);
            if (i == 2) p2 = p;
            StdOut.println(p);
            set.insert(p);
        }
        
        
        
        StdOut.println("set size = 10 : " + set.size());
        
        StdOut.println("set is not empty : " + set.isEmpty());
        
        Point2D p1 = new Point2D(1000.0,2.0);
        StdOut.println("set not contains point : " + set.contains(p1));
        
        StdOut.println("set contains point : " + set.contains(p2));
        
        RectHV rect = new RectHV(0.01, 0.01, 0.99, 0.99);
        SET<Point2D> rangePoints = (SET<Point2D>) set.range(rect);
        StdOut.println("range : " + rect);
        for (Point2D p : rangePoints)
        {
            StdOut.println(p);
        }
        
        Point2D p3 = set.nearest(new Point2D(0.1, 0.3));
        StdOut.println("nearest point is : " + p3);
        
        set.draw();
    }
}
