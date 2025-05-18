/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sadprojectwork;

/**
 * A command that resizes a shape to new dimensions,
 * and supports undoing the resize by restoring the old dimensions.
 * 
 * This command uses the Command design pattern.
 * 
 */
public class ResizeCommand implements Command {

    private final MyShape shape;
    
    // dimensions before the resize
    private final double oldWidth;
    private final double oldHeight;
    
    // dimensions after the resize
    private final double newWidth;
    private final double newHeight;

    public ResizeCommand(MyShape shape, double newWidth, double newHeight) {
        this.shape = shape;
        this.oldWidth = shape.getWidth();
        this.oldHeight = shape.getHeight();
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    // executes the resize by applying the new dimensions to the shape
    @Override
    public void execute() {
        shape.resize(newWidth, newHeight);
    }

    // undoes the resize by restoring the previous dimensions
    @Override
    public void undo() {
        shape.resize(oldWidth, oldHeight);
    }
}
