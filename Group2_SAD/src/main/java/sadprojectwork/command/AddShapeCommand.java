
package sadprojectwork.command;

import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.MyShape;

/**
 * A command that adds a shape to PaintModel
 * Supports undo functionality to remove the shape.
 */
public class AddShapeCommand implements Command {

    private PaintModel model;
    private MyShape shapeToAdd;
    
    /**
    * Creates an add command.
    * @param model: data model
    * @param shapeToAdd: shape to add
    */
    public AddShapeCommand(PaintModel model, MyShape shapeToAdd) {
        this.model = model;
        this.shapeToAdd = shapeToAdd;
    }

    /**
    * Adds the shape to the model.
    */    
    @Override
    public void execute() {
        model.addShape(shapeToAdd);
    }

    /**
    * Removes the shape from the model.
    */
    @Override
    public void undo() {
        model.removeShape(shapeToAdd);
    }
    
}
