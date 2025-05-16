package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ChangeColor implements Command {

    private final MyShape targetShape;
    private final Color newFillColor;
    private Color oldFillColor;
    
    private final Color newStrokeColor;
    private Color oldStrokeColor;

    public ChangeColor(MyShape targetShape, Color newFillColor, Color newStrokeColor) {
        this.targetShape = targetShape;
        this.newFillColor = newFillColor;
        this.newStrokeColor = newStrokeColor;
    }

    @Override
    public void execute() {
        Shape fxShape = targetShape.getFxShape();
        
        if (newFillColor != null) {
            oldFillColor = (Color) fxShape.getFill(); // Save old color 
            fxShape.setFill(newFillColor);
        }

        if (newStrokeColor != null) {
            oldStrokeColor = (Color) fxShape.getStroke(); // Save old stroke color 
            fxShape.setStroke(newStrokeColor);
        }
    }

    @Override
    public void undo() {
        Shape fxShape = targetShape.getFxShape();

        if (newFillColor != null) {
            oldFillColor = (Color) fxShape.getFill();
            fxShape.setFill(newFillColor);
        }

        if (newStrokeColor != null) {
            oldStrokeColor = (Color) fxShape.getStroke();
            fxShape.setStroke(newStrokeColor);
        }
    }
}
