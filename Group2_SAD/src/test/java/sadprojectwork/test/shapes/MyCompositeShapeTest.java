package sadprojectwork.test.shapes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.shapes.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyCompositeShapeTest {

    private MyCompositeShape composite;
    private MyShape shape1;
    private MyShape shape2;

    @BeforeEach
    public void setUp() {
        composite = new MyCompositeShape(0, 0);
        shape1 = new MyRectangle(10, 10, 20, 20, 0);
        shape2 = new MyEllipse(40, 40, 10, 10, 45);
    }
    
    /**
     * Tests the addShape method to verify a shape is correctly added to the composite.
     */
    @Test
    public void testAddShape() {
        composite.addShape(shape1);
        assertEquals(1, composite.getShapes().size());
        assertTrue(composite.getShapes().contains(shape1));
    }

    /**
     * Tests the removeShape method to ensure a shape is properly removed.
     */
    @Test
    public void testRemoveShape() {
        composite.addShape(shape1);
        composite.removeShape(shape1);
        assertTrue(composite.getShapes().isEmpty());
    }

    /**
     * Tests the getShapes method to verify the returned list contains the added shapes.
     */
    @Test
    public void testGetShapes() {
        composite.addShape(shape1);
        composite.addShape(shape2);
        List<MyShape> shapes = composite.getShapes();
        assertEquals(2, shapes.size());
        assertTrue(shapes.contains(shape1));
        assertTrue(shapes.contains(shape2));
    }

    /**
     * Tests the getShapes method to verify the returned list contains the added shapes.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(composite.isEmpty());
        composite.addShape(shape1);
        assertFalse(composite.isEmpty());
    }

    /**
     * Tests the clear method to ensure all shapes are removed from the composite.
     */
    @Test
    public void testClear() {
        composite.addShape(shape1);
        composite.clear();
        assertTrue(composite.getShapes().isEmpty());
    }
    
    /**
     * Tests the resize method to verify that shapes are resized proportionally.
     */
    @Test
    public void testResize() {
        composite.addShape(shape1);
        composite.resize(40, 40);  
        MyShape resized = composite.getShapes().get(0);
        assertEquals(40, resized.getWidth());
        assertEquals(40, resized.getHeight());
        assertEquals(10, composite.getStartX()); 
        assertEquals(10, composite.getStartY());
    }

    /**
     * Tests the moveTo method to ensure shapes are moved by the correct offset.
     */
    @Test
    public void testMoveTo() {
        composite.addShape(shape1);
        composite.moveTo(50, 50);
        assertEquals(50, composite.getStartX());
        assertEquals(50, composite.getStartY());
        MyShape moved = composite.getShapes().get(0);
        assertEquals(50, moved.getStartX());
        assertEquals(50, moved.getStartY());
    }

    /**
     * Tests the cloneShape method to verify a deep copy of the composite is made.
     */
    @Test
    public void testCloneShape() {
        composite.addShape(shape1);
        MyCompositeShape clone = (MyCompositeShape) composite.cloneShape();
        assertEquals(1, clone.getShapes().size());
        assertNotSame(composite.getShapes().get(0), clone.getShapes().get(0));
        assertEquals(composite.getShapes().get(0).getWidth(), clone.getShapes().get(0).getWidth());
    }
    
    /**
     * Tests the setRotation and getRotation methods to ensure rotation is propagated to children.
     */
    @Test
    public void testSetAndGetRotation() {
        composite.addShape(shape1);
        composite.setRotation(45.0);
        assertEquals(45.0, composite.getRotation());
        assertEquals(45.0, shape1.getRotation());
    }

    /**
     * Tests the toCSV method to verify the CSV output matches the sum of children CSVs.
     */
    @Test
    public void testToCSV() {
        String csv = composite.toCSV();
        String lines[] = csv.split("\n");
        int i = 0;
        for (MyShape shape : composite.getShapes()) {
            assertEquals(lines[i], shape.toCSV());
        }        
    }

    /**
     * Tests the bounding box calculation of the composite after adding multiple shapes.
     */
    @Test
    public void testBoundsCalculation() {
        composite.addShape(shape1); // (10,10) - (30,30)
        composite.addShape(shape2); // (40,40) - (50,50)
        assertEquals(10, composite.getStartX());
        assertEquals(10, composite.getStartY());
        assertEquals(40, composite.getWidth());
        assertEquals(40, composite.getHeight());
    }
}
