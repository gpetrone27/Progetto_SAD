
package sadprojectwork;

import command.ChangeColorCommand;
import command.MoveCommand;
import command.Command;
import decorator.BorderColorDecorator;
import decorator.FillColorDecorator;
import shapes.MyRectangle;
import shapes.MyEllipse;
import shapes.MyLine;
import shapes.MyShape;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
*/
@ExtendWith(ApplicationExtension.class)
public class PaintControllerTest {
    
    private PaintController controller;
    
    public PaintControllerTest() { }
    
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
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
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
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
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
            MyShape line = new MyLine(385, 290, 30, 20);
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
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
            rectangle.getFxShape().setFill(Color.BLUE);
            rectangle.getFxShape().setStroke(Color.PURPLE);
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();

        robot.clickOn(350, 350, MouseButton.PRIMARY); 
        assertNull(controller.getSelectedShape(), "After the click, the shape should no longer be selected!");
    }
    
    /**
     * Verifies that in the presence of a shape already selected, clicking on another must deselect the first one.
     *
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    void testNewSelectionWithShapeSelected(FxRobot robot){
        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
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
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
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
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
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
            MyShape line = new MyLine(385, 290, 30, 20);
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
            MyShape line = new MyLine(385, 290, 30, 20);
            line.getFxShape().setStroke(Color.ORANGE);
            shapeRef[0] = line;
            controller.getModel().addShape(line);
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

        assertEquals(newX, shapeRef[0].getStartX(), "Shape X should be updated!");
        assertEquals(newY, shapeRef[0].getStartY(), "Shape Y should be updated!");
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

        assertEquals(newX, shapeRef[0].getStartX(), "Shape X should be updated!");
        assertEquals(newY, shapeRef[0].getStartY(), "Shape Y should be updated!");
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

        assertEquals(newX, shapeRef[0].getStartX(), "Shape X should be updated!");
        assertEquals(newY, shapeRef[0].getStartY(), "Shape Y should be updated!");
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

       assertEquals(newX, shapeRef[0].getStartX(), "X should be updated after move!");
       assertEquals(newY, shapeRef[0].getStartY(), "Y should be updated after move!");

       robot.interact(() -> controller.undoOperation(new ActionEvent()));

       assertEquals(oldX, shapeRef[0].getStartX(), "X should return to original after undo!");
       assertEquals(oldY, shapeRef[0].getStartY(), "Y should return to original after undo!");
   }
   
   /**
    * Tests the border color change of a selected rectangle.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeBorderColorRectangle(FxRobot robot) {
         MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
            rectangle.getFxShape().setFill(Color.RED);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newBorder = Color.BLUE;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], null, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Border color should be updated!");
    }
   
   /**
    * Tests the fill color change of a selected rectangle.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeFillColorRectangle(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
            rectangle.getFxShape().setFill(Color.RED);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newFill = Color.PINK;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, null);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Fill color should be updated!");
    }
   
   /**
    * Tests the color change of a selected rectangle.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeColorRectangle(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
            rectangle.getFxShape().setFill(Color.RED);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newFill = Color.PINK;
        Color newBorder = Color.BLUE;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Fill color should be updated!");
        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Border color should be updated!");
    }
   
   /**
    * Tests the border color change of a selected ellipse.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeBorderColorEllipse(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(Color.BLUE);
            ellipse.getFxShape().setStroke(Color.GREEN);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newBorder = Color.ORANGE;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], null, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Border color should be updated!");
    }
   
   /**
    * Tests the fill color change of a selected ellipse.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeFillColorEllipse(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(Color.BLUE);
            ellipse.getFxShape().setStroke(Color.GREEN);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newFill = Color.PINK;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, null);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Fill color should be updated!");
    }

   /**
    * Tests the color change of a selected ellipse.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeColorEllipse(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(Color.BLUE);
            ellipse.getFxShape().setStroke(Color.GREEN);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newFill = Color.PINK;
        Color newBorder = Color.BLUE;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Fill color should be updated!");
        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Border color should be updated!");
    }
   
   /**
    * Tests the border color change of a selected line.
    *
    *
    * @param TestFX robot: robot to simulate user interaction
    */
   @Test
   void testChangeColorLine(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape line = new MyLine(385, 290, 30, 20);
            line.getFxShape().setStroke(Color.PURPLE);
            shapeRef[0] = line;
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newBorder = Color.BROWN;
        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], null, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Border color should be updated!");
    }
   
   /**
    * Tests that a change fill color shape can be undone correctly.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testUndoFillChangeColor(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];
        Color originalFill = Color.RED;
        Color newFill = Color.GREEN;

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(originalFill);
            ellipse.getFxShape().setStroke(Color.BLACK);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, null);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Fill color should be updated!");

        robot.interact(() -> controller.undoOperation(new ActionEvent()));
        assertEquals(originalFill, shapeRef[0].getFxShape().getFill(), "Undo should restore original fill color.");
    }
    
    /**
    * Tests that a change border color shape can be undone correctly.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testUndoBorderChangeColor(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];
        Color originalBorder = Color.PURPLE;
        Color newBorder = Color.GREEN;

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
            rectangle.getFxShape().setFill(Color.RED);
            rectangle.getFxShape().setStroke(originalBorder);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], null, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Border color should be updated!");

        robot.interact(() -> controller.undoOperation(new ActionEvent()));
        assertEquals(originalBorder, shapeRef[0].getFxShape().getStroke(), "Undo should restore original border color!");
    }
    
    /**
    * Tests that a change color shape can be undone correctly.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testUndoChangeColor(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];
        Color originalFill = Color.RED;
        Color newFill = Color.GREEN;
        Color originalBorder = Color.YELLOW;
        Color newBorder = Color.BROWN;

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(originalFill);
            ellipse.getFxShape().setStroke(originalBorder);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> {
           Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, newBorder);
            controller.getModel().execute(changeColor);
            controller.undoOperation(new ActionEvent());
        });

        assertEquals(originalFill, shapeRef[0].getFxShape().getFill(), "Undo should restore original fill color!");
        assertEquals(originalBorder, shapeRef[0].getFxShape().getStroke(), "Undo should restore original border color!");
    }
    
    /**
    * Tests that a selected rectangle is cut: removed from model and canvas and copied to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testCutRectangle(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape rectangle = new MyRectangle(385, 290, 30, 20);
           rectangle.getFxShape().setFill(Color.BLUE);
           rectangle.getFxShape().setStroke(Color.YELLOW);
           shapeRef[0] = rectangle;
           controller.getModel().addShape(rectangle);
           controller.enableSelection(rectangle);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();
       robot.interact(() -> controller.cutShape(new ActionEvent()));

       assertFalse(controller.getModel().getShapes().contains(shapeRef[0]), "Rectangle must be removed from the model!");
       assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "Rectangle must be removed from the canvas!");
       assertNotNull(controller.getModel().getClipboard(), "Rectangle must be saved to clipboard!");
   }
   
   /**
    * Tests that a selected ellipse is cut: removed from model and canvas and copied to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testCutEllipse(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape ellipse = new MyEllipse(385, 290, 30, 20);
           ellipse.getFxShape().setFill(Color.BLUE);
           ellipse.getFxShape().setStroke(Color.YELLOW);
           shapeRef[0] = ellipse;
           controller.getModel().addShape(ellipse);
           controller.enableSelection(ellipse);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();
       robot.interact(() -> controller.cutShape(new ActionEvent()));

       assertFalse(controller.getModel().getShapes().contains(shapeRef[0]), "Ellipse must be removed from the model!");
       assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "Ellipse must be removed from the canvas!");
       assertNotNull(controller.getModel().getClipboard(), "Ellipse must be saved to clipboard!");
   }
   
   /**
    * Tests that a selected line is cut: removed from model and canvas and copied to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testCutLine(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape line = new MyLine(385, 290, 30, 20);
           line.getFxShape().setStroke(Color.ORANGE);
           shapeRef[0] = line;
           controller.getModel().addShape(line);
           controller.enableSelection(line);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();
       robot.interact(() -> controller.cutShape(new ActionEvent()));

       assertFalse(controller.getModel().getShapes().contains(shapeRef[0]), "Line must be removed from the model!");
       assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "Line must be removed from the canvas!");
       assertNotNull(controller.getModel().getClipboard(), "Line must be saved to clipboard!");
   }
   
   /**
    * Tests that a cut operation can be undone, restoring the shape to the model and to the canvas.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testUndoAfterCut(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(Color.PINK);
            ellipse.getFxShape().setStroke(Color.PURPLE);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();
        robot.interact(() -> controller.cutShape(new ActionEvent()));

        assertFalse(controller.getModel().getShapes().contains(shapeRef[0]));
        assertFalse(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()));

        robot.interact(() -> controller.undoOperation(new ActionEvent()));

        assertTrue(controller.getModel().getShapes().contains(shapeRef[0]), "Shape must be restored to the model!");
        assertTrue(controller.getCanvas().getChildren().contains(shapeRef[0].getFxShape()), "Shape must be restored to the canvas!");
    }
   
   /**
    * Tests that a selected rectangle is copy to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testCopyRectangle(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape rectangle = new MyRectangle(385, 290, 30, 20);
            rectangle.getFxShape().setFill(Color.BLUE);
            rectangle.getFxShape().setStroke(Color.BROWN);
            shapeRef[0] = rectangle;
            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.copyShape(new ActionEvent()));

        assertNotNull(controller.getModel().getClipboard(), "Clipboard should contain the copied rectangle!");
        assertNotSame(shapeRef[0], controller.getModel().getClipboard(), "The copied rectangle must be a new object!");
    }
    
    /**
    * Tests that a selected ellipse is copy to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testCopyEllipse(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(Color.BLUE);
            ellipse.getFxShape().setStroke(Color.BROWN);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.copyShape(new ActionEvent()));

        assertNotNull(controller.getModel().getClipboard(), "Clipboard should contain the copied ellipse!");
        assertNotSame(shapeRef[0], controller.getModel().getClipboard(), "The copied ellipse must be a new object!");
    }
    
    /**
    * Tests that a selected line is copy to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testCopyLine(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape line = new MyLine(385, 290, 30, 20);
            line.getFxShape().setStroke(Color.YELLOW);
            shapeRef[0] = line;
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.copyShape(new ActionEvent()));

        assertNotNull(controller.getModel().getClipboard(), "Clipboard should contain the copied line!");
        assertNotSame(shapeRef[0], controller.getModel().getClipboard(), "The copied line must be a new object!");
    }
    
    /**
    * Tests that a copy operation can be undone, emptying the clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testUndoAfterCopy(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(385, 290, 30, 20);
            ellipse.getFxShape().setFill(Color.PINK);
            ellipse.getFxShape().setStroke(Color.GREY);
            shapeRef[0] = ellipse;
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.copyShape(new ActionEvent()));

        assertNotNull(controller.getModel().getClipboard(), "Clipboard should contain a copied shape before undo!");

        robot.interact(() -> controller.undoOperation(new ActionEvent()));

        assertNull(controller.getModel().getClipboard(), "Undo should remove the copied shape from clipboard.");
        assertTrue(controller.getModel().getShapes().contains(shapeRef[0]), "Original shape must be on the canvas!");
    }
    
    /**
    * Tests the paste operation after copying a rectangl, adding it to the model and canvas.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testPasteRectangleAfterCopy(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape rectangle = new BorderColorDecorator(
                                new FillColorDecorator(
                                        new MyRectangle(385, 290, 30, 20), Color.RED), Color.BLUE);
           shapeRef[0] = rectangle;
           controller.getModel().addShape(rectangle);
           controller.enableSelection(rectangle);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();

       robot.interact(() -> {
           controller.copyShape(new ActionEvent());
           controller.pasteShape(new ActionEvent());
       });

       assertEquals(2, controller.getModel().getShapes().size(), "A new rectangle should be added to the model!");
   }

   /**
    * Tests the paste operation after copying a ellipse, adding it to the model and canvas.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testPasteEllipseAfterCopy(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape ellipse = new BorderColorDecorator(
                                new FillColorDecorator(
                                        new MyEllipse(385, 290, 30, 20), Color.RED), Color.ORANGE);
           shapeRef[0] = ellipse;
           controller.getModel().addShape(ellipse);
           controller.enableSelection(ellipse);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();

       robot.interact(() -> {
           controller.copyShape(new ActionEvent());
           controller.pasteShape(new ActionEvent());
       });

       assertEquals(2, controller.getModel().getShapes().size(), "A new ellipse should be added to the model!");
   }

   /**
    * Tests the paste operation after copying a line, adding it to the model and canvas.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testPasteLineAfterCopy(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape line = new BorderColorDecorator(
                                new MyLine(385, 290, 30, 20), Color.GREEN);
           shapeRef[0] = line;
           controller.getModel().addShape(line);
           controller.enableSelection(line);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();

       robot.interact(() -> {
           controller.copyShape(new ActionEvent());
           controller.pasteShape(new ActionEvent());
       });

       assertEquals(2, controller.getModel().getShapes().size(), "A new line should be added to the model!");
   }

   /**
    * Tests multiple paste operations from the clipboard.
    */
   @Test
   void testMultiplePaste(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape rectangle = new BorderColorDecorator(
                                new FillColorDecorator(
                                        new MyRectangle(385, 290, 30, 20), Color.GREY), Color.PURPLE);
           shapeRef[0] = rectangle;
           controller.getModel().addShape(rectangle);
           controller.enableSelection(rectangle);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();

       robot.interact(() -> {
           controller.copyShape(new ActionEvent());
           controller.pasteShape(new ActionEvent());
           controller.pasteShape(new ActionEvent());
       });

       assertEquals(3, controller.getModel().getShapes().size(), "Two new shapes should be added to the model!");
   }

   /**
    * Tests the undo operation after pasting a copied shape.
    */
   @Test
   void testUndoAfterPaste(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape ellipse = new BorderColorDecorator(
                                new FillColorDecorator(
                                        new MyEllipse(385, 290, 30, 20), Color.BLUE), Color.BLACK);
           shapeRef[0] = ellipse;
           controller.getModel().addShape(ellipse);
           controller.enableSelection(ellipse);
       });

       robot.interact(() -> {});
       robot.moveTo(shapeRef[0].getFxShape()).clickOn();

       robot.interact(() -> {
           controller.copyShape(new ActionEvent());
           controller.pasteShape(new ActionEvent());
       });

       int countAfterPaste = controller.getModel().getShapes().size();

       robot.interact(() -> controller.undoOperation(new ActionEvent()));

       assertEquals(countAfterPaste - 1, controller.getModel().getShapes().size(), "Undo should remove the pasted shape!");
   }

}
