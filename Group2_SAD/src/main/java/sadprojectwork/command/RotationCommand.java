
package sadprojectwork.command;

import sadprojectwork.shapes.MyShape;

/**
 * Implements the Command interface to rotate a shape.
 * When executed, rotates a selected shape
 * When undone, the shape returns to its original rotation.
 */
public class RotationCommand implements Command {

    private final MyShape shape;
    private final double oldRotation;
    private final double newRotation;
    
    /**
     * Creates a rotation command.
     * @param shape: shape to rotate
     * @param oldRotation: original rotation of the shape
     * @param newRotation: new rotation after the execution command 
    */
    public RotationCommand(MyShape shape, double oldRotation, double newRotation) {
        this.shape = shape;
        this.oldRotation = oldRotation;
        this.newRotation = newRotation;
    }
    
    /**
     * Sets the shape's rotation to the new value.
    */
    @Override
    public void execute() {
        shape.setRotation(newRotation);
    }

    /**
     * Restores the shape's previous rotation.
    */    
    @Override
    public void undo() {
        shape.setRotation(oldRotation);
    }
    
}
