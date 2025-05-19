
package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import sadprojectwork.*;
import javafx.scene.paint.Color;

public class PaintControllerTest {
    
    private PaintController controller;
    
    public PaintControllerTest() {
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
        controller.setModel(new Model());
        controller.setCanvas(new Pane());
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of initialize method, of class PaintController.
     */
    /*@Test
    public void testInitialize() {
        System.out.println("initialize");
        URL url = null;
        ResourceBundle rb = null;
        PaintController instance = new PaintController();
        instance.initialize(url, rb);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getSelectedShape method, of class PaintController.
     */
    /*@Test
    public void testGetSelectedShape() {
        System.out.println("getSelectedShape");
        PaintController instance = new PaintController();
        MyShape expResult = null;
        MyShape result = instance.getSelectedShape();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
    /**
     * Test of enableSelection method, of class PaintController.
     */
    @Test
    public void testEllipseSelection(){
        MyShape ellipse = new FillColorDecorator(
                                new BorderColorDecorator(
                                    new MyEllipse(100, 100, 60, 80), Color.GREEN),Color.YELLOW);
        
        controller.enableSelection(ellipse);
        assertTrue(controller.isShapeSelected(ellipse), "The ellipse must be selected!");
        assertNotNull(ellipse.getFxShape().getEffect(), "The ellipse must have a selection effect");
    }
    
    /**
     * Test of enableSelection method, of class PaintController.
     */
    @Test
    public void testRectangleSelection(){
        MyShape rectangle = new FillColorDecorator(
                                new BorderColorDecorator(
                                    new MyRectangle(100, 100, 60, 80), Color.RED),Color.GREEN);
        
        controller.enableSelection(rectangle);
        assertTrue(controller.isShapeSelected(rectangle), "The rectangle must be selected!");
        assertNotNull(rectangle.getFxShape().getEffect(), "The rectangle must have a selection effect");
    }
    
    /**
     * Test of enableSelection method, of class PaintController.
     */
    @Test
    public void testLineSelection(){
        MyShape line =new BorderColorDecorator(
                            new MyLine(0, 0, 30, 20),Color.BLACK);
        
        controller.enableSelection(line);
        assertTrue(controller.isShapeSelected(line), "The line must be selected!");
        assertNotNull(line.getFxShape().getEffect(), "The line must have a selection effect");
    }
    
    /**
     * Test of clearSelection method, of class PaintController.
     */
    @Test
    public void testDisableSelection(){
        MyShape ellipse = new FillColorDecorator(
                                new BorderColorDecorator(
                                    new MyEllipse(100, 100, 60, 80), Color.GREEN),Color.YELLOW);
       
        controller.enableSelection(ellipse);
        controller.clearSelection();
        assertNull(controller.getSelectedShape(), "No shape must be selected!");
        assertNull(ellipse.getFxShape().getEffect(), "The effect must be removed");
    }
    
    /**
     * Test of enableSelection method, of class PaintController.
     */
    @Test
    public void testNewSelectionWithShapeSelected(){
        MyShape ellipse = new FillColorDecorator(
                                new BorderColorDecorator(
                                    new MyEllipse(100, 100, 60, 80), Color.GREEN),Color.YELLOW);
        MyShape rectangle = new FillColorDecorator(
                                 new BorderColorDecorator(
                                     new MyRectangle(100, 100, 60, 80), Color.RED),Color.GREEN);
        
        controller.enableSelection(ellipse);
        controller.enableSelection(rectangle);
        
        assertEquals(rectangle, controller.getSelectedShape(), "The second shape must be selected");
        assertNotNull(rectangle.getFxShape().getEffect(), "The secondo shape must be highlighted");
        assertNull(ellipse.getFxShape().getEffect(), "The first shape should no longer be highlighted");
    }
    
    /*@Test
    public void testDeleteRectangle() {
       MyShape rectangle = new FillColorDecorator(
                                 new BorderColorDecorator(
                                     new MyRectangle(100, 100, 60, 80), Color.BLUE),Color.PURPLE);
       
       controller.getModel().addShape(rectangle);
       controller.getCanvas().getChildren().add(rectangle.getFxShape());
       
       controller.enableSelection(rectangle);
       controller.deleteShape(new ActionEvent());
       
       assertFalse(controller.getModel().getShapes().contains(rectangle), "The shape must be deleted from the model!");
       assertFalse(controller.getCanvas().getChildren().contains(rectangle), "The shape must be deleted from the canvas!");
       
    }*/
            
}
