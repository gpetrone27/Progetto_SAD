
package shapes;

import javafx.scene.shape.Ellipse;

/**
 * Represents a custom ellipse shape in the drawing application.
 * Extends MyShape and wraps a JavaFX Ellipse.
 */
public class MyEllipse extends MyShape {

    // Strongly typed reference to wrapped FX shape
    private Ellipse ellipse;

    /**
     * Creates an ellipse centered in (centerX, centerY) with the given radii
     * @param centerX
     * @param centerY
     * @param radiusX
     * @param radiusY 
     */
    public MyEllipse(double centerX, double centerY, double radiusX, double radiusY) {
        super(centerX, centerY);
        ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setStrokeWidth(3); // Temporary: set border width to 3
        this.fxShape = ellipse;
    }

    /**
     * Resizes the ellipse by adjusting its radii and center position based on
     * the specified deltas
     * @param newRadiusX
     * @param newRadiusY
     */
    @Override
    public void resize(double newRadiusX, double newRadiusY) {
        
        // Resizes the ellipse
        double centerX = startX + newRadiusX;
        double centerY = startY + newRadiusY;
        ellipse.setCenterX(centerX);
        ellipse.setCenterY(centerY);
        ellipse.setRadiusX(Math.abs(newRadiusX));
        ellipse.setRadiusY(Math.abs(newRadiusY));
    }

    /**
     * Creates a copy of the shape, useful for the copy and paste command
     * @return cloned shape
     */
    @Override
    public MyShape cloneShape() {
        return new MyEllipse(startX, startY, ellipse.getRadiusX(), ellipse.getRadiusY());
    }

    // Sets a new position for the center of the ellipse
    @Override
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
        ellipse.setCenterX(x);
        ellipse.setCenterY(y);
    }

    /**
     * Returns the radius X value
     * @return getRadiusX()
     */
    @Override
    public double getFirstDim() {
        return ellipse.getRadiusX();
    }

    /**
     * Returns the radius Y value
     * @return getRadiusY()
     */
    @Override
    public double getSecondDim() {
        return ellipse.getRadiusY();
    }

    @Override
    public String toCSV() {
        return Shapes.ELLIPSE + ";" + ellipse.getCenterX() + ";" + ellipse.getCenterY() + ";" + getFirstDim() + ";" + getSecondDim() + ";" + ellipse.getFill() + ";" + ellipse.getStroke();
    }
    
}
