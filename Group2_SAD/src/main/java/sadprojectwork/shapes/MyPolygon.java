
package sadprojectwork.shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;

/**
 * Represents a custom polygon shape using the JavaFX object Polyline.
 * The polygon supports basic transformations such as moving and resizing, and can be cloned or serialized to CSV.
 */
public class MyPolygon extends MyShape {
    
    // Strongly typed reference to wrapped FX shape
    private Polyline polygon;
  
    /**
     * Constructs a MyPolygon object.
     * @param startX    The initial X coordinate
     * @param startY    The initial Y coordinate
     * @param points    The list of polygon points (can be null to start an empty polygon)
     * @param rotation  The initial rotation of the polygon
     */
    public MyPolygon(double startX, double startY, List<Point2D> points, double rotation) {
        
        super(startX, startY);
        
        polygon = new Polyline(startX, startY);
        polygon.setStrokeWidth(3);
        
        if (points != null) {
            reconstruct(points);
        }
        
        this.fxShape = polygon;
        setRotation(rotation);
    }
    
    /**
     * Reconstructs the polygon from the current list of points.
     * It is typically used when loading or cloning an existing polygon.
     */   
    private void reconstruct(List<Point2D> points) {
        polygon.getPoints().clear();
        for (Point2D point : points) {
            polygon.getPoints().addAll(point.getX(), point.getY());
        }
    }

    /**
     * Adds the given point coordinates to the list of points of the polygon.
     * Returns true if the polygon is closed and false if it's still open.
     * @param point
     * @return polygon is closed
     */
    public boolean addPoint(Point2D point) {
        Point2D first = new Point2D(polygon.getPoints().get(0), polygon.getPoints().get(1));
        if (point.distance(first) < 10) {
            closePolygon();
            return true;
        }
        else {
            polygon.getPoints().addAll(point.getX(), point.getY());
            return false;
        }
    }
    
    /**
     * Closes the polygon.
     */
    public void closePolygon() {
        Point2D first = new Point2D(polygon.getPoints().get(0), polygon.getPoints().get(1));
        polygon.getPoints().addAll(first.getX(), first.getY());
        recomputeStartingPoint();
    }
    
    /**
     * Recomputes the starting point to the top-left corner of the bounding box.
     */
    public void recomputeStartingPoint() {
        
        List<Double> points = polygon.getPoints();

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        for (int i = 0; i < points.size(); i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
        }

        this.startX = minX;
        this.startY = minY;
    }
    
    /**
     * Resizes the polygon proportionally based on new width and height.
     * @param newWidth  The new width
     * @param newHeight The new height
     */    
    @Override
    public void resize(double newWidth, double newHeight) {
        
        List<Double> points = polygon.getPoints();

        // Find current bounds
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (int i = 0; i < points.size(); i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        double currentWidth = maxX - minX;
        double currentHeight = maxY - minY;

        if (currentWidth == 0 || currentHeight == 0) {
            return; // Avoid division by zero
        }

        double scaleX = newWidth / currentWidth;
        double scaleY = newHeight / currentHeight;

        // Scale points relative to top-left corner (minX, minY)
        for (int i = 0; i < points.size(); i += 2) {
            double x = points.get(i);
            double y = points.get(i + 1);

            double newX = minX + (x - minX) * scaleX;
            double newY = minY + (y - minY) * scaleY;

            points.set(i, newX);
            points.set(i + 1, newY);
        }

        // Update startX and startY to new position (top-left)
        this.startX = minX;
        this.startY = minY;
    }
    
    /**
     * Clones the current shape, including its points and rotation.
     * @return A new instance of {@link MyPolygon} identical to the current one.
     */
    @Override
    public MyShape cloneShape() {
        return new MyPolygon(startX, startY, convertToPoint2D(), getRotation());
    }
    
    /**
     * Converts the list of points of the polygon from (double, double) to Point2D.
     * @return 
     */
    public List<Point2D> convertToPoint2D() {
        List<Double> polygonPoints = polygon.getPoints();
        List<Point2D> clonedCoords = new ArrayList<>();
        for (int i = 0; i < polygonPoints.size(); i += 2) {
            clonedCoords.add(new Point2D(polygonPoints.get(i), polygonPoints.get(i + 1)));
        }
        return clonedCoords;
    }
    
    /**
     * Returns the width of the polygon by computing maxX - minX.
     * @return The width
     */
    @Override
    public double getWidth() {
       
        List<Double> polygonPoints = polygon.getPoints();
        
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        
        for (int i = 0; i < polygonPoints.size(); i += 2) {
            double x = polygonPoints.get(i);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        
        return maxX - minX;
    }

    /**
     * Returns the height of the polygon by computing maxY - minY.
     * @return The height
     */
    @Override
    public double getHeight() {
        
        List<Double> polygonPoints = polygon.getPoints();
        
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        
        for (int i = 0; i < polygonPoints.size(); i += 2) {
            double y = polygonPoints.get(i + 1);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        
        return maxY - minY;
    }

    /**
     * Moves the entire polygon to a new position based on the given coordinates.
     * @param x The new X coordinate
     * @param y The new Y coordinate
     */
    @Override
    public void moveTo(double x, double y) {
        
        List<Double> points = polygon.getPoints();

        // First, find current top-left corner (bounding box)
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i += 2) {
            minX = Math.min(minX, points.get(i));
            minY = Math.min(minY, points.get(i + 1));
        }

        // Calculate offset
        double offsetX = x - minX;
        double offsetY = y - minY;

        // Apply offset to all points
        for (int i = 0; i < points.size(); i += 2) {
            points.set(i, points.get(i) + offsetX);       // X
            points.set(i + 1, points.get(i + 1) + offsetY); // Y
        }
        
        // Updates stored starting points
        this.startX = x;
        this.startY = y;
    }
    
    /**
    * Serializes the polygon to a CSV format string.
    * @return A CSV string representing the polygon's data
    */
    @Override
    public String toCSV() {
        StringBuffer pointsList = new StringBuffer();
        for(Point2D p : convertToPoint2D()) {
            pointsList.append(Double.toString(p.getX()));
            pointsList.append("~");
            pointsList.append(Double.toString(p.getY()));
            pointsList.append("/");
        }
        pointsList.deleteCharAt(pointsList.length() - 1);
        return Shapes.POLYGON + ";" + startX + ";" + startY + ";" + getWidth() + ";" + getHeight() + ";" + polygon.getFill() + ";" + polygon.getStroke() + ";" + polygon.getRotate() + ";" + pointsList + ";null;null;null";
    }

}
