
package sadprojectwork;

import javafx.scene.paint.Color;

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
    }
    
    @Override
    public MyShape cloneShape() {
        MyShape clone = decoratedShape.cloneShape();
        clone.startX = decoratedShape.getStartX();
        clone.startY = decoratedShape.getStartY();
        return new BorderColorDecorator(clone, getBorderColor());
    }
    
    public void setBorderColor() {
        decoratedShape.getFxShape().setStroke(borderColor);
    }
    
    public Color getBorderColor() {
        return borderColor;
    }
    
}