
package sadprojectwork;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;

/**
 * Represents a custom rectangle shape in the drawing application.
 * Extends MyShape and wraps a JavaFX Rectangle.
 */
public class MyRectangle extends MyShape {
    private Rectangle rect;

    // creates a rectangle at position (x, y)with the specified width and height
    public MyRectangle(double x, double y, double width, double height) {
        super(x, y);
        rect = new Rectangle(x, y, width, height);
    }

    // returns the JavaFX shape object to be added to the pane
    @Override
    public Shape getFxShape() {
        return rect;
    }

    // resizes the rectangle based on the given width and height.
    // supports resizing in all directions by adjusting X and Y
    // when width or height is negative
    @Override
    public void resize(double newWidth, double newHeight) {
         if (newWidth < 0) {
            rect.setX(startX + newWidth);
            rect.setWidth(-newWidth);
        } else {
            rect.setX(startX);
            rect.setWidth(newWidth);
        }

        if (newHeight < 0) {
            rect.setY(startY + newHeight);
            rect.setHeight(-newHeight);
        } else {
            rect.setY(startY);
            rect.setHeight(newHeight);
        }
    }

    // Creates a copy of the shape, useful for the copy and paste command
    @Override
    public MyShape cloneShape() {
        return new MyRectangle(startX, startY, rect.getWidth(), rect.getHeight());
    }

    // sets a new top-left position for the rectangle.
    // only updates X and Y, width and height remain unchanged
    @Override
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
        rect.setX(x);
        rect.setY(y);
    }
}
