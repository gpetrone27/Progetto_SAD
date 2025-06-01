package sadprojectwork.test.shapes;

import sadprojectwork.shapes.MyLine;
import sadprojectwork.shapes.MyShape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLineTest {

    private MyLine line;

    @BeforeEach
    void setUp() {
        // Initialize a line from (0,0) to (3,4) – length should be 5
        line = new MyLine(0, 0, 3, 4, 0);
    }

    @Test
    void testGetFxShape() {
        // Checks that getFxShape() returns the correct Line object
        Shape fx = line.getFxShape();
        assertInstanceOf(Line.class, fx);
        Line fxLine = (Line) fx;
        assertEquals(0, fxLine.getStartX()); 
        assertEquals(0, fxLine.getStartY());
        assertEquals(3, fxLine.getEndX());
        assertEquals(4, fxLine.getEndY());
    }

    @Test
    void testResize_correctLengthChange() {
        // Resizing the line to length 10 should keep direction and update end point
        line.resize(10, 0);
        assertEquals(10, line.getWidth());
        Line fx = (Line) line.getFxShape();
        assertEquals(10.0, fx.getEndX());
        assertEquals(0.0, fx.getEndY());
    }

    @Test
    void testResize_fromZeroLength() {
        // If the original line has zero length, it should extend horizontally to the right
        MyLine zeroLine = new MyLine(1, 1, 1, 1, 0);
        zeroLine.resize(5, 0);
        Line fx = (Line) zeroLine.getFxShape();
        assertEquals(6.0, fx.getEndX());
        assertEquals(1.0, fx.getEndY());
    }

    @Test
    void testResizeTo() {
        // resizeTo() should set the new end coordinates directly
        line.resizeTo(7, 2);
        Line fx = (Line) line.getFxShape();
        assertEquals(7.0, fx.getEndX());
        assertEquals(2.0, fx.getEndY());
    }
    
    @Test
    void testResizeLength() {
        
        line.resizeLength(10);

        Line fx = (Line) line.getFxShape();
    
        assertEquals(6.0, fx.getEndX());
        assertEquals(8.0, fx.getEndY());
    }

    @Test
    void testCloneShape() {
        // cloneShape() should return a new object with the same properties
        MyShape clone = line.cloneShape();
        assertTrue(clone instanceof MyLine);
        Line fx1 = (Line) line.getFxShape();
        Line fx2 = (Line) clone.getFxShape();
        assertEquals(fx1.getStartX(), fx2.getStartX());
        assertEquals(fx1.getStartY(), fx2.getStartY());
        assertEquals(fx1.getEndX(), fx2.getEndX());
        assertEquals(fx1.getEndY(), fx2.getEndY());
    }

    @Test
    void testMoveTo() {
        // setPosition() should move the entire line by translating both endpoints
        line.moveTo(2, 2); // from (0,0)-(3,4) to (2,2)-(5,6)
        Line fx = (Line) line.getFxShape();
        assertEquals(2.0, fx.getStartX());
        assertEquals(2.0, fx.getStartY());
        assertEquals(5.0, fx.getEndX());
        assertEquals(6.0, fx.getEndY());
    }
    
    @Test
    void testGetWidthPositiveDirection() {
        MyLine line = new MyLine(10, 20, 30, 20, 0);
        assertEquals(30.0, line.getWidth());
    }
    
    @Test
    void testGetHeightPositiveDirection() {
        MyLine line = new MyLine(10, 20, 10, 50, 0);
        assertEquals(50.0, line.getHeight());
    }

    @Test
    void testGetLengthDiagonalLine() {
        MyLine line = new MyLine(0, 0, 3, 4, 0); 
        assertEquals(5.0, line.getLength());
    }

    @Test
    void testToCSV() {
        // toCSV() should return a semicolon-separated string with coordinates and color
        line.getFxShape().setStroke(Color.BLUE);
        String csv = line.toCSV();
        assertTrue(csv.startsWith("LINE;0.0;0.0;3.0;4.0;"));
        assertTrue(csv.contains("0x0000ffff")); // Default JavaFX format for Color.BLUE
    }
    
}
