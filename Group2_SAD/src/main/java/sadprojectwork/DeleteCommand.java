
package sadprojectwork;

import javafx.scene.layout.Pane;

/**
 * Implementa l'interfaccia Command e rappresenta un'operazione che permette di
 * eliminare una figura presente nel disegno.
 * Quando eseguito, rimuove la figura specificata dal disegno.
 * Quando annullato (undo), ripristina la figura nel modello.
 * ------------------------------------------------------------------------------
 * Implements the Command interface and represents an operation that allows you to
 * delete a shape present in the drawing.
 * When executed, removes the selected shape from the drawing.
 * When canceled (undo), it restores the shape to the model.
 */
public class DeleteCommand implements Command {
    
    private Model model;
    private MyShape shapeToDelete;
    private Pane canvas;

    public DeleteCommand(Model model, MyShape shape, Pane canvas) {
        this.model = model;
        this.shapeToDelete = shape;
        this.canvas = canvas;
    }

    /* Esegue l'operazione di eliminazione della figura.
    -----------------------------------------------------
    Performs the figure elimination operation. */
    @Override
    public void execute() {
        model.removeShape(shapeToDelete);
        canvas.getChildren().remove(shapeToDelete.getFxShape());
    }

    /* Annulla l'operazione di eliminazione, ripristinando la figura.
    ----------------------------------------------------------------
    Undo the deletion operation, restoring the figure.*/
    @Override
    public void undo() {
        model.addShape(shapeToDelete);
        canvas.getChildren().add(shapeToDelete.getFxShape());
    }
}