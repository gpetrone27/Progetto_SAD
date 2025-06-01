
package sadprojectwork.command;

import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.MyShape;

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
    
    @Override
    public void execute() {
        model.addShape(shapeToAdd);
    }

    @Override
    public void undo() {
        model.removeShape(shapeToAdd);
    }
    
}
