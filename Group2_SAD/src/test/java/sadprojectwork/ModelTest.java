package sadprojectwork;

import decorator.BorderColorDecorator;
import decorator.FillColorDecorator;
import shapes.MyRectangle;
import shapes.MyLine;
import shapes.MyShape;
import command.AddShapeCommand;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class ModelTest {

    private Model model = new Model();
    private AddShapeCommand addCmd = new AddShapeCommand(model, new MyLine(10, 20, 30, 40));

    public ModelTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        model = new Model();
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of execute method, of class Model.
     */
    @Test
    public void testExecute() {
        model.execute(addCmd);
        assertEquals(1, model.getCommandHistory().size());
        assertEquals(0, model.getDeletedCommands().size());
    }

    /**
     * Test of undoLast method and redoLast method.
     */
    @Test
    public void testUndoAndRedo() {
        model.execute(addCmd);
        model.undoLast();
        assertEquals(0, model.getCommandHistory().size());
        assertEquals(1, model.getDeletedCommands().size());
        model.redoLast();
        assertEquals(1, model.getCommandHistory().size());
        assertEquals(0, model.getDeletedCommands().size());
    }

    /**
     * Test of undoLast method and redoLast method with empty stacks.
     */
    @Test
    public void testUndoAndRedoWithEmptyStacks() {
        model.undoLast();
        model.redoLast();
        assertTrue(model.getCommandHistory().isEmpty());
        assertTrue(model.getDeletedCommands().isEmpty());
    }

    /**
     * Test of save functionality.
     */
    @Test
    public void testSaveDrawing() throws IOException {
        // Create a decorated rectangle with fill and border color
        MyShape shape = new BorderColorDecorator(
                new FillColorDecorator(
                        new MyRectangle(10, 20, 100, 50), Color.RED),
                Color.BLUE);

        // Add the shape to the model
        model.addShape(shape);

        // Create a temporary file for saving the drawing
        File tempFile = File.createTempFile("testSave", ".csv");
        tempFile.deleteOnExit();

        // Save the shapes to the CSV file
        model.saveDrawing(tempFile);

        // Check that the file was written and is not empty
        assertTrue(tempFile.length() > 0, "The saved filed should contains datas.");
    }

    /**
     * Test of load functionality.
     */
    @Test
    public void testLoadDrawing() throws IOException {

        // Create a temporary CSV file with known shape data
        File tempFile = File.createTempFile("testLoad", ".csv");
        tempFile.deleteOnExit();

        // Write three shapes to the file: a rectangle, a line, and an ellipse
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.println("SHAPE;STARTX;STARTY;WIDTH;HEIGHT;FILL;BORDER");
            writer.println("RECTANGLE;10.0;20.0;100.0;50.0;0xff0000ff;0x0000ffff");
            writer.println("LINE;5.0;5.0;50.0;50.0;0x000000ff;0x00ff00ff");
            writer.println("ELLIPSE;15.0;25.0;60.0;30.0;0x00ffffff;0xff00ffff");
        }

        // Load shapes from the file
        List<MyShape> loaded = model.loadDrawing(tempFile);

        // Check that exactly three shapes were loaded
        assertEquals(3, loaded.size());

        // Check each shape type and its border color
        MyShape rect = loaded.get(0);
        assertTrue(rect instanceof BorderColorDecorator);
        assertEquals(Color.BLUE, ((BorderColorDecorator) rect).getBorderColor());

        MyShape line = loaded.get(1);
        assertTrue(line instanceof BorderColorDecorator);

        MyShape ellipse = loaded.get(2);
        assertTrue(ellipse instanceof BorderColorDecorator);
        assertEquals(Color.MAGENTA, ((BorderColorDecorator) ellipse).getBorderColor());
    }
}
