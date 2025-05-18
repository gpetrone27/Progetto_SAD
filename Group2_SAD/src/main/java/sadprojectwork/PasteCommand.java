package sadprojectwork;

import javafx.scene.layout.Pane;

/**
 * Implements the Command interface to paste a shape.
 * When executed, pastes a cloned shape from the clipboard
 * to a new position and adds it into the canvas and into the model.
 * When undo, it removes the pasted shape.
 * 
 * @author noemi
 * 
 */
public class PasteCommand implements Command {
    
    private MyShape shapeToPaste;
    private Model model;
    private Pane canvas;
    
    public PasteCommand(Model model, Pane canvas, double xPos, double yPos) {
        
        this.model = model;
        this.canvas = canvas;

        MyShape clipboard = model.getClipboard();
        if (clipboard != null) {
            this.shapeToPaste = clipboard.cloneShape();
            this.shapeToPaste.moveTo(xPos, yPos);
        }
    }

    @Override
    public void execute() {
        model.addShape(shapeToPaste);
        canvas.getChildren().add(shapeToPaste.getFxShape());
    }

    @Override
    public void undo() {
        model.removeShape(shapeToPaste);
        canvas.getChildren().remove(shapeToPaste.getFxShape());  
    }
    
    public MyShape getPastedShape() {
        return shapeToPaste;
    }

}
