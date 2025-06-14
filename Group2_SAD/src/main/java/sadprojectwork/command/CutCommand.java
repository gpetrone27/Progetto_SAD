
package sadprojectwork.command;

import sadprojectwork.mvc.PaintModel;
import sadprojectwork.shapes.MyShape;

/**
 * Implements the Command interface to cut a shape.
 * When executed, removes the selected shape from the canvas and from the model,
 * saving it into the clipboard.
 * When undone, restores the shape to its original position.
 */
public class CutCommand implements Command {
    
    private PaintModel model;
    private MyShape shapeToCut;
    
    /**
     * Creates a cut command.
     * @param model: data model that contains the clipboard
     * @param shapeToCut: shape to cut
     */
    public CutCommand(PaintModel model, MyShape shapeToCut){
        this.model = model;
        this.shapeToCut = shapeToCut;
    } 

    /**
     * Removes the selected shape from the model and saves it to the clipboard. 
     */
    @Override
    public void execute() {
        model.setClipboard(shapeToCut.cloneShape());
        model.removeShape(shapeToCut);
    }

    /**
     * Undoes the cut, emptying the clipboard. 
     */
    @Override
    public void undo() {
       model.addShape(shapeToCut);
       model.setClipboard(null);
    }

}
