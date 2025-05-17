
package sadprojectwork;

public class MoveCommand implements Command {
    
    private MyShape shape;
    private double oldX, oldY;
    private double newX, newY;

    public MoveCommand(MyShape shape, double oldX, double oldY, double newX, double newY) {
        this.shape = shape;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }
    
    @Override
    public void execute() {
        shape.setPosition(newX, newY);
    }
    
    @Override
    public void undo() {
        shape.setPosition(oldX, oldY);
    }
    
}
