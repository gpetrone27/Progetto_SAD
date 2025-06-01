/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sadprojectwork.test.command;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.command.BringToBackCommand;
import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.MyShape;

import static org.junit.jupiter.api.Assertions.*;
import sadprojectwork.shapes.*;

/**
 * Unit tests for the BringToBackCommand class.
 * This command moves a shape to the back of the drawing order in the model.
 */
class BringToBackCommandTest {

    private PaintModel model;
    private BringToBackCommand command;
    private MyShape rectangle, ellipse, secondEllipse;

    /**
     * Initializes a PaintModel and three shapes.
     * Adds the shapes to the model in a specific order.
     * Prepares a BringToBackCommand for the second shape.
     */
    @BeforeEach
    void setUp() {
        model = new PaintModel();
        
        rectangle = new MyRectangle(0, 0, 10, 10, 0);
        rectangle.getFxShape().setFill(Color.GREEN);
        rectangle.getFxShape().setStroke(Color.YELLOW);
        
        ellipse = new MyEllipse(20, 20, 10, 10, 0);
        ellipse.getFxShape().setFill(Color.PINK);
        ellipse.getFxShape().setStroke(Color.PURPLE);
        
        secondEllipse = new MyEllipse(40, 40, 10, 10, 0);
        secondEllipse.getFxShape().setFill(Color.GREEN);
        secondEllipse.getFxShape().setStroke(Color.LIGHTBLUE);

        model.addShape(rectangle);
        model.addShape(ellipse);
        model.addShape(secondEllipse);
        command = new BringToBackCommand(model, ellipse);
    }

    /**
     * Tests that the execute method moves the shape to the back (index 0).
     * Verifies that the order of the remaining shapes is preserved.
     */
    @Test
    void testExecute() {
        command.execute();
        assertEquals(ellipse, model.getShapes().get(0));
        assertEquals(rectangle, model.getShapes().get(1));
        assertEquals(secondEllipse, model.getShapes().get(2));
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
        assertEquals(secondEllipse, model.getShapes().get(2));
    }
}
