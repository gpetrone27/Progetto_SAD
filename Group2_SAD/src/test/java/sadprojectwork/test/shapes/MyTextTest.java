package sadprojectwork.test.shapes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sadprojectwork.shapes.MyText;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author david
 */
public class MyTextTest {
    
    MyText text;
    
    @BeforeEach
    public void setUp() {
        text = new MyText(50, 100, "Hello world", "Arial", 16, 0);
    }
  
    /**
     * Test of getFontFamily method, of class MyText.
     */
    @Test
    public void testGetFontFamily() {
        assertEquals(text.getFontFamily(), "Arial");
    }

    /**
     * Test of setFontFamily method, of class MyText.
     */
    @Test
    public void testSetFontFamily() {
        text.setFontFamily("Calibri");
        assertEquals(text.getFontFamily(), "Calibri");
    }

    /**
     * Test of getSize method, of class MyText.
     */
    @Test
    public void testGetSize() {
        assertEquals(text.getSize(), 16);
    }

    /**
     * Test of setSize method, of class MyText.
     */
    @Test
    public void testSetSize() {
        text.setSize(20);
        assertEquals(text.getSize(), 20);
    }

    /**
     * Test of getStartX method, of class MyText.
     */
    @Test
    public void testGetStartX() {
        assertEquals(text.getStartX(), 50);
    }

    /**
     * Test of getStartY method, of class MyText.
     */
    @Test
    public void testGetStartY() {
        assertEquals(text.getStartY(), 100);
    }

    /**
     * Test of cloneShape method, of class MyText.
     */
    @Test
    public void testCloneShape() {
        MyText clone = (MyText) text.cloneShape();
        assertNotSame(text, clone);
        assertEquals(text.getStartX(), clone.getStartX());
        assertEquals(text.getStartY(), clone.getStartY());
        assertEquals(text.getFontFamily(), clone.getFontFamily());
        assertEquals(text.getSize(), clone.getSize());
        assertEquals(((javafx.scene.text.Text) text.getFxShape()).getText(),((javafx.scene.text.Text) clone.getFxShape()).getText());
        assertEquals(text.getRotation(), clone.getRotation());
    }

    /**
     * Test of moveTo method, of class MyText.
     */
    @Test
    public void testMoveTo() {
        text.moveTo(200, 150);

        assertEquals(200, text.getStartX());
        assertEquals(150, text.getStartY());
    }


    /**
     * Test of toCSV method, of class MyText.
     */
    @Test
    public void testToCSV() {
        String[] csv = text.toCSV().split(";");
        
        assertEquals("TEXT", csv[0]);
        assertEquals("50.0", csv[1]);
        assertEquals("100.0", csv[2]);
        assertEquals(Double.toString(text.getWidth()), csv[3]);
        assertEquals(Double.toString(text.getHeight()), csv[4]);  
        assertEquals("0.0", csv[7]);
        assertEquals("null", csv[8]); 
        assertEquals("Hello world", csv[9]);
        assertEquals("Arial", csv[10]);
        assertEquals("16.0", csv[11]);
    }
    
}
