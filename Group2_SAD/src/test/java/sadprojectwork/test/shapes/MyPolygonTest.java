
package sadprojectwork.test.shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.shapes.MyPolygon;
import sadprojectwork.shapes.MyShape;
import static org.junit.jupiter.api.Assertions.*;
import sadprojectwork.shapes.Shapes;

public class MyPolygonTest {
    
    private MyPolygon emptyPolygon;
    private MyPolygon polygon;
    
    @BeforeEach
    public void setUp() {
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(10, 10));
        points.add(new Point2D(20, 10));
        points.add(new Point2D(15, 20));
        emptyPolygon = new MyPolygon(0, 0, null, 0);
        // Polygon with three points,with width = 10 and height = 10
        polygon = new MyPolygon(0, 0, points, 0);
    }
    
    /**
     * Test of getFxShape that returns a JavaFX Path
     */
    @Test
    void testGetFxShape() {
        Shape shape = emptyPolygon.getFxShape();
        assertInstanceOf(Polyline.class, shape);
    }
    
    /**
     * Test of getWidth method, of class MyPolygon.
     */
    @Test
    public void testGetWidth() {
        double width = polygon.getWidth();
        assertEquals(width, 10);
    }

    /**
     * Test of getHeight method, of class MyPolygon.
     */
    @Test
    public void testGetHeight() {
        double height = polygon.getHeight();
        assertEquals(height, 10);
    }

    /**
     * Test of resize method, of class MyPolygon.
     */
    @Test
    public void testResize() {
        
        double oldWidth = polygon.getWidth(); // 10
        double oldHeight = polygon.getHeight(); // 10
        double newWidth = oldWidth*2;
        double newHeight = oldHeight*2;
        
        polygon.resize(newWidth, newHeight);
        
        assertEquals(newWidth, polygon.getWidth());
        assertEquals(newHeight, polygon.getHeight()); 
        
        // Tests if the top-left point remains the same
        Point2D topLeft = polygon.convertToPoint2D().get(0);
        assertEquals(10, topLeft.getX());
        assertEquals(10, topLeft.getY());
    }
   
    /**
     * Test of cloneShape method, of class MyPolygon.
     */
    @Test
    public void testCloneShape() {
        MyShape cloned = polygon.cloneShape();
        assertTrue(cloned instanceof MyPolygon);
        MyPolygon clonedPolygon = (MyPolygon) cloned;
        
        // Checks if points are copied and not same references
        for (int i = 0; i < polygon.convertToPoint2D().size(); i++) {
            Point2D original = polygon.convertToPoint2D().get(i);
            Point2D clonePt = clonedPolygon.convertToPoint2D().get(i);
            assertEquals(original.getX(), clonePt.getX());
            assertEquals(original.getY(), clonePt.getY());
            assertNotSame(original, clonePt);
        }
    }

    /**
     * Test of toCSV method, of class MyPolygon.
     */
    @Test
    public void testToCSV() {
        String csv = polygon.toCSV();
        assertTrue(csv.contains(Shapes.POLYGON.toString()));
        assertTrue(csv.contains("10.0~10.0/20.0~10.0/15.0~20.0"));
    }
    
}
