/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sadprojectwork;

/**
 *
 * @author noemi
 */
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
