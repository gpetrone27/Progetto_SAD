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
 * Decorator that adds or overrides the fill color of a shape.
 * Uses the Decorator design pattern to apply a fill color
 * without modifying the underlying shape class.
 */

public class FillColorDecorator extends ShapeDecorator {
    private Color fillColor;

    public FillColorDecorator(MyShape decoratedShape, Color fillColor) {
        super(decoratedShape);
        this.fillColor = fillColor;
        Shape fxShape = getFxShape();
        fxShape.setFill(fillColor);
    }
    
    public MyShape cloneShape() {
        MyShape clone = decoratedShape.cloneShape();
        clone.startX = decoratedShape.getStartX();
        clone.startY = decoratedShape.getStartY();
        return new FillColorDecorator(clone, getFillColor());
    }
    
    public Color getFillColor() {
        return (Color) getFxShape().getFill();
    }
}