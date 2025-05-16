
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class MyLine extends MyShape {
    
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

    //create a copy of the shape, usefull for the copy and paste command
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
