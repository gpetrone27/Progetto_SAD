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

        assertEquals(65, fx.getCenterX());
        assertEquals(70, fx.getCenterY());
        assertEquals(15, fx.getRadiusX());
        assertEquals(10, fx.getRadiusY());
    }

    // Test resizing with positive radii - should adjust center and radii
    @Test
    void testResizePositive() {

        ellipse.resize(30, 20);
        Ellipse fx = (Ellipse) ellipse.getFxShape();

        assertEquals(65, fx.getCenterX());
        assertEquals(70, fx.getCenterY());
        assertEquals(15, fx.getRadiusX());
        assertEquals(10, fx.getRadiusY());
    }

    // Test resizing with negative values - should flip direction but use absolute radii
    @Test
    void testResizeNegative() {
        ellipse.resize(-30, -40); // dovrebbe capovolgere ma usare valori assoluti
        Ellipse fx = (Ellipse) ellipse.getFxShape();

        assertEquals(35, fx.getCenterX(), 1e-6); // 50 + (-30 / 2) = 50 - 15
        assertEquals(40, fx.getCenterY(), 1e-6); // 60 + (-40 / 2) = 60 - 20
        assertEquals(15, fx.getRadiusX(), 1e-6); // abs(-30 / 2)
        assertEquals(20, fx.getRadiusY(), 1e-6); // abs(-40 / 2)
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

    // Test moveTo sets the new center correctly
    @Test
    void testMoveTo() {
        ellipse.moveTo(100, 200);  // sposta in (100, 200)

        Ellipse fx = (Ellipse) ellipse.getFxShape();
        // Verifica che startX e startY siano aggiornati
        assertEquals(100, ellipse.getStartX());
        assertEquals(200, ellipse.getStartY());

        // rX = 15, rY = 10 → centro = (100 + 15, 200 + 10) = (115, 210)
        assertEquals(115, fx.getCenterX());
        assertEquals(210, fx.getCenterY());
    }

    // getWidth should return radiusX
    @Test
    void testGetWidth() {
        assertEquals(30, ellipse.getWidth());
    }

    // getHeight should return radiusY
    @Test
    void testGetHeight() {
        assertEquals(20, ellipse.getHeight());
    }

    // Test the CSV format including color fields using Color.valueOf()
    @Test
    void testToCSVFormat() {
        Ellipse fx = (Ellipse) ellipse.getFxShape();
        fx.setFill(Color.YELLOW);
        fx.setStroke(Color.BLUE);

        String csv = ellipse.toCSV();
        String[] parts = csv.split(";");

        assertEquals(9, parts.length);

        Color fill = Color.valueOf(parts[5]);
        Color stroke = Color.valueOf(parts[6]);

        assertEquals(Color.YELLOW, fill);
        assertEquals(Color.BLUE, stroke);
    }

}
