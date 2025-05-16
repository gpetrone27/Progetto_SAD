/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author gianl
 */
public class MyLine extends MyShape{
    private Line line;

    public MyLine(double startX, double startY, double endX, double endY, Color borderColor) {
        super(startX, startY);
        this.line = new Line(startX, startY, endX, endY);
        line.setStroke(borderColor);
    }

    @Override
    public Shape getFxShape() {
        return line;
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        line.setEndX(startX + newWidth);
        line.setEndY(startY + newHeight);
    }

    @Override
    public MyShape cloneShape() {
        return new MyLine(startX, startY, line.getEndX(), line.getEndY(), (Color) line.getStroke());
    }
    
    @Override
    public void setPosition(double x, double y) {
        double dx = x - startX;
        double dy = y - startY;
        this.startX = x;
        this.startY = y;
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(line.getEndX() + dx);
        line.setEndY(line.getEndY() + dy);
    }
}
