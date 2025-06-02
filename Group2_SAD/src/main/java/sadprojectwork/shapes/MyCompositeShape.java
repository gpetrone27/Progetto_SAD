
package sadprojectwork.shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Shape;

/**
 * A composite shape that groups multiple MyShape instances into a single logical unit.
 */
public class MyCompositeShape extends MyShape {
    
    protected List<MyShape> children;

    public MyCompositeShape(double startX, double startY) {
        super(startX, startY);
        children  = new ArrayList<>();
        this.fxShape = null; // there is no fxShape
    }
      
    /**
     * Adds a shape to the composite and updates the bounding box
     * @param shape
     */
    public void addShape(MyShape shape) {
        children.add(shape);
        calculateBounds();
    }
 
    /**
     * Removes a shape to the composite and updates the bounding box
     * @param shape
     */
    public void removeShape(MyShape shape) {
        children.remove(shape);
        calculateBounds();
    }
    
    /**
     * Returns the list of child shapes in this composite
     * @return a list of children
     */
    public List<MyShape> getShapes() {
        return children; 
    }
  
    
    /**
     * Computes the bounding box of the composite shape based on its children.
     */
    private void calculateBounds() {
        if (children.isEmpty()) {
            startX = 0;
            startY = 0;
            width = 0;
            height = 0;
            return;
        }

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (MyShape shape : children) {
            minX = Math.min(minX, shape.getStartX());
            minY = Math.min(minY, shape.getStartY());
            maxX = Math.max(maxX, shape.getStartX() + shape.getWidth());
            maxY = Math.max(maxY, shape.getStartY() + shape.getHeight());
        }

        startX = minX;
        startY = minY;
        width = maxX - minX;
        height = maxY - minY;
    }
    
    public boolean isEmpty() {
        return children.isEmpty();
    }
    
    /**
     * Clears all child shapes from this composite.
     */
    public void clear() {
        children.clear();
    }

    /**
     * Resizes the composite shape by proportionally resizing and repositioning all children
     * @param newWidth the new width of the composite
     * @param newHeight the new height of the composite
     */
    @Override
    public void resize(double newWidth, double newHeight) {
        // Avoids division by zero
        if (width == 0 || height == 0) {
            // If current sizes are zero, do not resize
            return;
        }

        double scaleX = newWidth / width;
        double scaleY = newHeight / height;

        for (MyShape child : children) {
            // Child offset with respect to the composite fixed point
            double offsetX = child.getStartX() - startX;
            double offsetY = child.getStartY() - startY;

            // Nuova posizione proporzionale
            double newChildX = startX + offsetX * scaleX;
            double newChildY = startY + offsetY * scaleY;

            // New proportional position
            double newChildWidth = child.getWidth() * scaleX;
            double newChildHeight = child.getHeight() * scaleY;

            // Resize and move the child
            child.resize(newChildWidth, newChildHeight);
            child.moveTo(newChildX, newChildY);
        }

        calculateBounds();
    }

    @Override
    public Shape getFxShape() {
        throw new UnsupportedOperationException("Composite shape has no single JavaFX Shape. Use getChildren() to access individual shapes.");
    }
   
    /**
     * Creates a deep copy of the composite shape, including clones of all child shapes
     * @return the cloned composite shape
     */
    @Override
    public MyShape cloneShape() {
        MyCompositeShape clone = new MyCompositeShape(startX, startY);
        for (MyShape shape : children) {
            clone.addShape(shape.cloneShape());
        }
        clone.moveTo(startX, startY);
        return clone;
    }
    
    /**
     * Moves the composite shape to a new position by moving all child shapes accordingly
     * @param x the new X position
     * @param y the new Y position
     */    
    @Override
    public void moveTo(double x, double y) {
        double dx = x - startX;
        double dy = y - startY;
        for (MyShape shape : children) {
            shape.moveOf(dx, dy);
        }
        this.startX = x;
        this.startY = y;
    }

    /**
     * Returns the X coordinate of the top-left corner of the composite
     * @return the X position
     */
    @Override
    public double getStartX() {
        return startX;
    }

    /**
     * Returns the Y coordinate of the top-left corner of the composite
     * @return the Y position
     */
    @Override
    public double getStartY() {
        return startY;
    }

    /**
     * Returns the width of the composite bounding box
     * @return the width
     */
    @Override
    public double getWidth() {
        return width;
    }
    
    /**
     * Returns the height of the composite bounding box
     * @return the height
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * Sets the rotation for all child shapes.
     * @param rotation the rotation angle in degrees
     */
    @Override
    public void setRotation(double rotation) {
        for (MyShape shape : children) {
            shape.setRotation(rotation);
        }
    }
  
    /**
     * Returns the rotation of the first child shape
     * All children share the same rotation
     * @return the rotation angle in degrees
     */
    @Override
    public double getRotation() {
        return children.get(0).getRotation();
    }
    
    /**
     * Exports the composite shape as a CSV string by concatenating child shapes
     * @return the CSV representation of all child shapes
     */    
    @Override
    public String toCSV() {
        StringBuffer buffer = new StringBuffer();
        for (MyShape shape : children) {
            buffer.append(shape.toCSV()).append("\n");
        }
        
        if (buffer.length() > 0) {
            buffer.setLength(buffer.length() - 1); // removes the last '\n'
        }
        return buffer.toString();
    }
    
    @Override
    public void mirrorHorizontally() {
        double centerX = startX + width / 2;

        for (MyShape shape : children) {
            double shapeCenterX = shape.getStartX() + shape.getWidth() / 2;
            double distanceFromCenter = shapeCenterX - centerX;

            // Nuova posizione riflessa rispetto al centro del composite
            double newShapeCenterX = centerX - distanceFromCenter;
            double newStartX = newShapeCenterX - shape.getWidth() / 2;

            shape.moveTo(newStartX, shape.getStartY());
            shape.mirrorHorizontally(); // applica il flip interno
        }

        calculateBounds(); // aggiorna le dimensioni
    }

    @Override
    public void mirrorVertically() {
        double centerY = startY + height / 2;

        for (MyShape shape : children) {
            double shapeCenterY = shape.getStartY() + shape.getHeight() / 2;
            double distanceFromCenter = shapeCenterY - centerY;

            // Nuova posizione riflessa rispetto al centro del composite
            double newShapeCenterY = centerY - distanceFromCenter;
            double newStartY = newShapeCenterY - shape.getHeight() / 2;

            shape.moveTo(shape.getStartX(), newStartY);
            shape.mirrorVertically(); // applica il flip interno
        }

        calculateBounds(); // aggiorna le dimensioni
    }
    
}
