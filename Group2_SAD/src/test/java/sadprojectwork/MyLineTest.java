package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLineTest {

    private MyLine line;

    // this method runs before each test, initializing a MyLine instance
    @BeforeEach
    void setUp() {
        line = new MyLine(10, 20, 30, 40);
    }

    // tests that the Line is correctly created and returned by getFxShape()
    @Test
    void testConstructorAndGetFxShape() {
        Shape shape = line.getFxShape();
        assertInstanceOf(Line.class, shape);

        Line fxLine = (Line) shape;
        assertEquals(10, fxLine.getStartX());
        assertEquals(20, fxLine.getStartY());
        assertEquals(30, fxLine.getEndX());
        assertEquals(40, fxLine.getEndY());
        assertEquals(3.0, fxLine.getStrokeWidth());
        assertTrue(fxLine.isPickOnBounds());
    }

    // tests the resize() method to ensure it sets the new end coordinates
    @Test
    void testResize() {
        line.resize(50, 60);
        Line fxLine = (Line) line.getFxShape();
        assertEquals(50, fxLine.getEndX());
        assertEquals(60, fxLine.getEndY());
    }

    // tests resizing to the same point as the start (zero-length line)
    @Test
    void testResizeToSamePoint() {
        line.resize(10, 20);
        Line fxLine = (Line) line.getFxShape();
        assertEquals(10, fxLine.getEndX());
        assertEquals(20, fxLine.getEndY());
        assertEquals(0, line.getWidth());
        assertEquals(0, line.getHeight());
    }

    // tests that setPosition moves both the start and end points correctly
    @Test
    void testSetPositionMovesBothPoints() {
        line.resize(30, 40); // ensure the line has an end point different from the start
        line.setPosition(20, 30);

        Line fxLine = (Line) line.getFxShape();
        assertEquals(20, fxLine.getStartX());
        assertEquals(30, fxLine.getStartY());
        assertEquals(40, fxLine.getEndX()); // previous endX (30) + deltaX (10)
        assertEquals(50, fxLine.getEndY()); // previous endY (40) + deltaY (10)
    }

    // tests that cloneShape returns a new independent object with the same coordinates
    @Test
    void testCloneShapeCreatesIndependentCopy() {
        MyLine clone = (MyLine) line.cloneShape();
        assertNotSame(clone, line); // different instances
        assertNotSame(clone.getFxShape(), line.getFxShape()); // different Line objects

        Line original = (Line) line.getFxShape();
        Line copied = (Line) clone.getFxShape();

        // check that coordinates are the same
        assertEquals(original.getStartX(), copied.getStartX());
        assertEquals(original.getStartY(), copied.getStartY());
        assertEquals(original.getEndX(), copied.getEndX());
        assertEquals(original.getEndY(), copied.getEndY());
    }

    // tests getWidth() returns the horizontal distance between start and end
    @Test
    void testGetWidth() {
        assertEquals(20, line.getWidth()); // endX - startX = 30 - 10
    }

    // tests getHeight() returns the vertical distance between start and end
    @Test
    void testGetHeight() {
        assertEquals(20, line.getHeight()); // endY - startY = 40 - 20
    }

    // tests that toCSV() generates the expected format, and checks color values
    @Test
    void testToCSVFormat() {
        line.getFxShape().setStroke(Color.BLACK); // set stroke color explicitly
        String csv = line.toCSV();

        // check the CSV format starts as expected
        assertTrue(csv.startsWith("LINE;10.0;20.0;30.0;40.0;"));

        // ensure the number of fields is correct
        String[] parts = csv.split(";");
        assertEquals(7, parts.length);

        // for lines, stroke color is used both as fill and stroke
        assertEquals(parts[5], parts[6]);
    }

}
