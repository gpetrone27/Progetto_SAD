
package sadprojectwork;

import javafx.scene.shape.Rectangle;

/**
 * Represents a custom rectangle shape in the drawing application. Extends
 * MyShape and wraps a JavaFX Rectangle.
 */
public class MyRectangle extends MyShape {

    // Strongly typed reference to wrapped FX shape
    private Rectangle rectangle;
    
    /**
     * Creates an FX Rectangle
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public MyRectangle(double x, double y, double width, double height) {
        super(x, y);
        rectangle = new Rectangle(x, y, width, height);
        rectangle.setStrokeWidth(3); // Temporary: set border width to 3
        this.fxShape = rectangle;
    }

    /**
     * Resizes the rectangle based on the given width and height.
     * Supports resizing in all directions by adjusting X and Y when width or height is negative
     */
    @Override
    public void resize(double... dimensions) {
        
        // Checks if two parameters were given
        if (dimensions.length != 2) {
            throw new IllegalArgumentException("Rectangle needs width and height");
        }
        
        // Checks if width is negative
        if (dimensions[0] < 0) {
            rectangle.setX(startX + dimensions[0]);
            rectangle.setWidth(-dimensions[0]);
        }
        else {
            rectangle.setX(startX);
            rectangle.setWidth(dimensions[0]);
        }
        
        // Checks if height is negative
        if (dimensions[1] < 0) {
            rectangle.setY(startY + dimensions[1]);
            rectangle.setHeight(-dimensions[1]);
        }
        else {
            rectangle.setY(startY);
            rectangle.setHeight(dimensions[1]);
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

    // Sets a new top-left position for the rectangle.
    // Only updates X and Y, width and height remain unchanged
    @Override
    public void setPosition(double x, double y) {
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
        return Shapes.RECTANGLE + ";" + rectangle.getX() + ";" + rectangle.getY() + ";" + rectangle.getWidth() + ";" + rectangle.getHeight() + ";" + rectangle.getFill() + ";" + rectangle.getStroke();
    }

}
