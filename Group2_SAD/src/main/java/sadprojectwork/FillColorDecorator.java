
package sadprojectwork;

import javafx.scene.paint.Color;

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
    }
    
    @Override
    public MyShape cloneShape() {
        MyShape clone = decoratedShape.cloneShape();
        clone.startX = decoratedShape.getStartX();
        clone.startY = decoratedShape.getStartY();
        return new FillColorDecorator(clone, getFillColor());
    }
    
    public void setFillColor() {
        decoratedShape.getFxShape().setFill(fillColor);
    }
    
    public Color getFillColor() {
        return fillColor;
    }
}