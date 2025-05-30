/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;

import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import sadprojectwork.PaintModel;
import shapes.MyEllipse;
import shapes.MyRectangle;
import shapes.MyShape;

/**
 *
 *
 * 
 */
public class MirrorCommand implements Command {
    private final MyShape shape;
    private final boolean horizontal;  // true = mirror horizontally, false = vertically
    private Scale appliedScale = null;

    public MirrorCommand(MyShape shape, boolean horizontal) {
        this.shape = shape;
        this.horizontal = horizontal;
    }

    @Override
    public void execute() {
        Shape fxShape = shape.getFxShape();

        fxShape.getTransforms().removeIf(t -> t instanceof Scale && (
            ((Scale)t).getX() == -1 || ((Scale)t).getY() == -1));

        Bounds bounds = fxShape.getBoundsInParent();
        double pivotX = bounds.getMinX() + bounds.getWidth() / 2.0;
        double pivotY = bounds.getMinY() + bounds.getHeight() / 2.0;

        if (horizontal) {
            appliedScale = new Scale(-1, 1, pivotX, pivotY);
            shape.setMirroredHorizontally(true);
            shape.setMirroredVertically(false);
        } else {
            appliedScale = new Scale(1, -1, pivotX, pivotY);
            shape.setMirroredVertically(true);
            shape.setMirroredHorizontally(false);
        }

        fxShape.getTransforms().add(appliedScale);
    }

    @Override
    public void undo() {
        if (appliedScale == null) return;

        Shape fxShape = shape.getFxShape();
        fxShape.getTransforms().remove(appliedScale);

        if (horizontal) {
            shape.setMirroredHorizontally(false);
        } else {
            shape.setMirroredVertically(false);
        }

        appliedScale = null;
    }
}
