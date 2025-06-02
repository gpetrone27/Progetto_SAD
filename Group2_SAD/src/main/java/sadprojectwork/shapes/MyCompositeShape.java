
package sadprojectwork.shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Shape;

/**
 * A composite shape that groups multiple MyShape instances into a single logical unit.
 */
public class MyCompositeShape extends MyShape {
    
    // List of children of the composite shape
    protected List<MyShape> children;
    
    // Rotation of the composite shape
    double rotation;
    
    public MyCompositeShape(double startX, double startY) {
        super(startX, startY);
        children  = new ArrayList<>();
        this.fxShape = null; // there is no fxShape
    }
      
    private List<OriginalShapeInfo> basePositions = new ArrayList<>();
    private boolean initialized = false;
    
    private static class OriginalShapeInfo {
        
        MyShape shape;
        double startX;
        double startY;
        
        OriginalShapeInfo(MyShape shape, double startX, double startY) {
            this.shape = shape;
            this.startX = startX;
            this.startY = startY;
        }
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
        
        basePositions.clear();
        initialized = false;
        
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

        if (newWidth == 0 || newHeight == 0) {
            return;
        }
        
        double rScaleX = newWidth / width;
        double rScaleY = newHeight / height;

        for (MyShape child : children) {
            // Child offset with respect to the composite fixed point
            double offsetX = child.getStartX() - startX;
            double offsetY = child.getStartY() - startY;

            // Nuova posizione proporzionale
            double newChildX = startX + offsetX * rScaleX;
            double newChildY = startY + offsetY * rScaleY;

            // New proportional position
            double newChildWidth = child.getWidth() * rScaleX;
            double newChildHeight = child.getHeight() * rScaleY;

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
     * Rotates the whole shape by computing the centroid vector rotation.
     * @param rotation the rotation angle in degrees
     */
    @Override
    public void setRotation(double rotation) {
        
        this.rotation = rotation;

        if (!initialized) {
            basePositions.clear();
            for (MyShape shape : children) {
                basePositions.add(new OriginalShapeInfo(shape, shape.getStartX(), shape.getStartY()));
            }
            initialized = true;
        }
        
        // Center of the bounding box
        double centerX = startX + width / 2;
        double centerY = startY + height / 2;

        double radians = Math.toRadians(rotation);

        for (OriginalShapeInfo base : basePositions) {
            
            // Center
            double shapeCenterX = base.startX + base.shape.getWidth() / 2;
            double shapeCenterY = base.startY + base.shape.getHeight() / 2;

            // Center vector
            double dx = shapeCenterX - centerX;
            double dy = shapeCenterY - centerY;

            // Center vector rotation
            double rotatedX = dx * Math.cos(radians) - dy * Math.sin(radians);
            double rotatedY = dx * Math.sin(radians) + dy * Math.cos(radians);

            // New center after rotation
            double newCenterX = centerX + rotatedX;
            double newCenterY = centerY + rotatedY;

            // New starting position
            double newStartX = newCenterX - base.shape.getWidth() / 2;
            double newStartY = newCenterY - base.shape.getHeight() / 2;

            base.shape.moveTo(newStartX, newStartY);
            base.shape.setRotation(rotation);
            
        }
    }
    
    /**
     * Returns the rotation of the composite shape.
     * @return the rotation angle in degrees
     */
    @Override
    public double getRotation() {
        return rotation;
    }
    
    /**
     * Mirrors the composite shape horizontally with respect to its vertical center axis
     * This operation reflects each child shape across the vertical axis that passes
     * through the center of the composite's bounding box, and then applies a horizontal
     * flip to maintain visual consistency (by inverting its internal X scale).
     */
    @Override
    public void mirrorHorizontally() {
        
        double centerX = startX + width / 2;

        for (MyShape shape : children) {

            double shapeCenterX = shape.getStartX() + shape.getWidth() / 2;
            double dx = shapeCenterX - centerX;

            // New reflected position with respect to the centre of the composite
            double newShapeCenterX = centerX - dx;
            double newStartX = newShapeCenterX - shape.getWidth() / 2;

            shape.moveTo(newStartX, shape.getStartY());
            shape.mirrorHorizontally(); // applies the internal flip
            
        }

        calculateBounds(); // recalculates bounds
    }
    
    /**
     * Mirrors the composite shape vertically with respect to its horizontal center axis
     * This operation reflects each child shape across the horizontal axis that passes
     * through the center of the composite's bounding box, and then applies a vertical
     * flip to maintain visual consistency (by inverting its internal Y scale).
     */
    @Override
    public void mirrorVertically() {
        
        double centerY = startY + height / 2;

        for (MyShape shape : children) {
            
            double shapeCenterY = shape.getStartY() + shape.getHeight() / 2;
            double dy = shapeCenterY - centerY;

            // New reflected position with respect to the centre of the composite            
            double newShapeCenterY = centerY - dy;
            double newStartY = newShapeCenterY - shape.getHeight() / 2;

            shape.moveTo(shape.getStartX(), newStartY);
            shape.mirrorVertically(); // applies the internal flip
            
        }

        calculateBounds(); // recalculates bounds
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
    
}
