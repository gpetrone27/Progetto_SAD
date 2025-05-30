
package shapes;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Shape;

public class MyCompositeShape extends MyShape {
    
    protected List<MyShape> children;

    public MyCompositeShape(double startX, double startY) {
        super(startX, startY);
        children  = new ArrayList<>();
        this.fxShape = null; // there is no fxShape
    }
    
    public void addShape(MyShape shape) {
        children.add(shape);
        calculateBounds();
    }
        
    public void removeShape(MyShape shape) {
        children.remove(shape);
        calculateBounds();
    }
    
    public List<MyShape> getShapes() {
        return children; 
    }
    
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
    
    public void clear() {
        children.clear();
    }

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

    @Override
    public MyShape cloneShape() {
        MyCompositeShape clone = new MyCompositeShape(startX, startY);
        for (MyShape shape : children) {
            clone.addShape(shape.cloneShape());
        }
        clone.moveTo(startX, startY);
        return clone;
    }
    
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

    @Override
    public double getStartX() {
        return startX;
    }

    @Override
    public double getStartY() {
        return startY;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    
    @Override
    public void setRotation(double rotation) {
        for (MyShape shape : children) {
            shape.setRotation(rotation);
        }
    }
    
    @Override
    public double getRotation() {
        return children.get(0).getRotation();
    }
    
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
