/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sadprojectwork;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 *
 * @author gianl
 */
public class MyRectangle extends MyShape {
        private Rectangle rect;

    public MyRectangle(double x, double y, double width, double height, Color borderColor, Color fillColor) {
        super(x, y);
        rect = new Rectangle(x, y, width, height);
        rect.setStroke(borderColor);
        rect.setFill(fillColor);
    }

    @Override
    public Shape getFxShape() {
        return rect;
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        rect.setWidth(newWidth);
        rect.setHeight(newHeight);
    }

    @Override
    public MyShape cloneShape() {
        return new MyRectangle(startX, startY, rect.getWidth(), rect.getHeight(),
                               (Color) rect.getStroke(), (Color) rect.getFill());
    }

    @Override
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
        rect.setX(x);
    }
}
