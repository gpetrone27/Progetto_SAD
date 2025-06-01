package sadprojectwork.test.command;

import sadprojectwork.command.RotationCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.shapes.MyRectangle;

import static org.junit.jupiter.api.Assertions.*;

class RotationCommandTest {

    private MyRectangle shape;

    @BeforeEach
    void setUp() {
        // Inizializza un rettangolo qualsiasi
        shape = new MyRectangle(100, 100, 50, 50, 0);
    }

    @Test
    void testExecuteRotation() {
        RotationCommand cmd = new RotationCommand(shape, 0, 45);
        cmd.execute();
        assertEquals(45.0, shape.getRotation());
    }

    @Test
    void testUndoRotation() {
        shape.setRotation(30);
        RotationCommand cmd = new RotationCommand(shape, 30, 90);
        cmd.execute();
        cmd.undo();
        assertEquals(30.0, shape.getRotation());
    }

    @Test
    void testExecuteNegativeRotation() {
        RotationCommand cmd = new RotationCommand(shape, 0, -90);
        cmd.execute();
        assertEquals(-90.0, shape.getRotation());
    }

    @Test
    void testExecute() {
        RotationCommand cmd = new RotationCommand(shape, 0, 180);
        cmd.execute();
        assertEquals(180.0, shape.getRotation());

        cmd.undo();
        assertEquals(0.0, shape.getRotation());

        cmd.execute();
        assertEquals(180.0, shape.getRotation());

        cmd.undo();
        assertEquals(0.0, shape.getRotation());
    }

    @Test
    void testZeroRotation() {
        shape.setRotation(90);
        RotationCommand cmd = new RotationCommand(shape, 90, 0);
        cmd.execute();
        assertEquals(0.0, shape.getRotation());
        cmd.undo();
        assertEquals(90.0, shape.getRotation());
    }
}
