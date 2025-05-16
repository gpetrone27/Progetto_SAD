
package sadprojectwork;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

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

    //create a copy of the shape, usefull for the copy and paste command
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
