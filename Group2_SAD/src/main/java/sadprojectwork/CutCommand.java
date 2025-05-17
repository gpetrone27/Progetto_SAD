
package sadprojectwork;

import javafx.scene.layout.Pane;

public class CutCommand implements Command {
    
    private MyShape shapeToCut;
    private Model model;
    private Pane canvas;
    
    public CutCommand(MyShape shape, Model model, Pane canvas){
        this.shapeToCut = shape;
        this.model = model;
        this.canvas = canvas;
    } 

    @Override
    public void execute() {
        model.setClipboardShape(shapeToCut);
        model.removeShape(shapeToCut);
        canvas.getChildren().remove(shapeToCut.getFxShape());
    }

    @Override
    public void undo() {
       model.addShape(shapeToCut);
       canvas.getChildren().add(shapeToCut.getFxShape());
       model.setClipboardShape(null);
    }

}