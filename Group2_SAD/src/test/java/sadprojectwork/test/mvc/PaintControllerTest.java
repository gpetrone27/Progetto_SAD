
package sadprojectwork.test.mvc;

import sadprojectwork.command.ChangeColorCommand;
import sadprojectwork.command.MoveCommand;
import sadprojectwork.command.Command;
import sadprojectwork.decorator.BorderColorDecorator;
import sadprojectwork.decorator.FillColorDecorator;
import sadprojectwork.factory.ShapeFactoryManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import sadprojectwork.shapes.MyRectangle;
import sadprojectwork.shapes.MyShape;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import sadprojectwork.mvc.PaintController;
import sadprojectwork.shapes.MyCompositeShape;
import sadprojectwork.shapes.MyPolygon;
import sadprojectwork.shapes.MyText;
import sadprojectwork.shapes.Shapes;

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
    private ShapeFactoryManager factoryManager = new ShapeFactoryManager();
    
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.BLACK);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
            rectangle.getFxShape().setFill(Color.GREEN);
            rectangle.getFxShape().setStroke(Color.YELLOW);
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 100, 100, 40, 40, 0, false, false);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.PURPLE);
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
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.YELLOW);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, oldX, oldY, 60, 40, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, oldX, oldY, 60, 40, 0, false, false);
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
            MyShape line = factoryManager.createShape(Shapes.LINE,oldX, oldY, 60, 40, 0, false, false);
            line = new BorderColorDecorator(line, Color.PINK);
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
           MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, oldX, oldY, 40, 30, 0, false, false);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.PURPLE);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
           MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
           MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
           MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
           line = new BorderColorDecorator(line, Color.ORANGE);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
    * Tests that a selected rectangle is copied to clipboard.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
    void testCopyRectangle(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.YELLOW);
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
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
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
           MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
           rectangle = new BorderColorDecorator(rectangle, Color.RED);
           rectangle = new FillColorDecorator(rectangle, Color.ORANGE);
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
           MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
           ellipse = new BorderColorDecorator(ellipse, Color.RED);
           ellipse = new FillColorDecorator(ellipse, Color.ORANGE);
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
           MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
           line = new BorderColorDecorator(line, Color.GREEN);
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
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testMultiplePaste(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
           rectangle = new BorderColorDecorator(rectangle, Color.GREY);
           rectangle = new FillColorDecorator(rectangle, Color.PURPLE);shapeRef[0] = rectangle;
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
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
   @Test
   void testUndoAfterPaste(FxRobot robot) {
       MyShape[] shapeRef = new MyShape[1];

       Platform.runLater(() -> {
           MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 385, 290, 30, 20, 0, false, false);
           ellipse = new BorderColorDecorator(ellipse, Color.BLUE);
           ellipse = new FillColorDecorator(ellipse, Color.BLACK);
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
   
   /**
    * Tests the bring to the front a selected shape.
    * 
    * @param TestFX robot: robot to simulate user interactions. 
    */
   @Test
    void testBringToFront(FxRobot robot) {
        MyShape[] rectRef = new MyShape[1];
        MyShape[] lineRef = new MyShape[1];

        robot.interact(() -> {
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
            rectangle = new BorderColorDecorator(new FillColorDecorator(rectangle, Color.LIGHTBLUE), Color.LIGHTBLUE);
            
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 296, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.RED);
            
            rectRef[0] = rectangle;
            lineRef[0] = line;

            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.moveTo(rectRef[0].getFxShape()).clickOn();
        
        robot.interact(() -> controller.bringToFront(new ActionEvent()));

        List<Node> canvasChildren = controller.getCanvas().getChildren();
        assertEquals(rectRef[0].getFxShape(), canvasChildren.get(canvasChildren.size() - 1),
                "The rectangle must be in front of the line in the canvas!");

        List<MyShape> modelShapes = controller.getModel().getShapes();
        assertEquals(rectRef[0], modelShapes.get(modelShapes.size() - 1),
                "The rectangle must be last in the model!");
    }

    /**
    * Tests the bring to the back a selected shape.
    * 
    * @param TestFX robot: robot to simulate user interactions. 
    */
    @Test
    void testBringToBack(FxRobot robot){
        MyShape[] rectRef = new MyShape[1];
        MyShape[] lineRef = new MyShape[1];

        robot.interact(() -> {
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
            rectangle = new BorderColorDecorator(new FillColorDecorator(rectangle, Color.LIGHTPINK), Color.LIGHTPINK);
            
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 296, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.PURPLE);
            
            rectRef[0] = rectangle;
            lineRef[0] = line;

            controller.getModel().addShape(rectangle);
            controller.enableSelection(rectangle);
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.moveTo(lineRef[0].getFxShape()).clickOn();
        
        robot.interact(() -> controller.bringToBack(new ActionEvent()));

        List<Node> canvasChildren = controller.getCanvas().getChildren();
        assertEquals(lineRef[0].getFxShape(), canvasChildren.get(0),
                "The rectangle must be in front of the line in the canvas!");

        List<MyShape> modelShapes = controller.getModel().getShapes();
        assertEquals(lineRef[0], modelShapes.get(0),
                "The line must be first in the model!");        
    }
    
    /**
    * Tests the undo operation after bringing to front a shape.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testUndoBringToFront(FxRobot robot) {
        MyShape[] rectRef = new MyShape[1];
        MyShape[] lineRef = new MyShape[1];

        robot.interact(() -> {
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
            rectangle = new BorderColorDecorator(rectangle, Color.PINK);
            rectangle = new FillColorDecorator(rectangle, Color.LIGHTPINK);
            
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.PURPLE);
            
            rectRef[0] = rectangle;
            lineRef[0] = line;

            controller.getModel().addShape(rectangle);
            controller.getModel().addShape(line);
            controller.enableSelection(rectangle);
        });

        robot.moveTo(rectRef[0].getFxShape()).clickOn();
        robot.interact(() -> controller.bringToFront(new ActionEvent()));

        robot.interact(() -> controller.undoOperation(new ActionEvent()));

        List<Node> canvasChildren = controller.getCanvas().getChildren();
        assertTrue(canvasChildren.indexOf(rectRef[0].getFxShape()) < canvasChildren.indexOf(lineRef[0].getFxShape()),
                "The rectangle should be behind the line afted undo!");
    }
    
    /**
    * Tests the undo operation after bringing to back a shape.
    * 
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testUndoBringToBack(FxRobot robot) {
        MyShape[] rectRef = new MyShape[1];
        MyShape[] lineRef = new MyShape[1];

        robot.interact(() -> {
            MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
            rectangle = new BorderColorDecorator(rectangle, Color.PINK);
            rectangle = new FillColorDecorator(rectangle, Color.LIGHTPINK);
            
            MyShape line = factoryManager.createShape(Shapes.LINE, 385, 290, 30, 20, 0, false, false);
            line = new BorderColorDecorator(line, Color.PURPLE);
            
            rectRef[0] = rectangle;
            lineRef[0] = line;

            controller.getModel().addShape(rectangle);
            controller.getModel().addShape(line);
            controller.enableSelection(line);
        });

        robot.moveTo(lineRef[0].getFxShape()).clickOn();
        robot.interact(() -> controller.bringToBack(new ActionEvent()));

        robot.interact(() -> controller.undoOperation(new ActionEvent()));

        List<Node> canvasChildren = controller.getCanvas().getChildren();
        assertTrue(canvasChildren.indexOf(lineRef[0].getFxShape()) > canvasChildren.indexOf(rectRef[0].getFxShape()),
                "The line should be back in front of the rectangle after undo!");
    }
    
    /**
    * Tests the zoom in and zoom out in the canvas.
    * 
    * @param TestFX robot: robot to simulate user interactions. 
    */
    @Test
    void testZoom(FxRobot robot) {
        Pane canvas = robot.lookup("#canvas").queryAs(Pane.class);

        MyShape rectangle = factoryManager.createShape(Shapes.RECTANGLE, 385, 290, 30, 20, 0, false, false);
        rectangle = new BorderColorDecorator(rectangle, Color.PINK);
        rectangle = new FillColorDecorator(rectangle, Color.LIGHTPINK);
        
        final MyShape rectangleDecorator = rectangle;
        
        robot.interact(() -> {
            Shape shape = rectangleDecorator.getFxShape();
            canvas.getChildren().add(shape);
        });

        Shape fxShape = (Shape) canvas.getChildren().get(canvas.getChildren().size() - 1);

        double beforein = fxShape.localToScene(fxShape.getBoundsInLocal()).getWidth();

        robot.clickOn("#zoomInButton");
        robot.clickOn("#zoomInButton");
        robot.clickOn("#zoomInButton");
        robot.clickOn("#zoomInButton");

        double afterin = fxShape.localToScene(fxShape.getBoundsInLocal()).getWidth();
        assertTrue(afterin > beforein, "The zoom is not increased!");

        robot.clickOn("#zoomOutButton");
        robot.clickOn("#zoomOutButton");
        robot.clickOn("#zoomOutButton");
        robot.clickOn("#zoomOutButton");

        double afterout = fxShape.localToScene(fxShape.getBoundsInLocal()).getWidth();
        assertEquals(beforein, afterout, "The zoom is not decremented!");
    }

    /**
     * Tests the vertical scroll of the scrollbar.
     *
     * @param robot
     * @throws InterruptedException
     */
    @Test
    void testVerticalScroll(FxRobot robot) throws InterruptedException {
        // Adding shapes vertically 
        Platform.runLater(() -> {
            for (int i = 0; i < 100; i++) {
                MyShape rect = new MyRectangle(50, i * 50, 30, 20, 0, false, false);
                controller.getModel().addShape(rect);
            }
        });

        robot.interact(() -> {
        });

        javafx.scene.control.ScrollPane scrollPane = controller.drawingPane;

        // Save initial value  
        double initialValue = scrollPane.getVvalue();

        // Simulate a downwards
        robot.moveTo(scrollPane).scroll(30);

        double afterScrollDown = scrollPane.getVvalue();
        assertTrue(afterScrollDown > initialValue, "Scroll downwardso: the value should increase");

        // Simulate a upward
        robot.moveTo(scrollPane).scroll(-20);

        double afterScrollUp = scrollPane.getVvalue();
        assertTrue(afterScrollUp < afterScrollDown, "Scroll upwards: the value should decrease");
    }

    /**
     * Tests the horizontal scroll of the scrollbar.
     *
     * @param robot
     * @throws InterruptedException
     */
    @Test
    void testHorizontalScroll(FxRobot robot) throws InterruptedException {
        
        Platform.runLater(() -> {
            for (int i = 0; i < 100; i++) {
                MyShape rect = new MyRectangle(i * 50, 100, 30, 20, 0, false, false); 
                controller.getModel().addShape(rect);
            }
        });

        robot.interact(() -> {
        }); 

        javafx.scene.control.ScrollPane scrollPane = controller.drawingPane;
        double initialHValue = scrollPane.getHvalue();

        // Simulate horizontal right scroll 
        for (int i = 0; i <= 20; i++) {
            double progress = i / 10.0; 
            double finalProgress = progress;
            Platform.runLater(() -> scrollPane.setHvalue(finalProgress));
            robot.interact(() -> {
            }); 
        } 

        double afterScrollRight = scrollPane.getHvalue();

        assertTrue(afterScrollRight > initialHValue, "Right scroll: the value should increase");

        // Simulate horizontal left scroll 
        for (int i = 20; i >= 0; i--) {
            double progress = i / 10.0;
            Platform.runLater(() -> scrollPane.setHvalue(progress));
            robot.interact(() -> {
            });
        }
        double afterScrollLeft = scrollPane.getHvalue();

        assertTrue(afterScrollLeft < afterScrollRight, "Left Scroll: the value should decrease");
    }
    
    /**
    * Tests the grid toggle functionality.
    *
    * @param TestFX robot: robot to simulate user interactions.
    */
    @Test
    void testToggleGrid(FxRobot robot) {
        Pane canvas = robot.lookup("#canvas").queryAs(Pane.class);
       robot.clickOn("#gridButton");

       long gridLinesCount = canvas.getChildren().stream()
               .filter(node -> node instanceof Line && "grid".equals(node.getUserData()))
               .count();

       assertTrue(gridLinesCount > 0, "The grid should be visible now!");
       
       robot.clickOn("#gridButton");
       
       long remainingLines = canvas.getChildren().stream()
               .filter(node -> node instanceof Line && "grid".equals(node.getUserData()))
               .count();

       assertEquals(0, remainingLines, "The grid should be unvisible now!");
   }

    /**
     * Tests the resizing of the grid via the slider.
     *
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testResizeGrid(FxRobot robot) {
        Pane canvas = robot.lookup("#canvas").queryAs(Pane.class);
        Slider gridSlider = robot.lookup("#gridSizeSlider").queryAs(Slider.class);

        robot.clickOn("#gridButton");

        // Initial spacing value (e.g. 0)
        Platform.runLater(() -> gridSlider.setValue(10));
        robot.sleep(500);

        // Saves the X coordinates of vertical lines
        List<Double> initialXCoords = canvas.getChildren().stream()
            .filter(node -> node instanceof Line && "grid".equals(node.getUserData()))
            .map(node -> (Line) node)
            .filter(line -> line.getStartX() == line.getEndX()) // Vertical line
            .map(Line::getStartX)
            .sorted()
            .collect(Collectors.toList());

        assertTrue(initialXCoords.size() > 1);

        double initialSpacing = initialXCoords.get(1) - initialXCoords.get(0);

        // Changes grid spacing (e.g. 40)
        Platform.runLater(() -> gridSlider.setValue(40));
        robot.sleep(500);

        List<Double> updatedXCoords = canvas.getChildren().stream()
            .filter(node -> node instanceof Line && "grid".equals(node.getUserData()))
            .map(node -> (Line) node)
            .filter(line -> line.getStartX() == line.getEndX())
            .map(Line::getStartX)
            .sorted()
            .collect(Collectors.toList());

        assertTrue(updatedXCoords.size() > 1);

        double updatedSpacing = updatedXCoords.get(1) - updatedXCoords.get(0);

        assertNotEquals(initialSpacing, updatedSpacing);
        assertEquals(40.0, updatedSpacing, 1.0);
    }

    /**
     * Tests creation, selection, movement and resizing of a MyPolygon shape.
     * @param TestFX robot: robot to simulate user interactions.
     */
    @Test
    void testPolygonCreationMoveAndResize(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        double x1 = 100, y1 = 100;
        double x2 = 150, y2 = 120;
        double x3 = 120, y3 = 180;

        double newX = 200, newY = 220;

        double newWidth = 80, newHeight = 100;

        Platform.runLater(() -> {
            List<Point2D> points = new ArrayList<>();
            points.add(new Point2D(x1, y1));
            points.add(new Point2D(x2, y2));
            points.add(new Point2D(x3, y3));
            MyPolygon polygon = new MyPolygon(x1, y1, points, 0, false, false);
            polygon.getFxShape().setFill(Color.CYAN);
            polygon.getFxShape().setStroke(Color.BLACK);
            shapeRef[0] = polygon;
            controller.getModel().addShape(polygon);
            controller.enableSelection(polygon);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Platform.runLater(() -> shapeRef[0].moveTo(newX, newY));
        robot.interact(() -> {});
        assertEquals(newX, shapeRef[0].getStartX(), 0.001, "Polygon X should be updated after move!");
        assertEquals(newY, shapeRef[0].getStartY(), 0.001, "Polygon Y should be updated after move!");

        Platform.runLater(() -> shapeRef[0].resize(newWidth, newHeight));
        robot.interact(() -> {});
        assertEquals(newWidth, shapeRef[0].getWidth(), 0.001, "Polygon width should be updated after resize!");
        assertEquals(newHeight, shapeRef[0].getHeight(), 0.001, "Polygon height should be updated after resize!");
    }

    /**
     * Tests the color change of a selected polygon.
     * @param TestFX robot: robot to simulate user interaction
     */
    @Test
    void testChangeColorPolygon(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        Platform.runLater(() -> {
            List<Point2D> points = new ArrayList<>();
            points.add(new Point2D(100, 100));
            points.add(new Point2D(150, 120));
            points.add(new Point2D(120, 180));
            MyPolygon polygon = new MyPolygon(100, 100, points, 0, false, false);
            polygon.getFxShape().setFill(Color.GRAY);
            polygon.getFxShape().setStroke(Color.BLACK);
            shapeRef[0] = polygon;
            controller.getModel().addShape(polygon);
            controller.enableSelection(polygon);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        Color newFill = Color.PINK;
        Color newBorder = Color.BLUE;

        robot.interact(() -> {
            Command changeColor = new ChangeColorCommand(shapeRef[0], newFill, newBorder);
            controller.getModel().execute(changeColor);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Polygon fill color should be updated!");
        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Polygon border color should be updated!");
    }

    /**
     * Tests creation, color change, movement and rotation of a MyText shape.
     * @param robot FxRobot instance for UI simulation.
     */
    @Test
    void testTextPlacementMoveColorAndRotation(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        // Initial values
        double startX = 100, startY = 150;
        String content = "Hello World";
        String fontFamily = "Arial";
        double fontSize = 20;
        double initialRotation = 0;

        // Target values
        double newX = 200, newY = 250;
        double newRotation = 45;
        Color newFill = Color.GREEN;
        Color newBorder = Color.DARKBLUE;

        Platform.runLater(() -> {
            BorderColorDecorator text = new BorderColorDecorator(new FillColorDecorator(new MyText(startX, startY, content, fontFamily, fontSize, initialRotation, false, false), Color.TRANSPARENT), Color.BLACK);
            text.getFxShape().setFill(Color.BLACK);
            text.getFxShape().setStroke(Color.RED);
            shapeRef[0] = text;
            controller.getModel().addShape(text);
            controller.enableSelection(text);
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        // Changes color
        robot.interact(() -> {
            Command colorCmd = new ChangeColorCommand(shapeRef[0], newFill, newBorder);
            controller.getModel().execute(colorCmd);
        });

        assertEquals(newFill, shapeRef[0].getFxShape().getFill(), "Text fill color should be updated!");
        assertEquals(newBorder, shapeRef[0].getFxShape().getStroke(), "Text border color should be updated!");

        // Changes position
        Platform.runLater(() -> shapeRef[0].moveTo(newX, newY));
        robot.interact(() -> {});
        assertEquals(newX, shapeRef[0].getStartX(), 0.001, "Text X should be updated after move!");
        assertEquals(newY, shapeRef[0].getStartY(), 0.001, "Text Y should be updated after move!");

        // Changes Rotation
        Platform.runLater(() -> shapeRef[0].setRotation(newRotation));
        robot.interact(() -> {});
        assertEquals(newRotation, shapeRef[0].getRotation(), 0.001, "Text rotation should be updated!");
    }
    
    /**
     * Tests that a selected shape is mirrored horizontally.
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testMirrorHorizontally(FxRobot robot) {
        MyShape[] shapeRef = new MyShape[1];

        robot.interact(() -> {
            MyShape triangle = factoryManager.createShape(Shapes.POLYGON, 0, 0, 0, 0, 0, false, false);
            
            MyPolygon trianglePolygon = (MyPolygon) triangle;

            trianglePolygon.addPoint(new Point2D(0, 0));
            trianglePolygon.addPoint(new Point2D(60, 20));
            trianglePolygon.addPoint(new Point2D(20, 60));
            trianglePolygon.closePolygon();
            
            MyShape triangleDecorator = new BorderColorDecorator(
                                new FillColorDecorator(trianglePolygon, Color.LIGHTBLUE),
                                Color.BLUE);

            controller.getModel().addShape(triangleDecorator);
            controller.enableSelection(triangleDecorator);

            shapeRef[0] = triangleDecorator;
        });

        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.mirrorHorizontally(new ActionEvent()));

        assertTrue(shapeRef[0].getScaleX() < 0, "Shape must be mirrored horizontally!");
    }

    /**
     * Tests undo functionality for horizontal mirror.
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testUndoMirrorHorizontally(FxRobot robot) throws Exception {
        MyShape[] shapeRef = new MyShape[1];

        robot.interact(() -> {
            MyShape triangle = factoryManager.createShape(Shapes.POLYGON, 0, 0, 0, 0, 0, false, false);
            
            MyPolygon trianglePolygon = (MyPolygon) triangle;

            trianglePolygon.addPoint(new Point2D(0, 0));
            trianglePolygon.addPoint(new Point2D(60, 20));
            trianglePolygon.addPoint(new Point2D(20, 60));
            trianglePolygon.closePolygon();
            
            MyShape triangleDecorator = new BorderColorDecorator(
                                new FillColorDecorator(trianglePolygon, Color.LIGHTBLUE),
                                Color.BLUE);

            controller.getModel().addShape(triangleDecorator);
            controller.enableSelection(triangleDecorator);

            shapeRef[0] = triangleDecorator;
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.mirrorHorizontally(new ActionEvent()));

        robot.interact(() -> controller.undoOperation(new ActionEvent()));
        
        assertTrue(shapeRef[0].getScaleX() > 0, "Undo must restore scaleX positive!");
    } 

    /**
     * Tests that a selected shape is mirrored vertically.
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testMirrorVertically(FxRobot robot) throws Exception {
        MyShape[] shapeRef = new MyShape[1];

        robot.interact(() -> {
            MyShape triangle = factoryManager.createShape(Shapes.POLYGON, 0, 0, 0, 0, 0, false, false);
            
            MyPolygon trianglePolygon = (MyPolygon) triangle;

            trianglePolygon.addPoint(new Point2D(0, 0));
            trianglePolygon.addPoint(new Point2D(60, 20));
            trianglePolygon.addPoint(new Point2D(20, 60));
            trianglePolygon.closePolygon();
            
            MyShape triangleDecorator = new BorderColorDecorator(
                                new FillColorDecorator(trianglePolygon, Color.LIGHTBLUE),
                                Color.BLUE);

            controller.getModel().addShape(triangleDecorator);
            controller.enableSelection(triangleDecorator);

            shapeRef[0] = triangleDecorator;
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.mirrorVertically(new ActionEvent()));

        assertTrue(shapeRef[0].getScaleY() < 0, "Shape must be mirrored vertically!");
    }

    /**
     * Tests undo functionality for vertical mirror.
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testUndoMirrorVertically(FxRobot robot) throws Exception {
        MyShape[] shapeRef = new MyShape[1];

        robot.interact(() -> {
            MyShape triangle = factoryManager.createShape(Shapes.POLYGON, 0, 0, 0, 0, 0, false, false);
            
            MyPolygon trianglePolygon = (MyPolygon) triangle;

            trianglePolygon.addPoint(new Point2D(0, 0));
            trianglePolygon.addPoint(new Point2D(60, 20));
            trianglePolygon.addPoint(new Point2D(20, 60));
            trianglePolygon.closePolygon();
            
            MyShape triangleDecorator = new BorderColorDecorator(
                                new FillColorDecorator(trianglePolygon, Color.LIGHTBLUE),
                                Color.BLUE);

            controller.getModel().addShape(triangleDecorator);
            controller.enableSelection(triangleDecorator);

            shapeRef[0] = triangleDecorator;
        });

        robot.interact(() -> {});
        robot.moveTo(shapeRef[0].getFxShape()).clickOn();

        robot.interact(() -> controller.mirrorVertically(new ActionEvent()));

        robot.interact(() -> controller.undoOperation(new ActionEvent()));

        assertTrue(shapeRef[0].getScaleY() > 0, "Undo must restore scaleY positive!");
    }
    
    /**
     * Tests that clicking the "Group" menu item groups selected shapes into a MyCompositeShape.
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testGroupMenuItemCreatesCompositeShape(FxRobot robot) {
        MyShape[] shape1Ref = new MyShape[1];
        MyShape[] shape2Ref = new MyShape[1];

        robot.interact(() -> {
            MyShape rect = factoryManager.createShape(Shapes.RECTANGLE, 100, 100, 50, 50, 0, false, false);
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 200, 100, 60, 60, 0, false, false);

            controller.getModel().addShape(rect);
            controller.getModel().addShape(ellipse);

            controller.enableSelection(rect);
            controller.enableSelection(ellipse);

            shape1Ref[0] = rect;
            shape2Ref[0] = ellipse;
        });

        // Select the shapes
        robot.moveTo(shape1Ref[0].getFxShape()).clickOn();
        robot.press(KeyCode.CONTROL);
        robot.moveTo(shape2Ref[0].getFxShape()).clickOn();
        robot.release(KeyCode.CONTROL);
        robot.moveTo(shape1Ref[0].getFxShape()).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#groupMenuItem");

        // Verifies that a MyCompositeShape is created
        List<MyShape> shapes = controller.getModel().getShapes();
        long compositeCount = shapes.stream().filter(s -> s instanceof MyCompositeShape).count();
        assertEquals(1, compositeCount, "A single composite shape should be created");

        // Checks that the original shapes are contained in the composite
        MyCompositeShape group = (MyCompositeShape) shapes.stream()
                .filter(s -> s instanceof MyCompositeShape)
                .findFirst()
                .orElse(null);

        assertNotNull(group, "Grouped shape must not be null");
        assertTrue(group.getShapes().contains(shape1Ref[0]), "Group must contain the first shape");
        assertTrue(group.getShapes().contains(shape2Ref[0]), "Group must contain the second shape");

        // Verify that the original shapes have been removed from the model
        assertFalse(controller.getModel().getShapes().contains(shape1Ref[0]), "Original shape 1 must be removed from model");
        assertFalse(controller.getModel().getShapes().contains(shape2Ref[0]), "Original shape 2 must be removed from model");

    }
    
    /**
     * Tests that right-clicking on a grouped shape and selecting "Ungroup" unpacks the composite shape,
     * restoring the original individual shapes into the model.
     * @param robot TestFX robot to simulate user interactions.
     */
    @Test
    void testUngroupCompositeShape(FxRobot robot) {
        MyShape[] shape1Ref = new MyShape[1];
        MyShape[] shape2Ref = new MyShape[1];
        MyCompositeShape[] compositeRef = new MyCompositeShape[1];

        // Create two shapes and add them to the model
        robot.interact(() -> {
            MyShape rect = factoryManager.createShape(Shapes.RECTANGLE, 100, 100, 50, 50, 0, false, false);
            MyShape ellipse = factoryManager.createShape(Shapes.ELLIPSE, 200, 100, 60, 60, 0, false, false);

            controller.getModel().addShape(rect);
            controller.getModel().addShape(ellipse);

            controller.enableSelection(rect);
            controller.enableSelection(ellipse);

            shape1Ref[0] = rect;
            shape2Ref[0] = ellipse;
        });

        // Select both shapes with Ctrl+click
        robot.moveTo(shape1Ref[0].getFxShape()).clickOn();
        robot.press(KeyCode.CONTROL);
        robot.moveTo(shape2Ref[0].getFxShape()).clickOn();
        robot.release(KeyCode.CONTROL);

        // Right-click on one shape and click the "Group" menu item
        robot.moveTo(shape1Ref[0].getFxShape()).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#groupMenuItem");

        // Retrieve the created composite shape
        robot.interact(() -> {
            compositeRef[0] = (MyCompositeShape) controller.getModel().getShapes()
                    .stream()
                    .filter(s -> s instanceof MyCompositeShape)
                    .findFirst()
                    .orElse(null);
        });

        assertNotNull(compositeRef[0], "Composite shape must exist after grouping");

        // Right-click on the composite and click "Ungroup"
        robot.moveTo(shape1Ref[0].getFxShape()).clickOn(MouseButton.SECONDARY);
        robot.clickOn("#ungroupMenuItem");

        // Ensure the composite shape has been removed from the model
        assertFalse(controller.getModel().getShapes().contains(compositeRef[0]), "Composite shape must be removed after ungroup");

        // Ensure the original shapes are back in the model
        assertTrue(controller.getModel().getShapes().contains(shape1Ref[0]), "Shape 1 must be restored in the model");
        assertTrue(controller.getModel().getShapes().contains(shape2Ref[0]), "Shape 2 must be restored in the model");
    }

}

