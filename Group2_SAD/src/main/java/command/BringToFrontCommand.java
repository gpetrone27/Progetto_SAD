
package command;

import sadprojectwork.PaintModel;
import shapes.MyShape;

/**
 * Command to bring a shape to the front of the canvas and model.
 * When executed, the selected shape is moved to the end of the shape list in the model and canvas, 
 * effectively bringing it visually to the front.
 * When undone, the shape is restored to its original index in the model and canvas, 
 * preserving its previous order.
 */
public class BringToFrontCommand implements Command {
    
    private final PaintModel model;
    private final MyShape shapeToBring;
    private final int oldPosition;
    
    /**
    * Creates a bring to front command.
    * @param shapeToBring: shape to bring in front
    * @param model: data model that contains the clipboard
    */  
    public BringToFrontCommand(PaintModel model, MyShape shapeToBring) {
        this.model = model;
        this.shapeToBring = shapeToBring;
        this.oldPosition = model.getShapes().indexOf(shapeToBring);
    }

    /**
    * Moves the shape to the end of model and canvas lists, so as to bring the
    * shape in front.
    */
    @Override
    public void execute() {
        model.getShapes().remove(shapeToBring);
        model.getShapes().add(shapeToBring);
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
