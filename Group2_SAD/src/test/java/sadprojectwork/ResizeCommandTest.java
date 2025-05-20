package sadprojectwork;

import command.ResizeCommand;
import shapes.MyRectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResizeCommandTest {

    private MyRectangle rectangle;
    private ResizeCommand command;

    @BeforeEach
    void setUp() {
        // Create a rectangle at (0,0) with width 10 and height 20
        rectangle = new MyRectangle(0, 0, 10, 20);
        
        // Create a ResizeCommand to change it to width 30 and height 40
        command = new ResizeCommand(rectangle, 30, 40);
    }

    @Test
    void testExecute() {
        // Execute the command
        command.execute();

        // Check that the rectangle has new dimensions
        assertEquals(30.0, rectangle.getFirstDim());
        assertEquals(40.0, rectangle.getSecondDim());
    }

    @Test
    void testUndo() {
        // Execute and then undo
        command.execute();
        command.undo();

        // Check that the rectangle's original dimensions are restored
        assertEquals(10.0, rectangle.getFirstDim());
        assertEquals(20.0, rectangle.getSecondDim());
    }

}
