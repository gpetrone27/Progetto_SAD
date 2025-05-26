
package shapes;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * Represents a custom line shape in the drawing application.
 * Extends the abstract MyShape class and wraps a JavaFX Line.
 */
public class MyLine extends MyShape {

    // Strongly typed reference to wrapped FX shape
    private Line line;

    /**
     * Creates a FX Line
     * @param startX
     * @param startY
     * @param width
     * @param height 
     * @param rotation 
     */
    public MyLine(double startX, double startY, double width, double height, double rotation) {
        super(startX, startY);
        line = new Line(startX, startY, startX + width, startY + height);
        line.setStrokeWidth(3);
        this.fxShape = line;
        setRotation(rotation);
    }

    // Returns the JavaFX shape object to be added to the pane
    @Override
    public Shape getFxShape() {
        return line;
    }

    /**
     * Resizes the line with the given width and height values.
     * @param newWidth
     * @param newHeight
     */
    @Override
    public void resize(double newWidth, double newHeight) {
        line.setEndX(line.getStartX() + newWidth);
        line.setEndY(line.getStartY() + newHeight);
    }
    
    /**
     * Sets the end point of the line to the given coordinates.
     * @param endX
     * @param endY 
     */
    public void resizeTo(double endX, double endY) {
        line.setEndX(endX);
        line.setEndY(endY);
    }
    
    /**
     * Resizes the line with the new given length.
     * @param newLength 
     */
    public void resizeLength(double newLength) {
        
        // Calculate current length
        double dx = line.getEndX() - line.getStartX();
        double dy = line.getEndY() - line.getStartY();
        double currentLength = Math.sqrt(dx * dx + dy * dy);

        if (currentLength == 0) {
            // No direction; default to horizontal right
            line.setEndX(line.getStartX() + newLength);
            line.setEndY(line.getStartY());
            return;
        }

        // Normalize the direction vectors
        double ux = dx / currentLength;
        double uy = dy / currentLength;

        // Update end points of the line
        line.setEndX(line.getStartX() + ux * newLength);
        line.setEndY(line.getStartY() + uy * newLength);
    }

    /**
     * Creates a copy of the shape, useful for the copy and paste command
     * @return cloned shape
     */
    @Override
    public MyShape cloneShape() {
        return new MyLine(startX, startY, getWidth(), getHeight(), line.getRotate());
    }

    /**
     * Sets a new position for the start of the line, moving both start and
     * end points accordingly.
     * @param x
     * @param y
     */
    @Override
    public void moveTo(double x, double y) {
        double dx = x - startX;
        double dy = y - startY;
        this.startX = x;
        this.startY = y;
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(line.getEndX() + dx);
        line.setEndY(line.getEndY() + dy);
    }

    /**
     * Returns the width of the line.
     * @return width
     */
    @Override
    public double getWidth() {
        return line.getEndX() - line.getStartX();
    }

    /**
     * Returns the height of the line.
     * @return height
     */
    @Override
    public double getHeight() {
        return line.getEndY() - line.getStartY();
    }
    
    /**
     * Calculates the length of the line and returns it.
     * @return length
     */
    public double getLength() {
        double dx = line.getEndX() - startX;
        double dy = line.getEndY() - startY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toCSV() {
        return Shapes.LINE + ";" + startX + ";" + startY + ";" + getWidth() + ";" + getHeight() + ";" + fxShape.getStroke() + ";" + fxShape.getStroke() + ";" + fxShape.getRotate() + ";null;null;null;null";
    }
}
