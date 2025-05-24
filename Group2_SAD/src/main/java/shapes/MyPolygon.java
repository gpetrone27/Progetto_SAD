
package shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

/**
 * Represents a custom polygon shape made of Points2D using the JavaFX object Path.
 * The polygon supports basic transformations such as moving and resizing, and can be cloned or serialized to CSV.
 */
public class MyPolygon extends MyShape {
    
    private Path path;
    private List<Point2D> points; 
    private double smallerX;
    private double greaterX;
    private double smallerY;
    private double greaterY;
  
    /**
     * Constructs a MyPolygon object.
     *
     * @param startX    The initial X coordinate
     * @param startY    The initial Y coordinate
     * @param points    The list of polygon points (can be null to start an empty polygon)
     * @param rotation  The initial rotation of the polygon
     */
    public MyPolygon(double startX, double startY, List<Point2D> points, double rotation) {
        
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
        setRotation(rotation);
    }
    
    /**
     * Adds the first point of the polygon
     *
     * @param x The X coordinate of the point
     * @param y The Y coordinate of the point
     */    
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
    
    /**
     * Adds a line to the polygon or closes the shape if the point is close to the starting point.
     *
     * @param x The X coordinate of the new point
     * @param y The Y coordinate of the new point
     * @return true if the shape was closed, false otherwise
     */
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
    
    /**
     * Reconstructs the polygon's {@link Path} from the current list of points.
     * 
     * This method clears the path and rebuilds it by creating a {@link MoveTo} element
     * for the first point and {@link LineTo} elements for each subsequent point, 
     * followed by a {@link ClosePath} to close the shape.
     * 
     * It is typically used when loading or cloning an existing polygon.
     */   
    private void reconstruct() {
        Point2D firstPoint = points.get(0);
        path.getElements().add(new MoveTo(firstPoint.getX(), firstPoint.getY()));
        
        for (int i = 1; i < points.size(); i++) {
            Point2D point = points.get(i);
            path.getElements().add(new LineTo(point.getX(), point.getY()));
        }
        path.getElements().add(new ClosePath());
    }
    
    /**
     * Closes the polygon.
     */
    public void closeShape() {
        path.getElements().add(new ClosePath());
    }
    

    /**
     * Resizes the polygon proportionally based on new width and height,
     * keeping the top-left corner fixed.
     *
     * @param newWidth  The new width
     * @param newHeight The new height
     */    
    @Override
    public void resize(double newWidth, double newHeight) {
        double oldWidth = getWidth();
        double oldHeight = getHeight();

        if (oldWidth == 0 || oldHeight == 0) return;

        double scaleX = newWidth / oldWidth;
        double scaleY = newHeight / oldHeight;

        // The top-left point remains fixed
        Point2D ref = new Point2D(smallerX, smallerY);
        List<Point2D> newPoints = new ArrayList<>();

        path.getElements().clear();

        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            double newX = ref.getX() + (p.getX() - ref.getX()) * scaleX;
            double newY = ref.getY() + (p.getY() - ref.getY()) * scaleY;
            Point2D scaled = new Point2D(newX, newY);
            newPoints.add(scaled);

            if (i == 0) {
                path.getElements().add(new MoveTo(newX, newY));
            } else {
                path.getElements().add(new LineTo(newX, newY));
            }
        }

        path.getElements().add(new ClosePath());

        points = newPoints;
        
        recomputeBounds();
}

    
    /**
     * Recomputes the bounding box coordinates based on the current points.
     */
    private void recomputeBounds() {
        if (points == null || points.isEmpty()) {
            smallerX = startX;
            greaterX = startX;
            smallerY = startY;
            greaterY = startY;
            return;
        }

        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Point2D p : points) {
            double x = p.getX();
            double y = p.getY();

            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            
        }

        smallerX = minX;
        greaterX = maxX;
        smallerY = minY;
        greaterY = maxY;
    }

    
    /**
     * Clones the current shape, including its points and rotation.
     *
     * @return A new instance of {@link MyPolygon} identical to the current one
     */
    @Override
    public MyShape cloneShape() {
        List<Point2D> clonedPoints = new ArrayList<>();
        for(Point2D p : points) {
            clonedPoints.add(new Point2D(p.getX(), p.getY()));
        }
        return new MyPolygon(startX, startY, clonedPoints, path.getRotate());
    }
    /**
     * Returns the width of the polygon's bounding box.
     *
     * @return The width
     */
    @Override
    public double getWidth() {
       return greaterX - smallerX;
    }

    /**
     * Returns the height of the polygon's bounding box.
     *
     * @return The height
     */
    @Override
    public double getHeight() {
        return greaterY - smallerY;
    }

    /**
     * Moves the entire polygon to a new position based on the given coordinates.
     *
     * @param x The new X coordinate
     * @param y The new Y coordinate
     */
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
    
    /**
    * Serializes the polygon to a CSV format string.
    *
    * @return A CSV string representing the polygon's data
    */
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
        return Shapes.POLYGON + ";" + startX + ";" + startY + ";" + getWidth() + ";" + getHeight() + ";" + path.getFill() + ";" + path.getStroke() + ";" + path.getRotate() + ";" + pointsList;
    }

}