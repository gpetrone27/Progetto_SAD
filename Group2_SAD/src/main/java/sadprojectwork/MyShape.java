
package sadprojectwork;

import javafx.scene.shape.Shape;

/**
 * Abstract base class for all custom shapes in the drawing application.
 * Defines common properties and methods shared by all shapes.
 */
public abstract class MyShape {

    // Start coordinates of the shape
    protected double startX;
    protected double startY;
    
    // Dimensions of the shape
    protected double width;
    protected double height;

    public MyShape(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
    }

    // Return the javaFX shape
    public abstract Shape getFxShape();

    // Resizes the shape based on width and height or delta values
    // for shapes like rectangles and ellipses, this changes size.
    // for lines, this may be interpreted differently.
    public abstract void resize(double newWidth, double newHeight);

    // Creates and returns a copy of the shape
    public abstract MyShape cloneShape();

    // Set the new position of the shape on the pane
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
    }

    // Return the starting X coordinate of the shape
    public double getStartX() {
        return startX;
    }

    // Return the starting X coordinate of the shape 
    public double getStartY() {
        return startY;
    }
    
    // Return the width of the shape
    public double getWidth() {
        return width;
    }
    
    // Return the height of the shape
    public double getHeight() {
        return height;
    }
    
    public void moveOf(double updtX, double updtY) {
        setPosition(getStartX() + updtX, getStartY() + updtY);
    }
    
    public void moveTo(double xNew, double yNew) {
        setPosition(xNew, yNew);
    }

}
