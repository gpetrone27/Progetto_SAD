
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.BringToFrontCommand;
import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the BringToFrontCommand class.
 * This command moves a shape to the front of the drawing order in the model.
 */
class BringToFrontCommandTest {

    private PaintModel model;
    private BringToFrontCommand command;
    private MyShape rectangle, ellipse, secondRect;

    /**
     * Initializes a PaintModel and three shapes.
     * Adds the shapes to the model in a specific order.
     * Prepares a BringToFrontCommand for the second shape.
     */
    @BeforeEach
    void setUp() {
        model = new PaintModel();
        
        rectangle = new MyRectangle(0, 0, 10, 10, 0);
        rectangle.getFxShape().setFill(Color.GREEN);
        rectangle.getFxShape().setStroke(Color.YELLOW);
        
        ellipse = new MyEllipse(20, 20, 10, 10, 0);
        ellipse.getFxShape().setFill(Color.BEIGE);
        ellipse.getFxShape().setStroke(Color.BROWN);
        
        secondRect = new MyRectangle(40, 40, 10, 10, 0);
        secondRect.getFxShape().setFill(Color.RED);
        secondRect.getFxShape().setStroke(Color.GREY);

        model.addShape(rectangle);
        model.addShape(ellipse); 
        model.addShape(secondRect);

        command = new BringToFrontCommand(model, ellipse);
    }

    /**
     * Tests that the execute method moves the shape to the front (last index).
     * Verifies that the order of the remaining shapes is preserved.
     */
    @Test
    void testExecute() {
        command.execute();

        assertEquals(rectangle, model.getShapes().get(0));
        assertEquals(secondRect, model.getShapes().get(1));
        assertEquals(ellipse, model.getShapes().get(2));
    }

    /**
     * Tests that the undo method restores the original order of the shapes.
     */
    @Test
    void testUndo() {
        command.execute();
        command.undo();

        assertEquals(rectangle, model.getShapes().get(0));
        assertEquals(ellipse, model.getShapes().get(1));
        assertEquals(secondRect, model.getShapes().get(2));
    }
}
