
package sadprojectwork.command;

import sadprojectwork.shapes.MyShape;

/**
 * A command that resizes a shape to new dimensions,
 * and supports undoing the resize by restoring the old dimensions.
 * This command uses the Command design pattern.
 */
public class ResizeCommand implements Command {

    private final MyShape shape;
    
    // Dimensions before the resize
    private final double oldWidth;
    private final double oldHeight;
    
    // Dimensions after the resize
    private final double newWidth;
    private final double newHeight;

    public ResizeCommand(MyShape shape, double newWidth, double newHeight) {
        this.shape = shape;
        this.oldWidth = shape.getWidth();
        this.oldHeight = shape.getHeight();
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }


    /**
     * Resizes the shape to the new dimensions.
     */
    @Override
    public void execute() {
        shape.resize(newWidth, newHeight);
    }

    /**
     * Restores the shape's previous dimensions.
     */    
    @Override
    public void undo() {
        shape.resize(oldWidth, oldHeight);
    }
}
