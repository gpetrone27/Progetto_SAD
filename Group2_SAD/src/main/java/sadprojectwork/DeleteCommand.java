package sadprojectwork;

import javafx.scene.layout.Pane;

/**
 * Implements the Command interface to delete a shape.
 * When executed, removes the selected shape from the drawing.
 * When undo, it restores the shape to the model.
 * 
 * @author noemi
 * 
 */
public class DeleteCommand implements Command {
    // reference to the model
    private Model model;
    // shape to delete
    private MyShape shapeToDelete;
    // canvas
    private Pane canvas;

    /**
    * Create a delete command.
    * 
    * @param shape: shape to delete
    * @param model: data model
    * @param canvas: graphic canvas
    */
    public DeleteCommand(Model model, MyShape shape, Pane canvas) {
        this.model = model;
        this.shapeToDelete = shape;
        this.canvas = canvas;
    }

    /**
    * Removes the shape from the canvas and from the model. 
    */
    @Override
    public void execute() {
        model.removeShape(shapeToDelete);
        canvas.getChildren().remove(shapeToDelete.getFxShape());
    }

    /**
    * Reinserts the shape into the canvas and into the model. 
    */
    @Override
    public void undo() {
        model.addShape(shapeToDelete);
        canvas.getChildren().add(shapeToDelete.getFxShape());
    }
}