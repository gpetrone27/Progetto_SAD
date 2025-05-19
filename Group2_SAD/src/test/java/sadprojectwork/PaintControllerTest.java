/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;


/**
 * Test class for the method in the controller, that uses 
 * TestFX to test user interaction with the canvas.
 * 
 * N.B. Requires that the FXML file “arsnova.fxml” be accessible from 
 * the path `/ui/arsnova.fxml`.
 *
 * @author noemi
 */
@ExtendWith(ApplicationExtension.class)
public class PaintControllerTest {
    private PaintController controller;
    
    public PaintControllerTest() {
    }
    
    /**
     * Initializes the JavaFX application by loading the interface from FXML.
     * Is automatically called by TestFX before each test.
     *
     * @param stage: the main stage on which to load the interface.
     * @throws Exception if the FXML file is not found or cannot be loaded.
     */
   @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/arsnova.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    /**
     * Checks that a rectangle added to the canvas can be selected.
     * Checks that the selection is active and that the graphic effect is applied.
     *
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    void testRectangleSelected(FxRobot robot) {
        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(50, 50, 80, 60);
            rectangle.getFxShape().setFill(Color.GREEN);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});

        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();

        MyShape selected = controller.getSelectedShape();
        assertNotNull(selected, "The rectangle must be selected!");
        assertTrue(selected.getFxShape().getEffect() instanceof DropShadow, "If the rectangle is selected, there must be a selection effect!");
    }
    
    /**
     * Checks that an ellipse added to the canvas can be selected.
     * Checks that the selection is active and that the graphic effect is applied.
     *
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    void testEllipseSelected(FxRobot robot) {
        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(50, 60, 35, 20);
            ellipse.getFxShape().setFill(Color.BLUE);
            ellipse.getFxShape().setStroke(Color.PURPLE);
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});

        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();

        MyShape selected = controller.getSelectedShape();
        assertNotNull(selected, "The ellipse must be selected!");
        assertTrue(selected.getFxShape().getEffect() instanceof DropShadow, "If the ellipse is selected, there must be a selection effect!");
    }
    
    /**
     * Checks that a line added to the canvas can be selected.
     * Checks that the selection is active and that the graphic effect is applied.
     *
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    void testLineSelected(FxRobot robot) {
        Platform.runLater(() -> {
            MyShape line = new MyLine(0, 0, 30, 20);
            line.getFxShape().setStroke(Color.BLACK);
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.interact(() -> {});

        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();

        MyShape selected = controller.getSelectedShape();
        assertNotNull(selected, "The line must be selected!");
        assertTrue(selected.getFxShape().getEffect() instanceof DropShadow, "If the line is selected, there must be a selection effect!");
    }
    
    /**
     * Verifies that after a click outside the selected form,
     * the selection is removed.
     *
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    void testDisableSelection(FxRobot robot){
       Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(100, 100, 40, 30);
            rectangle.getFxShape().setFill(Color.BLUE);
            rectangle.getFxShape().setStroke(Color.PURPLE);
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();

        robot.clickOn(300, 300, MouseButton.PRIMARY); 
        assertNull(controller.getSelectedShape(), "After the click, the shape should no longer be selected!");
    }
    
    /**
     * Verifies that in the presence of a shape already selected, clicking on another must deselect the first one.
     *
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    public void testNewSelectionWithShapeSelected(FxRobot robot){
        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(40, 40, 30, 30);
            rectangle.getFxShape().setFill(Color.GREEN);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            MyShape ellipse = new MyEllipse(100, 100, 40, 40);
            ellipse.getFxShape().setFill(Color.BLUE);
            ellipse.getFxShape().setStroke(Color.PURPLE);
            controller.getModel().addShape(rectangle);
            controller.getModel().addShape(ellipse);
            controller.enableSelection(rectangle);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();
        MyShape rect = controller.getSelectedShape();

        robot.moveTo(controller.getCanvas().getChildren().get(1)).clickOn();
        MyShape ell = controller.getSelectedShape();

        assertNotEquals(rect, ell, "The second shape must be selected and highlighted!");
        assertNull(rect.getFxShape().getEffect(), "The first shape should no longer be selected and highlighted!");
    }
    
    /**
    * Verifies that a selected rectangle is correctly deleted
    * from the model and canvas, and that the selection is reset to zero.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    *
    */
    @Test
    void testDeleteRectangle(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(70, 70, 50, 40);
            rectangle.getFxShape().setFill(Color.GREEN);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.deleteShape(new ActionEvent()));
        controller.clearSelection();

        assertFalse(controller.getModel().getShapes().contains(shapeRef[0]), "The shape must be removed from the model!");
        assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "The shape must be removed from the canvas!");
        assertNull(controller.getSelectedShape(), "After deletion, the selection must not be present!");
    }
    
    /**
    * Verifies that a selected ellipse is correctly deleted
    * from the model and canvas, and that the selection is reset to zero.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    *
    */
    @Test
    void testDeleteEllipse(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(150, 200, 50, 30);
            ellipse.getFxShape().setFill(Color.GREEN);
            ellipse.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.deleteShape(new ActionEvent()));
        controller.clearSelection();

        assertFalse(controller.getModel().getShapes().contains(shapeRef[0]), "The shape must be removed from the model!");
        assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "The shape must be removed from the canvas!");
        assertNull(controller.getSelectedShape(), "After deletion, the selection must not be present!");
    }
    
    /**
    * Verifies that a selected line is correctly deleted
    * from the model and canvas, and that the selection is reset to zero.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    *
    */
    @Test
    void testDeleteLine(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape line = new MyLine(0, 0, 24, 50);
            line.getFxShape().setStroke(Color.PURPLE);
            shapeRef[0] = line;
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.deleteShape(new ActionEvent()));
        controller.clearSelection();

        assertFalse(controller.getModel().getShapes().contains(shapeRef[0]), "The shape must be removed from the model!");
        assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "The shape must be removed from the canvas!");
        assertNull(controller.getSelectedShape(), "After deletion, the selection must not be present!");
    }
    
    /**
    * Verifies that, after deleting a shape, the undo operation
    * correctly restores the shape to the model.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testUndoAfterDelete(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape line = new MyLine(0, 0, 70, 100);
            line.getFxShape().setStroke(Color.ORANGE);
            shapeRef[0] = line;
            controller.getModel().addShape(line);
            controller.getCanvas().getChildren().add(line.getFxShape());
            controller.enableSelection(line);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();
        robot.interact(() -> controller.deleteShape(new ActionEvent()));

        assertFalse(controller.getModel().getShapes().contains(shapeRef[0]));

        robot.interact(() -> controller.undoOperation(new ActionEvent()));

        assertTrue(controller.getModel().getShapes().contains(shapeRef[0]), "The shape must be restored!");
    }
    
    /**
    * Check for errors when attempting to perform
    * deletion without any shape selected or drawn.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testDeleteWithNoDrawing(FxRobot robot) {
        Platform.runLater(() -> {
            controller.deleteShape(null);
        });

        robot.interact(() -> {
            assertNull(controller.getSelectedShape(), "No shape should be selected.");
            assertEquals(0, controller.getModel().getShapes().size(), "No shapes must be present in the model.");
        });
    }
    
    /**
    * Tests that a selected rectangle updates its position, when it's moved.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testMoveRectangle(FxRobot robot){
        MyShape[] shapeRef = new MyShape[1];
        double oldX = 50, oldY = 60;
        double newX = 120, newY = 130;

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(oldX, oldY, 60, 40);
            rectangle.getFxShape().setFill(Color.BLUE);
            rectangle.getFxShape().setStroke(Color.ORANGE);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Platform.runLater(() -> shapeRef[0].moveTo(newX, newY));
        robot.interact(() -> {});

        assertEquals(newX, shapeRef[0].getStartX(), "Shape X should be updated.");
        assertEquals(newY, shapeRef[0].getStartY(), "Shape Y should be updated.");
    }
    
    /**
    * Tests that a selected ellipse updates its position, when it's moved.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testMoveEllipse(FxRobot robot){
        MyShape[] shapeRef = new MyShape[1];
        double oldX = 50, oldY = 60;
        double newX = 120, newY = 130;

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(oldX, oldY, 60, 40);
            ellipse.getFxShape().setFill(Color.ORANGE);
            ellipse.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Platform.runLater(() -> shapeRef[0].moveTo(newX, newY));
        robot.interact(() -> {});

        assertEquals(newX, shapeRef[0].getStartX(), "Shape X should be updated.");
        assertEquals(newY, shapeRef[0].getStartY(), "Shape Y should be updated.");
    }
    
    /**
    * Tests that a selected line updates its position, when it's moved.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testMoveLine(FxRobot robot){
        MyShape[] shapeRef = new MyShape[1];
        double oldX = 50, oldY = 60;
        double newX = 120, newY = 130;

        Platform.runLater(() -> {
            MyShape line = new MyLine(oldX, oldY, 60, 40);
            line.getFxShape().setStroke(Color.PINK);
            shapeRef[0] = line;
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Platform.runLater(() -> shapeRef[0].moveTo(newX, newY));
        robot.interact(() -> {});

        assertEquals(newX, shapeRef[0].getStartX(), "Shape X should be updated.");
        assertEquals(newY, shapeRef[0].getStartY(), "Shape Y should be updated.");
    }
   
    /**
    * Tests that moving a shape updates its position and can be undone correctly.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testUndoAfterMove(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];
       double oldX = 30, oldY = 70;
       double newX = 180, newY = 220;

       Platform.runLater(() -> {
           MyShape rectangle = new MyRectangle(oldX, oldY, 40, 30);
           rectangle.getFxShape().setFill(Color.PINK);
           rectangle.getFxShape().setStroke(Color.BLACK);
           shapeRef[0] = rectangle;
           controller.getModel().addShape(rectangle);
           controller.enableSelection(rectangle);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();

       Platform.runLater(() -> {
           Command moveCmd = new MoveCommand(shapeRef[0], oldX, oldY, newX, newY);
           controller.getModel().execute(moveCmd);
       });
       robot.interact(() -> {});

       assertEquals(newX, shapeRef[0].getStartX(), "X should be updated after move.");
       assertEquals(newY, shapeRef[0].getStartY(), "Y should be updated after move.");

       robot.interact(() -> controller.undoOperation(new ActionEvent()));

       assertEquals(oldX, shapeRef[0].getStartX(), "X should return to original after undo.");
       assertEquals(oldY, shapeRef[0].getStartY(), "Y should return to original after undo.");
   }

}
