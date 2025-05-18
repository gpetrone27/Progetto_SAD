
package sadprojectwork;

/**
 * Implements the Command interface to paste a shape.
 * When executed, pastes a cloned shape from the clipboard
 * to a new position and adds it into the canvas and into the model.
 * When undone, it removes the pasted shape.
 */
public class PasteCommand implements Command {
    
    private Model model;
    private MyShape shapeToPaste;
    
    /**
    * Creates a paste command.
    * @param model: data model that contains the clipboard
    * @param xPos: X coordinate where paste the shape
    * @param yPos: Y coordinate where paste the shape
    */
    public PasteCommand(Model model, double xPos, double yPos) {
        
        this.model = model;

        MyShape clipboard = model.getClipboard();
        if (clipboard != null) {
            this.shapeToPaste = clipboard.cloneShape();
            this.shapeToPaste.moveTo(xPos, yPos);
        }
    }

    /**
    * Adds a decorated copy shape to the model and to the canvas.
    */
    @Override
    public void execute() {
        model.addShape(shapeToPaste);
    }

    /**
    * Undoes the paste, removing the shape from the model and from the canvas.
    */
    @Override
    public void undo() {
        model.removeShape(shapeToPaste);
    }
    
    /**
    * Returns the pasted shape, to enable selection in the UI.
    * @return pasted shape
    */
    public MyShape getPastedShape() {
        return shapeToPaste;
    }

}
