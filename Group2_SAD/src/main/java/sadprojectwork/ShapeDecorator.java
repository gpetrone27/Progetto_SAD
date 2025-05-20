
package sadprojectwork;

import shapes.MyShape;
import javafx.scene.shape.Shape;

/**
 * Abstract Decorator class for MyShape objects. Allows dynamic addition of
 * responsibilities (e.g., visual styles) without modifying the original shape
 * implementation.
 */
public abstract class ShapeDecorator extends MyShape {

    protected MyShape decoratedShape; // Wrapped shape to be decorated

    public ShapeDecorator(MyShape decoratedShape) {
        super(decoratedShape.getStartX(), decoratedShape.getStartY());
        this.decoratedShape = decoratedShape;
    }

    @Override
    public Shape getFxShape() {
        return decoratedShape.getFxShape();
    }

    @Override
    public double getStartX() {
        return decoratedShape.getStartX();
    }

    @Override
    public double getStartY() {
        return decoratedShape.getStartY();
    }
    
    @Override
    public double getFirstDim() {
        return decoratedShape.getFirstDim();
    }
    
    @Override
    public double getSecondDim() {
        return decoratedShape.getSecondDim();
    }
    
    @Override
    public void resize(double newFirstDim, double newSecondDim) {
        decoratedShape.resize(newFirstDim, newSecondDim);
    }

    @Override
    public void setPosition(double x, double y) {
        decoratedShape.setPosition(x, y);
    }

    @Override
    public MyShape cloneShape() {
        return decoratedShape.cloneShape();
    }
    
    @Override
    public String toCSV() {
        return decoratedShape.toCSV();
    }
    
}
