
package sadprojectwork;

import javafx.scene.layout.Pane;

public class CopyCommand implements Command {
    
    private MyShape shapeToCopy;
    private Model model;
    private Pane canvas;
    
    public CopyCommand(MyShape shape, Model model, Pane canvas){
        this.shapeToCopy = shape;
        this.model = model;
        this.canvas = canvas;
    }

    @Override
    public void execute() {
        MyShape clonedCopy = shapeToCopy.cloneShape();
        model.setClipboard(clonedCopy);
    }

    @Override
    public void undo() {
        model.setClipboard(null);
    }

}
