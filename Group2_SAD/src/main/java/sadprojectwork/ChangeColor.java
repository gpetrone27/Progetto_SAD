/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 *
 * @author gianl
 */
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
        oldFillColor = (Color) fxShape.getFill();  // salva colore precedente
        fxShape.setFill(newFillColor);             // applica nuovo colore
    }

    @Override
    public void undo() {
        targetShape.getFxShape().setFill(oldFillColor); // ripristina colore precedente
    }
}
