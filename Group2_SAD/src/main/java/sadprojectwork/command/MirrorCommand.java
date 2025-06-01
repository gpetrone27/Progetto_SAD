
package sadprojectwork.command;

import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import sadprojectwork.shapes.MyShape;

/**
 * Implements the Command interface to mirror a shape.
 * When executed, mirrors the shape horizontally or vertically, depending on the constructor parameter. 
 * The mirroring flips the shape along the specified axis.
 * When undone, When undone, the command applies the same mirroring operation again, 
 * since mirroring is its own inverse (involutory operation), effectively restoring the shape to its original state.
 */
public class MirrorCommand implements Command {
    private final MyShape shapeToMirror;
    private final boolean horizontal;

    /**
    * Creates a mirror command.
    * @param shapeToMirror: shape to mirror
    * @param horizontal: true if the mirror must be horizontal; false instead.
    */
    public MirrorCommand(MyShape shapeToMirror, boolean horizontal) {
        this.shapeToMirror = shapeToMirror;
        this.horizontal = horizontal;
    }

    /**
     * Performs mirroring on the shape.
     * If horizontal is true, applies horizontal mirroring,
     * vertical mirroring instead.
     */
    @Override
    public void execute() {
        if (horizontal) {
            shapeToMirror.mirrorHorizontally();
        } else {
            shapeToMirror.mirrorVertically();
        }
    }

    /**
     * Undoes the applied mirroring, repeating the same operation, 
     * since the mirroring is involutional (its own inverse).
     */
    @Override
    public void undo() {
        if (horizontal) {
            shapeToMirror.mirrorHorizontally();
        } else {
            shapeToMirror.mirrorVertically();
        }
    }
}
