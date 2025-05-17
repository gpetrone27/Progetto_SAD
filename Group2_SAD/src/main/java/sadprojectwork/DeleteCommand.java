/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sadprojectwork;

import javafx.scene.layout.Pane;

/**
 *
 * Implementa l'interfaccia Command e rappresenta un'operazione che permette di
 * eliminare una figura presente nel disegno.
 * Quando eseguito, rimuove la figura specificata dal disegno.
 * Quando annullato (undo), ripristina la figura nel modello.
 * ------------------------------------------------------------------------------
 * Implements the Command interface and represents an operation that allows you to
 * delete a figure present in the drawing.
 * When executed, removes the specified figure from the drawing.
 * When canceled (undo), it restores the figure to the model.
 * 
 * @author noemi
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
        model.setSelectedShape(null);
    }

    /* Annulla l'operazione di eliminazione, ripristinando la figura.
    ----------------------------------------------------------------
    Undo the deletion operation, restoring the figure.*/
    @Override
    public void undo() {
        model.addShape(shapeToDelete);
        canvas.getChildren().add(shapeToDelete.getFxShape());
        model.setSelectedShape(shapeToDelete);
    }
}