package sadprojectwork;

import javafx.scene.layout.Pane;

/**
* Implements the Command interface to cut a shape.
* When executed, removes the selected shape from the canvas and from the model,
* saving it into the clipboard.
* When undo, restores the shape to its original position.
* 
* @author noemi
*
*/
public class CutCommand implements Command {
    // shape to cut
    private MyShape shapeToCut;
    // reference to the model
    private Model model;
    // canvas
    private Pane canvas;
    
    /**
    * Create a cut command.
    * 
    * @param shape: shape to cut
    * @param model: data model that contains the clipboard
    * @param canvas: graphic canvas
    */
    public CutCommand(MyShape shape, Model model, Pane canvas){
        this.shapeToCut = shape;
        this.model = model;
        this.canvas = canvas;
    } 

    /**
    * Removes the selected shape from the drawing and saves it to the clipboard. 
    */
    @Override
    public void execute() {
        model.setClipboard(shapeToCut);
        model.removeShape(shapeToCut);
        canvas.getChildren().remove(shapeToCut.getFxShape());
    }

    /**
    * Undo the cut, emptying the clipboard. 
    */
    @Override
    public void undo() {
       model.addShape(shapeToCut);
       canvas.getChildren().add(shapeToCut.getFxShape());
       model.setClipboard(null);
    }

}
