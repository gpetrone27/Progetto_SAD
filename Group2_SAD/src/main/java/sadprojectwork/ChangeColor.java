
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class ChangeColor implements Command {

    private final MyShape targetShape;
    private final Color newFillColor;
    private Color oldFillColor;

    public ChangeColor(MyShape targetShape, Color newFillColor) {
        this.targetShape = targetShape;
        this.newFillColor = newFillColor;
    }

    @Override
    public void execute() {
        Shape fxShape = targetShape.getFxShape();
        oldFillColor = (Color) fxShape.getFill();  // Salva colore precedente
        fxShape.setFill(newFillColor);             // Applica nuovo colore
    }

    @Override
    public void undo() {
        targetShape.getFxShape().setFill(oldFillColor); // Ripristina colore precedente
    }
}
