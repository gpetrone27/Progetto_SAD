
package command;

import shapes.MyShape;

/**
 * A command that resizes a shape to new dimensions,
 * and supports undoing the resize by restoring the old dimensions.
 * This command uses the Command design pattern.
 */
public class ResizeCommand implements Command {

    private final MyShape shape;
    
    // Dimensions before the resize
    private final double oldFirstDim;
    private final double oldSecondDim;
    
    // Dimensions after the resize
    private final double newFirstDim;
    private final double newSecondDim;

    public ResizeCommand(MyShape shape, double newFirstDim, double newSecondDim) {
        this.shape = shape;
        this.oldFirstDim = shape.getWidth();
        this.oldSecondDim = shape.getHeight();
        this.newFirstDim = newFirstDim;
        this.newSecondDim = newSecondDim;
    }

    // Executes the resize by applying the new dimensions to the shape
    @Override
    public void execute() {
        shape.resize(newFirstDim, newSecondDim);
    }

    // Undoes the resize by restoring the previous dimensions
    @Override
    public void undo() {
        shape.resize(oldFirstDim, oldSecondDim);
    }
}
