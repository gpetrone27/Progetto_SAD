package testsadprojectwork;

import shapes.MyEllipse;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyEllipseTest {

    private MyEllipse ellipse;

    // Set up a default ellipse centered at (50, 60) with radiusX=30 and radiusY=20
    @BeforeEach
    void setUp() {
        ellipse = new MyEllipse(50, 60, 30, 20);
    }

    // Verify that getFxShape returns a valid Ellipse with the correct initial values
    @Test
    void testConstructorAndGetFxShape() {
        Shape shape = ellipse.getFxShape();
        assertInstanceOf(Ellipse.class, shape);

        Ellipse fx = (Ellipse) shape;
        assertEquals(50, fx.getCenterX());
        assertEquals(60, fx.getCenterY());
        assertEquals(30, fx.getRadiusX());
        assertEquals(20, fx.getRadiusY());
        assertEquals(3.0, fx.getStrokeWidth());
    }

    // Test resizing with positive radii - should adjust center and radii
    @Test
    void testResizePositive() {
        ellipse.resize(10, 15);  // deltaX, deltaY from start
        Ellipse fx = (Ellipse) ellipse.getFxShape();

        assertEquals(60, fx.getCenterX()); // startX 50 + 10
        assertEquals(75, fx.getCenterY()); // startY 60 + 15
        assertEquals(10, fx.getRadiusX());
        assertEquals(15, fx.getRadiusY());
    }

    // Test resizing with negative values - should flip direction but use absolute radii
    @Test
    void testResizeNegative() {
        ellipse.resize(-25, -30);
        Ellipse fx = (Ellipse) ellipse.getFxShape();

        assertEquals(25, fx.getCenterX()); // 50 + (-25)
        assertEquals(30, fx.getCenterY()); // 60 + (-30)
        assertEquals(25, fx.getRadiusX()); // abs(-25)
        assertEquals(30, fx.getRadiusY()); // abs(-30)
    }

    // Test cloneShape returns a new MyEllipse with same data but independent
    @Test
    void testCloneShapeCreatesCopy() {
        MyEllipse clone = (MyEllipse) ellipse.cloneShape();
        assertNotSame(clone, ellipse);
        assertNotSame(clone.getFxShape(), ellipse.getFxShape());

        Ellipse orig = (Ellipse) ellipse.getFxShape();
        Ellipse copy = (Ellipse) clone.getFxShape();

        assertEquals(orig.getCenterX(), copy.getCenterX());
        assertEquals(orig.getCenterY(), copy.getCenterY());
        assertEquals(orig.getRadiusX(), copy.getRadiusX());
        assertEquals(orig.getRadiusY(), copy.getRadiusY());
    }

    // Test setPosition sets the new center correctly
    @Test
    void testSetPosition() {
        ellipse.setPosition(100, 200);
        Ellipse fx = (Ellipse) ellipse.getFxShape();
        assertEquals(100, fx.getCenterX());
        assertEquals(200, fx.getCenterY());
    }

    // getWidth should return radiusX
    @Test
    void testGetWidth() {
        assertEquals(30, ellipse.getFirstDim());
    }

    // getHeight should return radiusY
    @Test
    void testGetHeight() {
        assertEquals(20, ellipse.getSecondDim());
    }

    // Test the CSV format including color fields using Color.valueOf()
    @Test
    void testToCSVFormat() {
        Ellipse fx = (Ellipse) ellipse.getFxShape();
        fx.setFill(Color.YELLOW);
        fx.setStroke(Color.BLUE);

        String csv = ellipse.toCSV();
        String[] parts = csv.split(";");

        assertEquals(7, parts.length);

        Color fill = Color.valueOf(parts[5]);
        Color stroke = Color.valueOf(parts[6]);

        assertEquals(Color.YELLOW, fill);
        assertEquals(Color.BLUE, stroke);
    }
}