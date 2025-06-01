
package sadprojectwork.command;

import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.MyShape;

/**
 * Implements the Command interface to delete a shape.
 * When executed, removes the selected shape from the drawing.
 * When undone, it restores the shape to the model.
 */
public class DeleteCommand implements Command {
    
    private PaintModel model;
    private MyShape shapeToDelete;

    /**
    * Creates a delete command.
    * @param model: data model
    * @param shapeToDelete: shape to delete
    */
    public DeleteCommand(PaintModel model, MyShape shapeToDelete) {
        this.model = model;
        this.shapeToDelete = shapeToDelete;
    }

    /**
    * Removes the shape from the canvas and from the model.
    */
    @Override
    public void execute() {
        model.removeShape(shapeToDelete);
    }

    /**
    * Undoes the delete, reinsering the shape into the model.
    */
    @Override
    public void undo() {
        model.addShape(shapeToDelete);
    }
}