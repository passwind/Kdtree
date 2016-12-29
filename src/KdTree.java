import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static final boolean VERTICAL = true;
//    private final static boolean HORIZONTAL = false;
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
        return root == null;
    }
    
    // number of points in the set 
    public int size()       
    {                 
        return n;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)        
    {
        if (root == null) 
        {
            root = makeNode(null, p);
            n++;
        }
        else insert(root, p);
    }
    
    private void insert(Node parent, Point2D p) {
        if (parent.point.equals(p)) return;
        
        if (less(p, parent)) 
        {
            if (parent.left == null) 
            {
                parent.left = makeNode(parent, p);
                n++;
            }
            else insert(parent.left, p);
        }
        else
        {
            if (parent.right == null) 
            {
                parent.right = makeNode(parent, p);
                n++;
            }
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
        return equals(root, p);
    }
    
    private boolean equals(Node node, Point2D p)
    {
        if (node == null) return false;
        if (node.point.equals(p)) return true;
        if (less(p, node)) return equals(node.left, p);
        else return equals(node.right, p);
    }

    // draw all points to standard draw 
    public void draw()        
    {
        draw(root, 0.0, 0.0, 1.0, 1.0);
    }
    
    private void draw(Node node, double xmin, double ymin, double xmax, double ymax)
    {
        if (node == null) return;
        
        // draw current node
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y());
        
        StdDraw.setPenRadius();
        if (node.direction == VERTICAL)
        {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), ymin, node.point.x(), ymax);
            draw(node.left, xmin, ymin, node.point.x(), ymax);
            draw(node.right, node.point.x(), ymin, xmax, ymax);
        }
        else 
        {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin, node.point.y(), xmax, node.point.y());
            draw(node.left, xmin, ymin, xmax, node.point.y());
            draw(node.right, xmin, node.point.y(), xmax, ymax);
        }
    }

    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect)      
    {
        if (rect == null) throw new NullPointerException();
        
        SET<Point2D> rangePoints = new SET<Point2D>();
        
        RectHV nodeRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        range(rect, root, rangePoints, nodeRect);
        
        return rangePoints;
    }
    
    private void range(RectHV rect, Node node, SET<Point2D> set, RectHV nodeRect)
    {
        if (node == null) return;
        if (!rect.intersects(nodeRect)) return;
        
        if (rect.contains(node.point)) set.add(node.point);
        RectHV nodeRect1;
        RectHV nodeRect2;
        if (node.direction == VERTICAL)
        {
            nodeRect1 = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.point.x(), nodeRect.ymax());
            nodeRect2 = new RectHV(node.point.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
        }
        else
        {
            nodeRect1 = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.point.y());
            nodeRect2 = new RectHV(nodeRect.xmin(), node.point.y(), nodeRect.xmax(), nodeRect.ymax());
        }
        range(rect, node.left, set, nodeRect1);
        range(rect, node.right, set, nodeRect2);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)   
    {
        if (p == null) throw new NullPointerException();
        
        return nearestPoint(root, p, Double.MAX_VALUE);
    }

    private Point2D nearestPoint(Node node, Point2D p, double minDist)
    {
        if (node == null) return null;
        Point2D np = node.point;
        double dist = p.distanceTo(np);
        if (dist > minDist) return node.point;
        
        Node nextSearchNode = node.left;
        Node nextSearchNode1 = node.right; 
        if (!less(p, node))
        {
            nextSearchNode = node.right;
            nextSearchNode1 = node.left;
        }
        
        Point2D np1 = nearestPoint(nextSearchNode, p, dist);
        if (np1 != null) 
        {
            double dist1 = p.distanceTo(np1);
            if (dist1 < dist) 
            {
                dist = dist1;
                np = np1;
            }
        }
        
        Point2D np2 = nearestPoint(nextSearchNode1, p, dist);
        if (np2 != null)
        {
            double dist2 = p.distanceTo(np2);
            if (dist2 < dist) np = np2;
        }
        
        return np;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args)    
    {
        In in = new In(args[0]);
        KdTree set = new KdTree();
        StdOut.println("set is empty: " + set.isEmpty());
        double[] coord = in.readAllDoubles();
        Point2D p2 = null;
        for (int i = 0; i < coord.length-1; i += 2)
        {
            Point2D p = new Point2D(coord[i], coord[i+1]);
            if (i == 2) p2 = p;
            StdOut.println(p);
            set.insert(p);
        }
        
        StdOut.println("set size = 10 : " + set.size());
        
        StdOut.println("set is not empty : " + set.isEmpty());
        
        Point2D p1 = new Point2D(1000.0, 2.0);
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
