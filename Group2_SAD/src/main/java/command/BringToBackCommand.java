
package command;

import sadprojectwork.PaintModel;
import shapes.MyShape;

/**
 * Command to bring a shape to the back of the canvas and model.
 * When executed, the selected shape is moved to the top of the list of shapes in the model 
 * and in the drawing area, visually bringing it to the background.
 * When undone, the shape is returned to its original index in both the model and canvas, 
 * preserving the previous order.
 */
public class BringToBackCommand implements Command {
    
    private final PaintModel model;
    private final MyShape shapeToBring;
    private final int oldPosition;

    /**
    * Creates a bring to back command.
    * @param shapeToBring: shape to bring to back
    * @param model: data model that contains the clipboard
    */ 
    public BringToBackCommand(PaintModel model, MyShape shapeToBring) {
        this.model = model;
        this.shapeToBring = shapeToBring;
        this.oldPosition = model.getShapes().indexOf(shapeToBring);
    }
    
    /**
    * Moves the shape to the top of model and canvas lists, so as to bring the shape to the back.
    */
    @Override
    public void execute() {
        model.getShapes().remove(shapeToBring);
        model.getShapes().add(0, shapeToBring);
    }

    /**
     * Undoes the bring, reinserting the shape at its original position
     * in model and canvas.
     */
    @Override
    public void undo() {
        model.getShapes().remove(shapeToBring);
        model.getShapes().add(oldPosition, shapeToBring);
    }
}
