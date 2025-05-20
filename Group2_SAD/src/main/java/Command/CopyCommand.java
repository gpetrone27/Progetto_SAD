
package command;

import sadprojectwork.Model;
import shapes.MyShape;

/**
* Implements the Command interface to copy a shape.
* When executed, saves a copy of the selected shape to the clipboard,
* including “decorations” via clone.
* When undone, empties the clipboard.
*/
public class CopyCommand implements Command {
    
    private Model model;
    private MyShape shapeToCopy;
    
    /**
    * Creates a copy command.
    * @param shapeToCopy: shape to copy
    * @param model: data model that contains the clipboard
    */
    public CopyCommand(Model model, MyShape shapeToCopy) {
        this.model = model;
        this.shapeToCopy = shapeToCopy;
    }

    /**
    * Saves a decorated copy of the current shape into the clipboard. 
    */
    @Override
    public void execute() {
        model.setClipboard(shapeToCopy.cloneShape());
    }

    /**
    * Undoes the copy, emptying the clipboard. 
    */
    @Override
    public void undo() {
        model.setClipboard(null);
    }

}
