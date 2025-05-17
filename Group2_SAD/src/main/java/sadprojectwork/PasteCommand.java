
package sadprojectwork;

import javafx.scene.layout.Pane;

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
