
package sadprojectwork.test.decorator;

import sadprojectwork.decorator.FillColorDecorator;
import sadprojectwork.shapes.MyRectangle;
import sadprojectwork.shapes.MyEllipse;
import sadprojectwork.shapes.MyShape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FillColorDecoratorTest {

    private MyShape baseShape;
    private FillColorDecorator decorated;

    // Set up a base shape (rectangle) and decorate it with a fill color
    @BeforeEach
    void setUp() {
        baseShape = new MyRectangle(0, 0, 100, 50, 0);
        decorated = new FillColorDecorator(baseShape, Color.RED);
    }

    // Test that the decorator correctly applies the fill color to the shape
    @Test
    void testSetFillColor() {
        Shape shape = decorated.getFxShape();
        assertEquals(Color.RED, shape.getFill());
    }

    // Test that getFillColor() returns the color passed in the constructor
    @Test
    void testGetFillColor() {
        assertEquals(Color.RED, decorated.getFillColor());
    }

    // Test that cloneShape returns a new decorator with a cloned shape and same fill color
    @Test
    void testCloneShapeCreatesCopy() {
        FillColorDecorator copy = (FillColorDecorator) decorated.cloneShape();

        // Ensure it's a new object, not the same reference
        assertNotSame(copy, decorated);
        assertNotSame(copy.getFxShape(), decorated.getFxShape());

        // Check that the fill color remains the same
        assertEquals(decorated.getFillColor(), copy.getFxShape().getFill());
    }

    // Test that toCSV delegates to the decorated shape and includes the fill color
    @Test
    void testToCSVDelegatesCorrectly() {
        String csv = decorated.toCSV();
        assertEquals(baseShape.toCSV(), csv);  // Should match the base shape's CSV
    }

    @Test
    void testEllipseFillColor() {
        MyShape ellipse = new MyEllipse(50, 50, 20, 10, 0);
        FillColorDecorator decoratedEllipse = new FillColorDecorator(ellipse, Color.GREEN);
        assertEquals(Color.GREEN, decoratedEllipse.getFxShape().getFill());
        assertEquals(Color.GREEN, decoratedEllipse.getFillColor());
    }
}
