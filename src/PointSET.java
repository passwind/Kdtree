import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private SET<Point2D> points;
    
    // construct an empty set of points 
    public PointSET()             
    {
        points = new SET<Point2D>();
    }
    
    // is the set empty? 
    public boolean isEmpty()   
    {
        return points.size() == 0;
    }
    
    // number of points in the set 
    public int size()       
    {                 
        return points.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)        
    {
        if (p == null) throw new NullPointerException();
        points.add(p);
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
        PointSET set = new PointSET();
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
