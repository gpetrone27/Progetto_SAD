package sadprojectwork;

import javafx.scene.layout.Pane;

/**
 * Implements the Command interface to paste a shape.
 * When executed, pastes a cloned shape from the clipboard
 * to a new position and adds it into the canvas and into the model.
 * When undone, it removes the pasted shape.
 * 
 * @author noemi
 * 
 */
public class PasteCommand implements Command {
    // shape to paste
    private MyShape shapeToPaste;
    // reference to the model
    private Model model;
    // canvas
    private Pane canvas;
    
    /**
    * Create a paste command.
    * 
    * @param model: data model that contains the clipboard
    * @param canvas: graphic canvas
    * @param xPos: X coordinate where paste the shape
    * @param yPos: Y coordinate where paste the shape
    */
    public PasteCommand(Model model, Pane canvas, double xPos, double yPos) {
        
        this.model = model;
        this.canvas = canvas;

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
        canvas.getChildren().add(shapeToPaste.getFxShape());
    }

    /**
    * Undoes the paste, removing the shape from the model and from the canvas. 
    */
    @Override
    public void undo() {
        model.removeShape(shapeToPaste);
        canvas.getChildren().remove(shapeToPaste.getFxShape());  
    }
    
    /**
    * Returns the pasted shape, to immediatly select the shape in the UI. 
    * 
    * @return pasted shape
    */
    public MyShape getPastedShape() {
        return shapeToPaste;
    }

}
