
package sadprojectwork.test.command;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.MirrorCommand;
import sadprojectwork.shapes.MyPolygon;

import java.util.List;
import javafx.geometry.Point2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MirrorCommand class.
 * This command mirrors a shape horizontally or vertically.
 */
class MirrorCommandTest {

    private MyPolygon triangle;
    private MirrorCommand horizontalMirrorCommand;
    private MirrorCommand verticalMirrorCommand;

    /**
     * Initializes a polygon.
     * Creates one command for horizontal mirroring and one for vertical.
     */
    @BeforeEach
    void setUp() {
        List<Point2D> points = Arrays.asList(
        new Point2D(10, 10),
        new Point2D(20, 20),
        new Point2D(30, 10)
        );
        
        triangle = new MyPolygon(0, 0, points, 0, false, false);

        horizontalMirrorCommand = new MirrorCommand(triangle, true);
        verticalMirrorCommand = new MirrorCommand(triangle, false);
    }

    /**
     * Tests horizontal mirroring. 
     * Applying it twice should restore the original points.
     */
    @Test
    void testHorizontalMirrorExecuteAndUndo() {
        double originalScaleX = triangle.getFxShape().getScaleX();

        horizontalMirrorCommand.execute();
        double newScaleX = triangle.getFxShape().getScaleX();
        assertNotEquals(originalScaleX, newScaleX, "Scale should change after first mirror!");

        horizontalMirrorCommand.undo();
        double restored = triangle.getFxShape().getScaleX();
        assertEquals(originalScaleX, restored, "Scale should return to original after undo (second mirror)!");
    }

    /**
     * Tests vertical mirroring. Applying it twice should restore the original points.
     */
    @Test
    void testVerticalMirrorExecuteAndUndo() {
        double originalScaleY = triangle.getFxShape().getScaleY();

        verticalMirrorCommand.execute();
        double newScaleY = triangle.getFxShape().getScaleY();
        assertNotEquals(originalScaleY, newScaleY, "Scale should change after first mirror!");

        verticalMirrorCommand.undo();
        double restored = triangle.getFxShape().getScaleY();
        assertEquals(originalScaleY, restored, "Scale should return to original after undo (second mirror)!");
    }
}