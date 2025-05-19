/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
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
 *
 * @author noemi
 */
@ExtendWith(ApplicationExtension.class)
public class PaintControllerTest {
    private PaintController controller;
    
    public PaintControllerTest() {
    }
    
   @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/arsnova.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
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
    
    @Test
    void testEllipseSelected(FxRobot robot) {
        Platform.runLater(() -> {
            MyShape ellipse = new MyEllipse(50, 50, 80, 60);
            ellipse.getFxShape().setFill(Color.GREEN);
            ellipse.getFxShape().setStroke(Color.YELLOW);
            controller.getModel().addShape(ellipse);
            controller.enableSelection(ellipse);
        });

        robot.interact(() -> {});

        robot.moveTo(controller.getCanvas().getChildren().get(0)).clickOn();

        MyShape selected = controller.getSelectedShape();
        assertNotNull(selected, "The ellipse must be selected!");
        assertTrue(selected.getFxShape().getEffect() instanceof DropShadow, "If the ellipse is selected, there must be a selection effect!");
    }
    
}
