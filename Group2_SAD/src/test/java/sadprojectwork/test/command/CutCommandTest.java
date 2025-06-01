/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.CutCommand;
import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CutCommand class.
 * This command cuts a shape from the model and saves it to the clipboard.
 */
class CutCommandTest {

    private PaintModel model;
    private MyShape ellipse;
    private CutCommand command;

    /**
     * Initializes the PaintModel and a shape to cut.
     * Adds the shape to the model and prepares the CutCommand.
     */
    @BeforeEach
    void setUp() {
        model = new PaintModel();
        
        ellipse = new MyEllipse(10, 20, 30, 40, 0);
        ellipse.getFxShape().setFill(Color.BLUE);
        ellipse.getFxShape().setStroke(Color.GREEN);
        
        model.addShape(ellipse);

        command = new CutCommand(model, ellipse);
    }

    /**
     * Tests that execute removes the shape from the model
     * and places a clone of it in the clipboard.
     */
    @Test
    void testExecute() {
        command.execute();

        assertFalse(model.getShapes().contains(ellipse), "Shape should be removed from model!");

        MyShape clipboardShape = model.getClipboard();
        assertNotNull(clipboardShape, "Clipboard should contain a shape after cut!");
        assertNotSame(ellipse, clipboardShape, "Clipboard shape should be a different instance!");
        assertEquals(ellipse.getStartX(), clipboardShape.getStartX());
        assertEquals(ellipse.getStartY(), clipboardShape.getStartY());
        assertEquals(ellipse.getWidth(), clipboardShape.getWidth());
        assertEquals(ellipse.getHeight(), clipboardShape.getHeight());
        assertEquals(ellipse.getRotation(), clipboardShape.getRotation());
    }

    /**
     * Tests that undo restores the shape to the model
     * and clears the clipboard.
     */
    @Test
    void testUndo() {
        command.execute();
        command.undo();

        assertTrue(model.getShapes().contains(ellipse), "Shape should be restored to model after undo!");

        assertNull(model.getClipboard(), "Clipboard should be null after undo!");
    }
}
