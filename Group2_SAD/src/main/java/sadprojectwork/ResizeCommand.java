
package sadprojectwork;

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

    // Executes the resize by applying the new dimensions to the shape
    @Override
    public void execute() {
        shape.resize(newWidth, newHeight);
        System.out.println("OldWidth: " + oldWidth + "\nOldHeight: " + oldHeight + "\nNewWidth: " + newWidth + "\nNewHeight: " + newHeight);
    }

    // Undoes the resize by restoring the previous dimensions
    @Override
    public void undo() {
        shape.resize(oldWidth, oldHeight);
    }
}
