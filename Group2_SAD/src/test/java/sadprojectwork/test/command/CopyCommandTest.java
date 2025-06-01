/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.CopyCommand;
import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CopyCommand class.
 * This command copies a shape to the clipboard.
 */
class CopyCommandTest {

    private PaintModel model;
    private MyShape line;
    private CopyCommand command;

    /**
     * Initializes the PaintModel and a shape to copy.
     * Prepares the CopyCommand with the given shape and model.
     */
    @BeforeEach
    void setUp() {
        model = new PaintModel();
        
        line = new MyLine(10, 20, 30, 40, 0);
        line.getFxShape().setStroke(Color.VIOLET);

        command = new CopyCommand(model, line);
    }

    /**
     * Tests that execute stores a clone of the shape in the clipboard.
     */
    @Test
    void testExecute() {
        command.execute();

        MyShape clipboardShape = model.getClipboard();

        assertNotNull(clipboardShape, "Clipboard should not be null after copy!");
        assertNotSame(line, clipboardShape, "Clipboard shape should be a different instance!");
        assertEquals(line.getScaleX(), clipboardShape.getScaleX());
        assertEquals(line.getScaleY(), clipboardShape.getScaleY());
        assertEquals(line.getWidth(), clipboardShape.getWidth());
        assertEquals(line.getHeight(), clipboardShape.getHeight());
        assertEquals(line.getRotation(), clipboardShape.getRotation());
    }

    /**
     * Tests that undo clears the clipboard after a copy.
     */
    @Test
    void testUndo() {
        command.execute();
        command.undo();

        assertNull(model.getClipboard(), "Clipboard should be null after undo");
    }
}
