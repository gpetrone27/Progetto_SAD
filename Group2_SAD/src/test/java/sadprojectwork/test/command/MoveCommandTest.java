
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.MoveCommand;
import sadprojectwork.shapes.MyEllipse;
import sadprojectwork.shapes.MyShape;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MoveCommand class.
 * This command moves a shape to a new position and can undo the movement.
 */
class MoveCommandTest {

    private MyShape ellipse;
    private MoveCommand moveCommand;

    /**
     * Creates an ellipse and prepares a MoveCommand.
     */
    @BeforeEach
    void setUp() {
        ellipse = new MyEllipse(10, 20, 30, 40, 0);
        ellipse.getFxShape().setFill(Color.GREEN);
        ellipse.getFxShape().setStroke(Color.DARKGRAY);
        
        moveCommand = new MoveCommand(ellipse, 10, 20, 50, 70);
    }

    /**
     * Tests that execute moves the shape to the new coordinates.
     */
    @Test
    void testExecute() {
        moveCommand.execute();
        
        assertEquals(50.0, ellipse.getStartX(), "X coordinate should be updated after execute!");
        assertEquals(70.0, ellipse.getStartY(), "Y coordinate should be updated after execute!");
    }

    /**
     * Tests that undo restores the shape to its original coordinates.
     */
    @Test
    void testUndo() {
        moveCommand.execute();
        moveCommand.undo();
        
        assertEquals(10.0, ellipse.getStartX(), "X coordinate should be restored after undo!");
        assertEquals(20.0, ellipse.getStartY(), "Y coordinate should be restored after undo!");
    }
}