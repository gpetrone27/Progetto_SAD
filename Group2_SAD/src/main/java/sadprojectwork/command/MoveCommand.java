
package sadprojectwork.command;

import sadprojectwork.shapes.MyShape;

/**
* Implements the Command interface to move a shape.
* When executed, moves the shape to new coordinates.
* When undone, returns it to its original position.
*/
public class MoveCommand implements Command {
    
    private MyShape shapeToMove;
    private double oldX, oldY; // Original coordinates
    private double newX, newY; // New coordinates

    /**
    * Create a move command.
    * @param shape: shape to move
    * @param oldX: original X coordinate
    * @param oldY: original Y coordinate
    * @param newX: new X coordinate
    * @param newY: new Y coordinate
    */
    public MoveCommand(MyShape shape, double oldX, double oldY, double newX, double newY) {
        this.shapeToMove = shape;
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }
    
    /**
    * Moves the shape, setting the shape position to the new coordinates. 
    */
    @Override
    public void execute() {
        shapeToMove.moveTo(newX, newY);
    }
    
    /**
    * Undoes the movement, restoring the shape position to the original coordinates. 
    */
    @Override
    public void undo() {
        shapeToMove.moveTo(oldX, oldY);
    }
    
}
