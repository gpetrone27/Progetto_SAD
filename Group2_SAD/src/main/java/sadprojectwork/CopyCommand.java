package sadprojectwork;

import javafx.scene.layout.Pane;

/**
* Implements the Command interface to copy a figure.
* When executed, saves a copy of the selected figure to the clipboard,
* including “decorations” via clone.
* When undo, empties the clipboard.
* 
* @author noemi
*
*/
public class CopyCommand implements Command {
    
    // shape to copy
    private MyShape shapeToCopy;
    // reference to the model
    private Model model;
    // canvas
    private Pane canvas;
    
    /**
    * Create a copy command.
    * 
    * @param shape: shape to copy
    * @param model: data model that contains the clipboard
    * @param canvas: graphic canvas
    */
    public CopyCommand(MyShape shape, Model model, Pane canvas){
        this.shapeToCopy = shape;
        this.model = model;
        this.canvas = canvas;
    }

    /**
    * Saves a decorated copy of the current shape to the clipboard. 
    */
    @Override
    public void execute() {
        MyShape clonedCopy = shapeToCopy.cloneShape();
        model.setClipboard(clonedCopy);
    }

    /**
    * Undo the copy, emptying the clipboard. 
    */
    @Override
    public void undo() {
        model.setClipboard(null);
    }

}
