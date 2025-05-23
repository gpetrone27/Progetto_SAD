
package shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

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
        
        if (points == null) {
            this.points = new ArrayList<>();
            addMoveTo(startX, startY);
        }
        else {
            this.points = points;
            reconstruct();
        }
        
        this.fxShape = path;
        this.setRotation(rotation);
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
        Point2D firstPoint = points.get(0);
        path.getElements().add(new MoveTo(firstPoint.getX(), firstPoint.getY()));
        
        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i);
            path.getElements().add(new LineTo(point.getX(), point.getY()));
        }
        path.getElements().add(new ClosePath());
    }
    
    @Override
    public void resize(double newWidth, double newHeight) {
        // TO DO
    }

    @Override
    public MyShape cloneShape() {
        List<Point2D> clonedPoints = new ArrayList<>();
        for(Point2D p : points) {
            clonedPoints.add(new Point2D(p.getX(), p.getY()));
        }
        return new MyPolygon(startX, startY, clonedPoints);
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
        double dX = x - startX;
        double dY = y - startY;
        
        int i = 0;
        
        for (PathElement elem : path.getElements()) {
            if (elem instanceof MoveTo moveTo) {
                moveTo.setX(moveTo.getX() + dX);
                moveTo.setY(moveTo.getY() + dY);
                Point2D p = points.get(i);
                points.set(i, new Point2D(p.getX() + dX, p.getY() + dY));
                i++;
            } else if (elem instanceof LineTo lineTo) {
                lineTo.setX(lineTo.getX() + dX);
                lineTo.setY(lineTo.getY() + dY);
                Point2D p = points.get(i);
                points.set(i, new Point2D(p.getX() + dX, p.getY() + dY));
                i++;
            }            
        }
        startX += dX;
        startY += dY;

        smallerX += dX;
        greaterX += dX;
        smallerY += dY;
        greaterY += dY;
    }
    
    @Override
    public String toCSV() {
        StringBuffer pointsList = new StringBuffer();
        for(Point2D p : points) {
            pointsList.append(Double.toString(p.getX()));
            pointsList.append("-");
            pointsList.append(Double.toString(p.getY()));
            pointsList.append("/");
        }
        pointsList.deleteCharAt(pointsList.length() - 1);
        return Shapes.POLYGON + ";" + startX + ";" + startY + ";" + getWidth() + ";" + getHeight() + ";" + path.getFill() + ";" + path.getStroke() + ";" + pointsList;
    }

}
