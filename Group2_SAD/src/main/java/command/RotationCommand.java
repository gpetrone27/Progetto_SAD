
package command;

import shapes.MyShape;

public class RotationCommand implements Command {

    private final MyShape shape;
    private final double oldRotation;
    private final double newRotation;
    
    public RotationCommand(MyShape shape, double oldRotation, double newRotation) {
        this.shape = shape;
        this.oldRotation = oldRotation;
        this.newRotation = newRotation;
    }
    
    @Override
    public void execute() {
        shape.setRotation(newRotation);
    }

    @Override
    public void undo() {
        shape.setRotation(oldRotation);
    }
    
}
