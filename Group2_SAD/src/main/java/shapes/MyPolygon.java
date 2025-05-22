
package shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class MyPolygon extends MyShape {
    
    private Path path;
    private List<Point2D> points; 
    private double smallerX = 0;
    private double greaterX = 0;
    private double smallerY = 0;
    private double greaterY = 0;
    
    public MyPolygon(double startX, double startY, List<Point2D> points) {
        
        super(startX, startY);
        
        path = new Path();
        path.setStrokeWidth(3);
        addMoveTo(startX, startY);
        this.fxShape = path;
        
        if (points == null) {
            this.points = new ArrayList<>(); 
        }
        else {
            this.points = points;
            reconstruct();
        }
    }
    
    public void addMoveTo(double x, double y) {
        path.getElements().add(new MoveTo(x, y));
        points.add(new Point2D(x, y));
        if(x < smallerX) {
            smallerX = x;
        } else if(x > greaterX) {
            greaterX  = x;
        }
        
        if(y < smallerY) {
            smallerY = y;
        } else if(y > greaterY) {
          greaterY = y;  
        }
    }
    
    public boolean addLineTo(double x, double y) {
        if (Math.abs(x - startX) <= 10 && Math.abs(y - startY) <= 10) {
            path.getElements().add(new ClosePath());
            return true;
        }
        else {
            path.getElements().add(new LineTo(x, y));
            points.add(new Point2D(x, y));
            return false;
        }
    }
    
    private void reconstruct() {
        Point2D firstPoint = points.remove(0);
        addMoveTo(firstPoint.getX(), firstPoint.getY());
        for (Point2D point : points) {
            addLineTo(point.getX(), point.getY());
        }
        path.getElements().add(new ClosePath());
    }
    
    @Override
    public void resize(double newWidth, double newHeight) {
        // TO DO
    }

    @Override
    public MyShape cloneShape() {
        return new MyPolygon(smallerX, greaterY, points);
    }

    @Override
    public double getWidth() {
       return greaterX - smallerX;
    }

    @Override
    public double getHeight() {
        return greaterY - smallerY;
    }

    @Override
    public void moveTo(double x, double y) {
        // TO DO
    }
    
    @Override
    public String toCSV() {
        StringBuffer pointsList = new StringBuffer(String.format("%d:", points.size()));
        for(Point2D p : points) {
            pointsList.append(String.format("%f,%f/", p.getX(), p.getY()));
        }
        pointsList.deleteCharAt(pointsList.length() - 1);
        return Shapes.POLYGON + ";" + startX + ";" + startY + ";" + getWidth() + ";" + getHeight() + ";" + path.getFill() + ";" + path.getStroke() + ";" + pointsList;
    }

}
