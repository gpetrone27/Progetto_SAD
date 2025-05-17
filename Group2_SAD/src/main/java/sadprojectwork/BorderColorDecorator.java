/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sadprojectwork;

/**
 *
 * @author gianl
 */
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Decorator class that adds or overrides the border (stroke) color
 * of a shape without modifying the original shape implementation.
 * Uses the Decorator design pattern.
 */

public class BorderColorDecorator extends ShapeDecorator {

    private Color borderColor;

    public BorderColorDecorator(MyShape decoratedShape, Color borderColor) {
        super(decoratedShape);
        this.borderColor = borderColor;
        Shape fxShape = getFxShape();
        fxShape.setStroke(borderColor);
    }

    // forwards the resizeTo operation to the decorated shape
    @Override
    public void resizeTo(double endX, double endY) {
        decoratedShape.resizeTo(endX, endY);
    }
    
    public MyShape cloneShape() {
        MyShape clone = decoratedShape.cloneShape();
        clone.startX = decoratedShape.getStartX();
        clone.startY = decoratedShape.getStartY();
        return new BorderColorDecorator(clone, getBorderColor());
    }
    
    public Color getBorderColor() {
        return (Color) getFxShape().getStroke();
    }
    
}