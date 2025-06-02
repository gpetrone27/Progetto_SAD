
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.DeleteCommand;
import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DeleteCommand class.
 * This command deletes a shape from the model.
 */
class DeleteCommandTest {

    private PaintModel model;
    private MyShape ellipse;
    private DeleteCommand command;

    /**
     * Initializes the PaintModel and a shape to delete.
     * Adds the shape to the model and prepares the DeleteCommand.
     */
    @BeforeEach
    void setUp() {
        model = new PaintModel();
        
        ellipse = new MyEllipse(10, 20, 30, 40, 0);
        ellipse.getFxShape().setFill(Color.BLACK);
        ellipse.getFxShape().setStroke(Color.RED);
        model.addShape(ellipse);

        command = new DeleteCommand(model, ellipse);
    }

    /**
     * Tests that execute removes the shape from the model.
     */
    @Test
    void testExecute() {
        command.execute();

        assertFalse(model.getShapes().contains(ellipse), "Shape should be removed from model!");
    }

    /**
     * Tests that undo re-adds the shape to the model.
     */
    @Test
    void testUndo() {
        command.execute();
        command.undo();

        assertTrue(model.getShapes().contains(ellipse), "Shape should be restored to model after undo!");
    }
}

