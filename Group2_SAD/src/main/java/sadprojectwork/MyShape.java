
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Abstract base class for all custom shapes in the drawing application.
 * Defines common properties and methods shared by all shapes.
 */

public abstract class MyShape {

    protected double startX;
    protected double startY;
    private boolean selected = false;

    public MyShape(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
    }

    // return the javaFX shape
    public abstract Shape getFxShape();

    // resizes the shape based on width and height or delta values
    // for shapes like rectangles and ellipses, this changes size.
    // for lines, this may be interpreted differently.
    public abstract void resize(double newWidth, double newHeight);

    // creates and returns a copy of the shape
    public abstract MyShape cloneShape();

    // set the new position of the shape on the pane 
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
    }

    // return the starting X coordinate of the shape
    public double getStartX() {
        return startX;
    }

    // return the starting X coordinate of the shape 
    public double getStartY() {
        return startY;
    }
    
    public boolean isSelected(){
        return selected;
    }
    
    public void setSelected(boolean selected){
        this.selected = selected;
        if (getFxShape() != null) {
            if (selected) {
                getFxShape().setStroke(Color.DODGERBLUE);
                getFxShape().setStrokeWidth(2);
            } else {
                getFxShape().setStroke(null);
            }
        }
    }
    
    // resizes the shape to reach a target coordinate.
    // converts the end coordinates to delta values and delegates to resize().
    // can be overridden by shapes (like lines) that interpret resizing differently.
    public void resizeTo(double endX, double endY) {
        double dx = endX - startX;
        double dy = endY - startY;
        resize(dx, dy); 
    }
    
    public void move(double updtX, double updtY) {
        setPosition(getStartX() + updtX, getStartY() + updtY);
    }

}
