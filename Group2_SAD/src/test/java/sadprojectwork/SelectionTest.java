
package sadprojectwork;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.layout.Pane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class SelectionTest {
    
    private PaintController controller;
    private static boolean initialized = false;
    
    public SelectionTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        controller = new PaintController();
        controller.setCanvas(new Pane());
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    private ToggleButton createColorButton(Color color) {
        ToggleButton button = new ToggleButton();
        BackgroundFill fill = new BackgroundFill(color, null, null);
        button.setBackground(new Background(fill));
        return button;
    }

    public static void launchJavaFX() {
        if (!initialized) {
            new JFXPanel();
            Platform.setImplicitExit(false);
            initialized = true;
        }
    }

    /**
     * Test of enableSelection method.
     */
    @Test
    public void testRectangleSelection() {
        SelectionTest.launchJavaFX();
  
        ToggleGroup borderGroup = new ToggleGroup();
        ToggleGroup fillGroup = new ToggleGroup();

        ToggleButton borderButton = createColorButton(Color.YELLOW);
        ToggleButton fillButton = createColorButton(Color.GREEN);     

        borderButton.setToggleGroup(borderGroup);
        fillButton.setToggleGroup(fillGroup);

        controller.setBorderColorGroup(borderGroup);
        controller.setFillColorGroup(fillGroup);

        controller.setCanvas(new Pane());
        MyShape rectangle = new MyRectangle(50, 60, 35, 20);
        rectangle.getFxShape().setFill(Color.GREEN);
        rectangle.getFxShape().setStroke(Color.YELLOW);

        controller.getModel().addShape(rectangle);
        controller.getCanvas().getChildren().add(rectangle.getFxShape());

        controller.selectShape(rectangle);

        assertTrue(controller.isShapeSelected(rectangle), "Rectangle should be selected!");
        assertNotNull(rectangle.getFxShape().getEffect(), "If the rectangle is selected, there must be a selection effect!");
        }
    
    /**
     * Test of enableSelection method.
     */
    @Test
    public void testEllipseSelection() {
        SelectionTest.launchJavaFX();
  
        ToggleGroup borderGroup = new ToggleGroup();
        ToggleGroup fillGroup = new ToggleGroup();

        ToggleButton borderButton = createColorButton(Color.YELLOW);
        ToggleButton fillButton = createColorButton(Color.GREEN);     

        borderButton.setToggleGroup(borderGroup);
        fillButton.setToggleGroup(fillGroup);

        controller.setBorderColorGroup(borderGroup);
        controller.setFillColorGroup(fillGroup);

        controller.setCanvas(new Pane());
        MyShape ellipse = new MyEllipse(100, 100, 60, 80);
        ellipse.getFxShape().setFill(Color.BLUE);
        ellipse.getFxShape().setStroke(Color.PURPLE);

        controller.getModel().addShape(ellipse);
        controller.getCanvas().getChildren().add(ellipse.getFxShape());

        controller.selectShape(ellipse);

        assertTrue(controller.isShapeSelected(ellipse), "Ellipse should be selected!");
        assertNotNull(ellipse.getFxShape().getEffect(), "If the ellipse is selected, there must be a selection effect!");
        }
    
    /**
     * Test of enableSelection method.
     */
    @Test
    public void testLineSelection() {
        SelectionTest.launchJavaFX();

        ToggleGroup borderGroup = new ToggleGroup();
        ToggleGroup fillGroup = new ToggleGroup();

        ToggleButton borderButton = createColorButton(Color.YELLOW);
        ToggleButton fillButton = createColorButton(Color.TRANSPARENT);

        borderButton.setToggleGroup(borderGroup);
        fillButton.setToggleGroup(fillGroup);

        controller.setBorderColorGroup(borderGroup);
        controller.setFillColorGroup(fillGroup);

        controller.setCanvas(new Pane());

        MyShape line = new MyLine(0, 0, 30, 10);
        line.getFxShape().setStroke(Color.BLACK);

        controller.getModel().addShape(line);
        controller.getCanvas().getChildren().add(line.getFxShape());

        controller.selectShape(line);

        assertTrue(controller.isShapeSelected(line), "Line should be selected");
        assertNotNull(line.getFxShape().getEffect(), "If the line is selected, there must be a selection effect!");
    }

    
    /**
     * Test of clearSelection method.
     */
    @Test
    public void testDisableSelection(){
        SelectionTest.launchJavaFX();
  
        ToggleGroup borderGroup = new ToggleGroup();
        ToggleGroup fillGroup = new ToggleGroup();

        ToggleButton borderButton = createColorButton(Color.YELLOW);
        ToggleButton fillButton = createColorButton(Color.GREEN);     

        borderButton.setToggleGroup(borderGroup);
        fillButton.setToggleGroup(fillGroup);

        controller.setBorderColorGroup(borderGroup);
        controller.setFillColorGroup(fillGroup);

        controller.setCanvas(new Pane());
        MyShape ellipse = new MyEllipse(100, 100, 60, 80);
        ellipse.getFxShape().setFill(Color.BLUE);
        ellipse.getFxShape().setStroke(Color.PURPLE);

        controller.getModel().addShape(ellipse);
        controller.getCanvas().getChildren().add(ellipse.getFxShape());

        controller.selectShape(ellipse);
        controller.clearSelection();
        
        assertFalse(controller.isShapeSelected(ellipse), "Ellipse should be deselected after clearSelection!");
        assertNull(ellipse.getFxShape().getEffect(), "The selection effect should no longer be present after clearSelection!");

    }
}

