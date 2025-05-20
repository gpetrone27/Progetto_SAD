
package command;

import sadprojectwork.Model;
import shapes.MyShape;

public class AddShapeCommand implements Command {

    private Model model;
    private MyShape shapeToAdd;
    
    /**
    * Creates an add command.
    * @param model: data model
    * @param shapeToAdd: shape to add
    */
    public AddShapeCommand(Model model, MyShape shapeToAdd) {
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
