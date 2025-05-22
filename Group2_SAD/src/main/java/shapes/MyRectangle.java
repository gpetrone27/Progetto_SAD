
package shapes;

import javafx.scene.shape.Rectangle;

/**
 * Represents a custom rectangle shape in the drawing application.
 * Extends MyShape and wraps a JavaFX Rectangle.
 */
public class MyRectangle extends MyShape {

    // Strongly typed reference to wrapped FX shape
    private Rectangle rectangle;
    
    /**
     * Creates an FX Rectangle
     * @param startX
     * @param startY
     * @param width
     * @param height 
     */
    public MyRectangle(double startX, double startY, double width, double height) {
        super(startX, startY);
        rectangle = new Rectangle(startX, startY, width, height);
        rectangle.setStrokeWidth(3); // Temporary: set border width to 3
        this.fxShape = rectangle;
    }

    /**
     * Resizes the rectangle based on the given width and height.
     * Supports resizing in all directions by adjusting X and Y when width or height is negative.
     * @param newWidth
     * @param newHeight
     */
    @Override
    public void resize(double newWidth, double newHeight) {
        
        // Checks if width is negative
        if (newWidth < 0) {
            rectangle.setX(startX + newWidth);
            rectangle.setWidth(-newWidth);
        }
        else {
            rectangle.setX(startX);
            rectangle.setWidth(newWidth);
        }
        
        // Checks if height is negative
        if (newHeight < 0) {
            rectangle.setY(startY + newHeight);
            rectangle.setHeight(-newHeight);
        }
        else {
            rectangle.setY(startY);
            rectangle.setHeight(newHeight);
        }
    }

    /**
     * Creates a copy of the shape, useful for the copy and paste command
     * @return cloned shape
     */
    @Override
    public MyShape cloneShape() {
        return new MyRectangle(startX, startY, rectangle.getWidth(), rectangle.getHeight());
    }

    /**
     * Sets a new top-left position for the rectangle.
     * Only updates X and Y, width and height remain unchanged.
     * @param x
     * @param y
     */
    @Override
    public void moveTo(double x, double y) {
        this.startX = x;
        this.startY = y;
        rectangle.setX(x);
        rectangle.setY(y);
    }

    /**
     * Returns the width of the rectangle
     * @return getWidth()
     */
    @Override
    public double getWidth() {
        return rectangle.getWidth();
    }

    /**
     * Returns the height of the rectangle
     * @return getHeight()
     */
    @Override
    public double getHeight() {
        return rectangle.getHeight();
    }

    @Override
    public String toCSV() {
        return Shapes.RECTANGLE + super.toCSV();
    }

}
