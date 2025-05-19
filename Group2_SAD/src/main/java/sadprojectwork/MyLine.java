
package sadprojectwork;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * Represents a custom line shape in the drawing application. Extends the
 * abstract MyShape class and wraps a JavaFX Line.
 */
public class MyLine extends MyShape {

    private Line line;

    // Initializes the line with a start and end point
    public MyLine(double startX, double startY, double endX, double endY) {
        super(startX, startY);
        this.line = new Line(startX, startY, endX, endY);
        line.setStrokeWidth(3);
        line.setPickOnBounds(true);
    }

    // Returns the JavaFX shape object to be added to the pane
    @Override
    public Shape getFxShape() {
        return line;
    }

    // Resizes the line by setting new length
    @Override
    public void resize(double length, double dummy) {
        
        // Calculate current length
        double dx = line.getEndX() - startX;
        double dy = line.getEndY() - startY;
        double currentLength = Math.sqrt(dx * dx + dy * dy);

        if (currentLength == 0) {
            // No direction; default to horizontal right
            line.setEndX(startX + length);
            line.setEndY(startY);
            return;
        }

        // Normalize the direction vectors
        double ux = dx / currentLength;
        double uy = dy / currentLength;

        // Calculate new end points
        double newEndX = startX + ux * length;
        double newEndY = startY + uy * length;

        // Update end points of the line
        line.setEndX(newEndX);
        line.setEndY(newEndY);
    }
    
    // Resizes the line to the new absolute end coordinates
    public void resizeTo(double endX, double endY) {
        line.setEndX(endX);
        line.setEndY(endY);
    }

    // Creates a copy of the shape, useful for the copy and paste command
    @Override
    public MyShape cloneShape() {
        return new MyLine(startX, startY, line.getEndX(), line.getEndY());
    }

    // Sets a new position for the start of the line, moving both start and
    // end points accordingly
    @Override
    public void setPosition(double x, double y) {
        double dx = x - startX;
        double dy = y - startY;
        this.startX = x;
        this.startY = y;
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(line.getEndX() + dx);
        line.setEndY(line.getEndY() + dy);
    }

    // Returns the length of the line
    @Override
    public double getWidth() {
        double dx = line.getEndX() - startX;
        double dy = line.getEndY() - startY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Dummy: only used to maintain compatibility
    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public String toCSV() {
        return Shapes.LINE + ";" + startX + ";" + startY + ";" + line.getEndX() + ";" + line.getEndY() + ";" + line.getStroke() + ";" + line.getStroke();
    }
}
