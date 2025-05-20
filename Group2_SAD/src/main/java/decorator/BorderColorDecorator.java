
package decorator;

import shapes.MyShape;
import javafx.scene.paint.Color;
import sadprojectwork.ShapeDecorator;

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
        setBorderColor();
    }
    
    @Override
    public MyShape cloneShape() {
        return new BorderColorDecorator(decoratedShape.cloneShape(), (Color) decoratedShape.getFxShape().getStroke());
    }
    
    private void setBorderColor() {
        decoratedShape.getFxShape().setStroke(borderColor);
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
}
