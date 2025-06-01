
package sadprojectwork.shapes;

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
    
    protected double scaleX = 1.0;
    protected double scaleY = 1.0;
    
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
     * @param newWidth
     * @param newHeight
     */
    public abstract void resize(double newWidth, double newHeight);

    // Creates and returns a copy of the shape
    public abstract MyShape cloneShape();

    // Moves the shape to the specified coordinates
    public abstract void moveTo(double x, double y);

    // Moves the shape of the specified deltas
    public void moveOf(double dx, double dy) {
        moveTo(getStartX() + dx, getStartY() + dy);
    }
    
    /**
     * Returns the width of the shape.
     * @return 
     */
    public abstract double getWidth();
    
    /**
     * Returns the height of the shape.
     * @return 
     */
    public abstract double getHeight();
    
    public double getRotation() {
        return getFxShape().getRotate();
    }
    
    public void setRotation (double rotation) {
        getFxShape().setRotate(rotation);
    }
    
    /**
     * Applies a multiplicative factor to the current horizontal scale and updates the scaleX 
     * property of the associated JavaFX node.
     * @param factor 
     */
    public void applyScaleX(double factor) {
        scaleX *= factor;
        getFxShape().setScaleX(scaleX);
    }

    /**
     * Applies a multiplicative factor to the current vertical scale and updates the scaleY 
     * property of the associated JavaFX node.
     * @param factor 
     */
    public void applyScaleY(double factor) {
        scaleY *= factor;
        getFxShape().setScaleY(scaleY);
    }
    
    /**
     * Returns the current value of the horizontal scale.
     * @return 
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * Returns the current value of the vertical scale.
     * @return 
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * Mirror horizontally by reversing the X scale.
     */
    public void mirrorHorizontally() {
        applyScaleX(-1);
    }

    /**
     * Mirror vertically by reversing the Y scale.
     */
    public void mirrorVertically() {
        applyScaleY(-1);
    }
    
    /**
     * Returns a String in CSV format containing all the shapes fields
     * @return 
     */
    public String toCSV() {
        return ";" + getStartX() + ";" + getStartY() + ";" + getWidth() + ";" + getHeight() + ";" + getFxShape().getFill() + ";" + getFxShape().getStroke() + ";" + getFxShape().getRotate() + ";null;null;null;null";
    }

}
