/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sadprojectwork;

import javafx.scene.shape.Shape;

/**
 * Abstract Decorator class for MyShape objects. Allows dynamic addition of
 * responsibilities (e.g., visual styles) without modifying the original shape
 * implementation.
 */
public abstract class ShapeDecorator extends MyShape {

    // the shape being decorated (wrapped)
    protected MyShape decoratedShape;

    // initializes the decorator with the shape to be decorated
    public ShapeDecorator(MyShape decoratedShape) {
        super(decoratedShape.getStartX(), decoratedShape.getStartY());
        this.decoratedShape = decoratedShape;
    }

    // returns the actual JavaFX shape being rendered (delegated)
    @Override
    public Shape getFxShape() {
        return decoratedShape.getFxShape();
    }

    // delegates the resize logic to the original shape
    // this class doesn't handle resizing logic by itself
    @Override
    public void resize(double newWidth, double newHeight) {
        decoratedShape.resize(newWidth, newHeight);
    }

    // forwards the resizeTo call to the decorated shape
    @Override
    public void resizeTo(double endX, double endY) {
        decoratedShape.resizeTo(endX, endY);
    }

    @Override
    public double getStartX() {
        return decoratedShape.getStartX();
    }

    @Override
    public double getStartY() {
        return decoratedShape.getStartY();
    }

    // updates the position of the decorated shape
    @Override
    public void setPosition(double x, double y) {
        decoratedShape.setPosition(x, y);
    }

    // clones the underlying shape (note: decorators are not cloned here)
    @Override
    public MyShape cloneShape() {
        return decoratedShape.cloneShape();
    }

    // gets the width of the decorated shape
    // this simply delegates the call to the underlying shape
    @Override
    public double getWidth() {
        return decoratedShape.getWidth();
    }

    // gets the height of the decorated shape
    // this simply delegates the call to the underlying shape
    @Override
    public double getHeight() {
        return decoratedShape.getHeight();
    }
}
