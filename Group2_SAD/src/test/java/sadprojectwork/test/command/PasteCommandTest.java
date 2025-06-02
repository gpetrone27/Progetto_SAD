
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.PasteCommand;
import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PasteCommand class.
 * This command paste a shape in the model.
 */
public class PasteCommandTest {

    private PaintModel model;
    private MyShape rectangle;
    private PasteCommand pasteCommand;

    /**
     * Initializes the PaintModel and a shape to paste.
     * Adds the shape to the clipboard and prepares the PasteCommand.
     */
    @BeforeEach
    public void setUp() {
        model = new PaintModel();
        
        rectangle = new MyRectangle(10, 10, 20, 20, 0, false, false);
        rectangle.getFxShape().setFill(Color.TURQUOISE);
        
        model.setClipboard(rectangle);
        
        pasteCommand = new PasteCommand(model, 100, 100);
    }

    /**
     * Tests that execute pastes the shape to the model.
     */
    @Test
    public void testExecute() {
        pasteCommand.execute();

        MyShape pasted = pasteCommand.getPastedShape();
        assertTrue(model.getShapes().contains(pasted), "Model should contain the pasted shape after execute!");
        assertEquals(100, pasted.getStartX(), 0.01, "Pasted shape X position incorrect!");
        assertEquals(100, pasted.getStartY(), 0.01, "Pasted shape Y position incorrect!");
    }

    /**
     * Tests that undo deletes the shape from the model.
     */
    @Test
    public void testUndo() {
        pasteCommand.execute();
        MyShape pasted = pasteCommand.getPastedShape();
        pasteCommand.undo();
        
        assertFalse(model.getShapes().contains(pasted), "Shape should be removed from model after undo!");
    }

    /**
     * Tests that no shape is pasted if the clipboard is empty.
     */
    @Test
    public void testEmptyClipboard() {
        model.setClipboard(null);
        PasteCommand pasteCommand = new PasteCommand(model, 30, 30);
        
        assertNull(pasteCommand.getPastedShape(), "No shape should be paste when clipboard is null!");
    }
}
