
package sadprojectwork;

import javafx.scene.shape.Shape;

/**
 * Abstract base class for all custom shapes in the drawing application.
 * Defines common properties and methods shared by all shapes.
 */
public abstract class MyShape {

    // Wrapped FX shape
    protected Shape fxShape;
    
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

    /**
     * Returns the wrapped FX shape.
     * @return fxShape
     */
    public Shape getFxShape() {
        return fxShape;
    }
    
    /**
     * Returns the starting position X value.
     * @return startX
     */
    public double getStartX() {
        return startX;
    }

    /**
     * Returns the starting position Y value.
     * @return startY
     */
    public double getStartY() {
        return startY;
    }

    /**
     * Resizes the shape based on the new dimensions.
     * Lines only use length.
     * Rectangles and ellipses use width and height.
     * @param dimensions
     */
    public abstract void resize(double... dimensions);

    // Creates and returns a copy of the shape
    public abstract MyShape cloneShape();

    // Set the new position of the shape on the pane
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
    }

    /**
     * Returns the first dimension of the shape.
     * Lines return their length.
     * Rectangles and ellipses return their width.
     * @return 
     */
    public abstract double getWidth();
    
    /**
     * Returns the second dimension of the shape.
     * Lines return 0.
     * Rectangles and ellipses return their height.
     * @return 
     */
    public abstract double getHeight();
    
    public void moveOf(double dx, double dy) {
        setPosition(getStartX() + dx, getStartY() + dy);
    }
    
    public void moveTo(double x, double y) {
        setPosition(x, y);
    }
    
    /**
     * Returns a String in CSV format containing all the shapes fields
     * @return 
     */
    public abstract String toCSV();

}
