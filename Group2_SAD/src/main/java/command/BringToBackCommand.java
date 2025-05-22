/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package command;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
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
    private final Pane canvas;
    private final MyShape shapeToBring;
    private final int oldModel;
    private final int oldCanvas;
    private final Node fxShape;

    /**
    * Creates a bring to back command.
    * @param shapeToBring: shape to bring to back
    * @param model: data model that contains the clipboard
    * @param canvas: JavaFX pane representing drawing area
    */ 
    public BringToBackCommand(PaintModel model, Pane canvas, MyShape shapeToBring) {
        this.model = model;
        this.canvas = canvas;
        this.shapeToBring = shapeToBring;
        this.oldModel = model.getShapes().indexOf(shapeToBring);
        this.oldCanvas = canvas.getChildren().indexOf(shapeToBring.getFxShape());
        this.fxShape = shapeToBring.getFxShape();
    }
    
    /**
    * Moves the shape to the top of model and canvas lists, so as to bring the shape to the back.
    */
    @Override
    public void execute() {
        model.getShapes().remove(shapeToBring);
        canvas.getChildren().remove(fxShape);

        model.getShapes().add(0, shapeToBring);
    }

    /**
     * Undoes the bring, reinserting the shape at its original position
     * in model and canvas.
     */
    @Override
    public void undo() {
        model.getShapes().remove(shapeToBring);
        canvas.getChildren().remove(shapeToBring.getFxShape());

        model.getShapes().add(oldModel, shapeToBring);
    }
}
