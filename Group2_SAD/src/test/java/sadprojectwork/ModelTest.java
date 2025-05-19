
package sadprojectwork;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    public void testSaveDrawing() {
        // TO DO
    }
    
    /**
     * Test of load functionality.
     */
    @Test
    public void testLoadDrawing() {
        // TO DO
    }
    
}
