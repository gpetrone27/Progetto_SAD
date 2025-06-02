
package sadprojectwork.command;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import sadprojectwork.shapes.MyShape;

/**
 * A command that changes the fill and/or stroke color of a shape.
 */
public class ChangeColorCommand implements Command {

    private final MyShape targetShape;
    
    private final Color newFillColor;
    private final Color newStrokeColor;
    
    private Color oldFillColor;
    private Color oldStrokeColor;
    
    /**
    * Constructs a color change command for the given shape.
    * @param targetShape: the shape to modify
    * @param newFillColor: the new fill color
    * @param newStrokeColor: the new stroke color 
    */
    public ChangeColorCommand(MyShape targetShape, Color newFillColor, Color newStrokeColor) {
        this.targetShape = targetShape;
        this.newFillColor = newFillColor;
        this.newStrokeColor = newStrokeColor;
        this.oldFillColor = (Color) targetShape.getFxShape().getFill();
        this.oldStrokeColor = (Color) targetShape.getFxShape().getStroke();
    }
    
    /**
    * Applies the new fill and/or stroke color to the shape.
    */
    @Override
    public void execute() {
        
        Shape fxShape = targetShape.getFxShape();
        
        if (newFillColor != null) {
            fxShape.setFill(newFillColor);
        }
        if (newStrokeColor != null) {
            fxShape.setStroke(newStrokeColor);
        }
    }

    /**
    * Restores the shape's previous fill and/or stroke color.
    */
    @Override
    public void undo() {
        
        Shape fxShape = targetShape.getFxShape();

        if (newFillColor != null) {
            fxShape.setFill(oldFillColor);
        }
        if (newStrokeColor != null) {
            fxShape.setStroke(oldStrokeColor);
        }
    }
}
