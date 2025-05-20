
package command;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import shapes.MyShape;

public class ChangeColorCommand implements Command {

    private final MyShape targetShape;
    
    private final Color newFillColor;
    private final Color newStrokeColor;
    
    private Color oldFillColor;
    private Color oldStrokeColor;
    
    public ChangeColorCommand(MyShape targetShape, Color newFillColor, Color newStrokeColor) {
        this.targetShape = targetShape;
        this.newFillColor = newFillColor;
        this.newStrokeColor = newStrokeColor;
        this.oldFillColor = (Color) targetShape.getFxShape().getFill();
        this.oldStrokeColor = (Color) targetShape.getFxShape().getStroke();
    }

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
