/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import javafx.scene.shape.Shape;
import shapes.MyShape;

/**
 *
 * @author gianl
 */
public class RotationCommand implements Command{

    private final MyShape shape;
    private final double oldRotation;
    private final double newRotation;
    
    public RotationCommand(MyShape shape, double oldRotation, double newRotation) {
        this.shape = shape;
        this.oldRotation = oldRotation;
        this.newRotation = newRotation;
    }
    
    @Override
    public void execute() {
        shape.setRotation(newRotation);
    }

    @Override
    public void undo() {
        shape.setRotation(oldRotation);
    }
    
}
