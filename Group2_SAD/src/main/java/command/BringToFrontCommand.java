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
 * Command to bring a shape to the front of the canvas and model.
 * When executed, the selected shape is moved to the end of the shape list in the model and canvas, 
 * effectively bringing it visually to the front.
 * When undone, the shape is restored to its original index in the model and canvas, 
 * preserving its previous order.
 */
public class BringToFrontCommand implements Command {
    private final PaintModel model;
    private final Pane canvas;
    private final MyShape shapeToBring;
    private final int oldModel;
    private final int oldCanvas;
    private final Node fxShape;
    
    /**
    * Creates a bring to front command.
    * @param shapeToBring: shape to bring in front
    * @param model: data model that contains the clipboard
    * @param canvas: JavaFX pane representing drawing area
    */  
    public BringToFrontCommand(PaintModel model, MyShape shapeToBring, Pane canvas) {
        this.model = model;
        this.shapeToBring = shapeToBring;
        this.canvas = canvas;
        this.oldModel = model.getShapes().indexOf(shapeToBring);
        this.oldCanvas = canvas.getChildren().indexOf(shapeToBring.getFxShape());
        this.fxShape = shapeToBring.getFxShape();
    }

    /**
    * Moves the shape to the end of model and canvas lists, so as to bring the shape in front.
    */
    @Override
    public void execute() {
        model.getShapes().remove(shapeToBring);
        canvas.getChildren().remove(fxShape);

        model.getShapes().add(shapeToBring);
    }

    /**
     * Undoes the bring, reinserting the shape at its original position
     * in model and canvas.
     */
    @Override
    public void undo() {
        model.getShapes().remove(shapeToBring);
        canvas.getChildren().remove(fxShape);

        model.getShapes().add(oldModel, shapeToBring);
    }
}
