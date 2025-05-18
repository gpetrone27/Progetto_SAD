package sadprojectwork;

import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

/**
 * Represents a custom ellipse shape in the drawing application. Extends MyShape
 * and wraps a JavaFX Ellipse.
 */
public class MyEllipsis extends MyShape {

    private Ellipse ellipse;

    // creates an ellipse centered at (centerX, centerY)
    // with the given horizontal and vertical radii
    public MyEllipsis(double centerX, double centerY, double radiusX, double radiusY) {
        super(centerX, centerY);
        ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
    }

    // returns the JavaFX shape object to be added to the pane
    @Override
    public Shape getFxShape() {
        return ellipse;
    }

    // resizes the ellipse by adjusting its radii and center position
    // based on the specified deltas
    @Override
    public void resize(double newRadiusX, double newRadiusY) {
        double centerX = startX + newRadiusX;
        double centerY = startY + newRadiusY;

        ellipse.setCenterX(centerX);
        ellipse.setCenterY(centerY);
        ellipse.setRadiusX(Math.abs(newRadiusX));
        ellipse.setRadiusY(Math.abs(newRadiusY));
    }

    // Creates a copy of the shape, useful for the copy and paste command
    @Override
    public MyShape cloneShape() {
        return new MyEllipsis(startX, startY, ellipse.getRadiusX(), ellipse.getRadiusY());
    }

    // sets a new position for the center of the ellipse
    @Override
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
        ellipse.setCenterX(x);
        ellipse.setCenterY(y);
    }

    // gets the width of the ellipse, defined as the horizontal radius
    @Override
    public double getWidth() {
        return ellipse.getRadiusX();
    }

    // gets the height of the ellipse, defined as the vertical radius
    @Override
    public double getHeight() {
        return ellipse.getRadiusY();
    }
}
