package sadprojectwork.shapes;

import javafx.scene.shape.Ellipse;

/**
 * Represents a custom ellipse shape in the drawing application. Extends MyShape
 * and wraps a JavaFX Ellipse.
 */
public class MyEllipse extends MyShape {

    // Strongly typed reference to wrapped FX shape
    private Ellipse ellipse;

    /**
     * Creates a FX Ellipse
     *
     * @param startX
     * @param startY
     * @param width
     * @param height 
     * @param rotation 
     */
    public MyEllipse(double startX, double startY, double width, double height, double rotation) {
        
        // MyShape constructor
        super(startX, startY);

        // Adjusted starting points to handle negative dimensions
        double adjustedX = startX;
        double adjustedY = startY;

        if (width < 0) {
            adjustedX = startX + width; // Shift left
            width = -width;
        }
        if (height < 0) {
            adjustedY = startY + height; // Shift up
            height = -height;
        }

        double radiusX = width / 2.;
        double radiusY = height / 2.0;

        ellipse = new Ellipse(adjustedX + radiusX, adjustedY + radiusY, radiusX, radiusY);
        ellipse.setStrokeWidth(3); // Temporary: set border width to 3
        this.fxShape = ellipse;
        setRotation(rotation);
    }

    /**
     * Resizes the ellipse by adjusting its radii and center position based on
     * the specified deltas
     *
     * @param newWidth
     * @param newHeight
     */
    @Override
    public void resize(double newWidth, double newHeight) {
        
        if (newWidth == 0 || newHeight == 0) {
            return;
        }
        
        double centerX = startX + newWidth / 2.0;
        double centerY = startY + newHeight / 2.0;
        
        ellipse.setCenterX(centerX);
        ellipse.setCenterY(centerY);
        ellipse.setRadiusX(Math.abs(newWidth / 2.0));
        ellipse.setRadiusY(Math.abs(newHeight / 2.0));
    }

    @Override
    public double getStartX() {
        return ellipse.getCenterX() - ellipse.getRadiusX();
    }

    @Override
    public double getStartY() {
        return ellipse.getCenterY() - ellipse.getRadiusY();
    }

    /**
     * Creates a copy of the shape, useful for the copy and paste command
     *
     * @return cloned shape
     */
    @Override
    public MyShape cloneShape() {
        return new MyEllipse(startX, startY, getWidth(), getHeight(), ellipse.getRotate());
    }

    /**
     * Sets a new position for the center of the ellipse.
     *
     * @param x
     * @param y
     */
    @Override
    public void moveTo(double x, double y) {
        this.startX = x;
        this.startY = y;
        ellipse.setCenterX(x + ellipse.getRadiusX());
        ellipse.setCenterY(y + ellipse.getRadiusY());
    }

    /**
     * Returns the width of the ellipse
     *
     * @return getRadiusX() * 2
     */
    @Override
    public double getWidth() {
        return ellipse.getRadiusX() * 2;
    }

    /**
     * Returns the height of the ellipse
     *
     * @return getRadiusY()
     */
    @Override
    public double getHeight() {
        return ellipse.getRadiusY() * 2;
    }

    @Override
    public String toCSV() {
        return Shapes.ELLIPSE + super.toCSV();
    }

}
