
package sadprojectwork;

import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class MyRectangle extends MyShape {
    private Model model;
    private Rectangle rect;

    public MyRectangle(double x, double y, double width, double height, Color borderColor, Color fillColor, Model model) {
        super(x, y);
        this.model = model;
        rect = new Rectangle(x, y, width, height);
        rect.setStroke(borderColor);
        rect.setFill(fillColor);
        
        rect.setOnMouseClicked(e -> {
            model.getShapes().forEach(shape -> shape.setSelected(false));
            this.setSelected(true);
            model.setSelectedShape(this);
        });
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

    // Creates a copy of the shape, useful for the copy and paste command
    @Override
    public MyShape cloneShape() {
        return new MyRectangle(startX, startY, rect.getWidth(), rect.getHeight(),
                               (Color) rect.getStroke(), (Color) rect.getFill(), model);
    }

    @Override
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
        rect.setX(x);
    }
}
