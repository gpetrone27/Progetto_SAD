package testsadprojectwork;

import decorator.BorderColorDecorator;
import shapes.MyRectangle;
import shapes.MyEllipse;
import shapes.MyLine;
import shapes.MyShape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorderColorDecoratorTest {

    private MyShape baseShape;
    private BorderColorDecorator decorated;

    // Set up a base shape (rectangle) and decorate it with a border color
    @BeforeEach
    void setUp() {
        baseShape = new MyRectangle(0, 0, 100, 50);
        decorated = new BorderColorDecorator(baseShape, Color.BLUE);
    }

    // Test that the decorator correctly applies the border color to the shape
    @Test
    void testSetBorderColor() {
        Shape shape = decorated.getFxShape();
        assertEquals(Color.BLUE, shape.getStroke());
    }

    // Test that getBorderColor() returns the color passed in the constructor
    @Test
    void testGetBorderColor() {
        assertEquals(Color.BLUE, decorated.getBorderColor());
    }

    // Test that cloneShape returns a new decorator with a cloned shape and same border color
    @Test
    void testCloneShapeCreatesCopy() {
        BorderColorDecorator copy = (BorderColorDecorator) decorated.cloneShape();

        // Ensure it's a new object, not the same reference
        assertNotSame(copy, decorated);
        assertNotSame(copy.getFxShape(), decorated.getFxShape());

        // Check that the stroke color remains the same
        assertEquals(decorated.getBorderColor(), copy.getFxShape().getStroke());
    }

    // Test that toCSV delegates to the decorated shape and includes the stroke color
    @Test
    void testToCSVDelegatesCorrectly() {
        String csv = decorated.toCSV();
        assertEquals(baseShape.toCSV(), csv);  // Should match the base shape's CSV
    }

    // Test BorderColorDecorator with a Rectangle shape
    @Test
    void testRectangleBorderColor() {
        MyShape rect = new MyRectangle(0, 0, 100, 50);
        BorderColorDecorator decorated = new BorderColorDecorator(rect, Color.RED);

        Shape fxShape = decorated.getFxShape();
        assertInstanceOf(Rectangle.class, fxShape);
        assertEquals(Color.RED, fxShape.getStroke());
        assertEquals(Color.RED, decorated.getBorderColor());
    }

    // Test BorderColorDecorator with an Ellipse shape
    @Test
    void testEllipseBorderColor() {
        MyShape ellipse = new MyEllipse(50, 50, 20, 10);
        BorderColorDecorator decorated = new BorderColorDecorator(ellipse, Color.GREEN);

        Shape fxShape = decorated.getFxShape();
        assertInstanceOf(Ellipse.class, fxShape);
        assertEquals(Color.GREEN, fxShape.getStroke());
        assertEquals(Color.GREEN, decorated.getBorderColor());
    }

    // Test BorderColorDecorator with a Line shape
    @Test
    void testLineBorderColor() {
        MyShape line = new MyLine(0, 0, 50, 50);
        BorderColorDecorator decorated = new BorderColorDecorator(line, Color.BLUE);

        Shape fxShape = decorated.getFxShape();
        assertInstanceOf(Line.class, fxShape);
        assertEquals(Color.BLUE, fxShape.getStroke());
        assertEquals(Color.BLUE, decorated.getBorderColor());
    }

    // Test cloneShape with a decorated Rectangle
    @Test
    void testCloneShapeWithRectangle() {
        MyShape rect = new MyRectangle(10, 10, 40, 20);
        BorderColorDecorator decorated = new BorderColorDecorator(rect, Color.BLACK);
        BorderColorDecorator clone = (BorderColorDecorator) decorated.cloneShape();

        assertNotSame(decorated, clone);
        assertNotSame(decorated.getFxShape(), clone.getFxShape());
        assertEquals(decorated.getBorderColor(), clone.getFxShape().getStroke());
    }

    // Test toCSV delegates correctly for an Ellipse
    @Test
    void testToCSVWithEllipse() {
        MyShape ellipse = new MyEllipse(100, 100, 30, 15);
        BorderColorDecorator decorated = new BorderColorDecorator(ellipse, Color.PURPLE);

        String baseCSV = ellipse.toCSV();
        String decoratedCSV = decorated.toCSV();

        assertEquals(baseCSV, decoratedCSV); // CSV should match the base shape
    }
}
