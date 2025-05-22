
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
        if (points == null) {
            this.points = new ArrayList<>(); 
        }
        else {
            this.points = points;
        }
        path = new Path();
        path.setStrokeWidth(3);
        addMoveTo(startX, startY);
        this.fxShape = path;
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
  
    
    @Override
    public void resize(double newFirstDim, double newSecondDim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public String toCSV() {
        StringBuffer pointsList = new StringBuffer(String.format("%d:", points.size()));
        for(Point2D p : points) {
            pointsList.append(String.format("%f,%f/", p.getX(), p.getY()));
        }
        pointsList.deleteCharAt(pointsList.length() - 1);
        return Shapes.POLYGON + ";" + smallerX + ";" + greaterY + ";" + getWidth() + ";" + getHeight() + ";" + path.getFill() + ";" + path.getStroke() + ";" + pointsList;
    }

    @Override
    public void moveTo(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
