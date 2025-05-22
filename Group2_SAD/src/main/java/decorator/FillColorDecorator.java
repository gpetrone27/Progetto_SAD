
package decorator;

import shapes.MyShape;
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
        setFillColor();
    }
    
    @Override
    public MyShape cloneShape() {
        return new FillColorDecorator(decoratedShape.cloneShape(), (Color) decoratedShape.getFxShape().getFill());
    }
    
    private void setFillColor() {
        decoratedShape.getFxShape().setFill(fillColor);
    }
    
    public Color getFillColor() {
        return fillColor;
    }

}
