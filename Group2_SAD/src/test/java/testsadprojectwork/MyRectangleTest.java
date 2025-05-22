package testsadprojectwork;

import shapes.MyRectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyRectangleTest {

    private MyRectangle rect;

    // Runs before each test: create a rectangle at (10, 20) with width=100 and height=50
    @BeforeEach
    void setUp() {
        rect = new MyRectangle(10, 20, 100, 50);
    }

    // Tests that getFxShape returns a JavaFX Rectangle with correct properties
    @Test
    void testConstructorAndGetFxShape() {
        Shape shape = rect.getFxShape();
        assertInstanceOf(Rectangle.class, shape);

        Rectangle r = (Rectangle) shape;
        assertEquals(10, r.getX());
        assertEquals(20, r.getY());
        assertEquals(100, r.getWidth());
        assertEquals(50, r.getHeight());
        assertEquals(3.0, r.getStrokeWidth());
    }

    // Tests resizing the rectangle with positive width and height
    @Test
    void testResizePositive() {
        rect.resize(60, 30);
        Rectangle r = (Rectangle) rect.getFxShape();
        assertEquals(10, r.getX());
        assertEquals(20, r.getY());
        assertEquals(60, r.getWidth());
        assertEquals(30, r.getHeight());
    }

    // Tests resizing with a negative width: rectangle should "flip" left
    @Test
    void testResizeNegativeWidth() {
        rect.resize(-40, 30);
        Rectangle r = (Rectangle) rect.getFxShape();
        assertEquals(10 - 40, r.getX());
        assertEquals(20, r.getY());
        assertEquals(40, r.getWidth());
        assertEquals(30, r.getHeight());
    }

    // Tests resizing with a negative height: rectangle should "flip" up
    @Test
    void testResizeNegativeHeight() {
        rect.resize(50, -25);
        Rectangle r = (Rectangle) rect.getFxShape();
        assertEquals(10, r.getX());
        assertEquals(20 - 25, r.getY());
        assertEquals(50, r.getWidth());
        assertEquals(25, r.getHeight());
    }

    // Tests resizing with both width and height negative: should flip both ways
    @Test
    void testResizeNegativeWidthAndHeight() {
        rect.resize(-60, -40);
        Rectangle r = (Rectangle) rect.getFxShape();
        assertEquals(10 - 60, r.getX());
        assertEquals(20 - 40, r.getY());
        assertEquals(60, r.getWidth());
        assertEquals(40, r.getHeight());
    }

    // Tests cloneShape returns a copy with the same geometry, but as a new instance
    @Test
    void testCloneShapeCreatesCopy() {
        MyRectangle copy = (MyRectangle) rect.cloneShape();
        assertNotSame(rect, copy);
        assertNotSame(rect.getFxShape(), copy.getFxShape());

        Rectangle r1 = (Rectangle) rect.getFxShape();
        Rectangle r2 = (Rectangle) copy.getFxShape();

        assertEquals(r1.getX(), r2.getX());
        assertEquals(r1.getY(), r2.getY());
        assertEquals(r1.getWidth(), r2.getWidth());
        assertEquals(r1.getHeight(), r2.getHeight());
    }

    // Tests setPosition updates X and Y, but not width or height
    @Test
    void testSetPosition() {
        rect.setPosition(50, 60);
        Rectangle r = (Rectangle) rect.getFxShape();
        assertEquals(50, r.getX());
        assertEquals(60, r.getY());
        assertEquals(100, r.getWidth());
        assertEquals(50, r.getHeight());
    }

    // Tests getWidth returns current rectangle width
    @Test
    void testGetWidth() {
        assertEquals(100, rect.getWidth());
    }

    // Tests getHeight returns current rectangle height
    @Test
    void testGetHeight() {
        assertEquals(50, rect.getHeight());
    }

    // Tests toCSV returns expected string format
    @Test
    void testToCSVFormat() {
        Rectangle r = (Rectangle) rect.getFxShape();
        r.setFill(Color.RED);
        r.setStroke(Color.BLACK);

        String csv = rect.toCSV();
        String[] parts = csv.split(";");

        assertEquals(7, parts.length);

        // Parse the string color representations back to Color objects
        Color fillColor = Color.valueOf(parts[5]);
        Color strokeColor = Color.valueOf(parts[6]);

        // Use JavaFX's color equality
        assertEquals(Color.RED, fillColor);
        assertEquals(Color.BLACK, strokeColor);
    }
}
