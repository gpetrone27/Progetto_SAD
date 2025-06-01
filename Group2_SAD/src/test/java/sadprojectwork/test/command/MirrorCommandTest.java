/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sadprojectwork.test.command;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.MirrorCommand;
import sadprojectwork.shapes.MyPolygon;
import sadprojectwork.shapes.*;

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
        
        triangle = new MyPolygon(0, 0, points, 0);

        horizontalMirrorCommand = new MirrorCommand(triangle, true);
        verticalMirrorCommand = new MirrorCommand(triangle, false);
    }

    /**
     * Tests horizontal mirroring. 
     * Applying it twice should restore the original points.
     */
    @Test
    void testHorizontalMirrorExecuteAndUndo() {
        List<Point2D> originalPoints = List.copyOf(triangle.convertToPoint2D());

        horizontalMirrorCommand.execute();
        List<Point2D> mirroredOnce = List.copyOf(triangle.convertToPoint2D());
        assertNotEquals(originalPoints, mirroredOnce, "Points should change after first mirroring!");

        horizontalMirrorCommand.undo();
        List<Point2D> restored = triangle.convertToPoint2D();
        assertEquals(originalPoints, restored, "Points should return to original after undo (second mirror)!");
    }

    /**
     * Tests vertical mirroring. Applying it twice should restore the original points.
     */
    @Test
    void testVerticalMirrorExecuteAndUndo() {
        List<Point2D> originalPoints = List.copyOf(triangle.convertToPoint2D());

        verticalMirrorCommand.execute();
        List<Point2D> mirroredOnce = List.copyOf(triangle.convertToPoint2D());
        assertNotEquals(originalPoints, mirroredOnce, "Points should change after first mirroring!");

        verticalMirrorCommand.undo();
        List<Point2D> restored = triangle.convertToPoint2D();
        assertEquals(originalPoints, restored, "Points should return to original after undo (second mirror)!");
    }
}