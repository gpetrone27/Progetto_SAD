
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

public class MyEllipsis extends MyShape {
    
    private Ellipse ellipse;

    public MyEllipsis(double centerX, double centerY, double radiusX, double radiusY, Color borderColor, Color fillColor) {
        super(centerX, centerY);
        ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setStroke(borderColor);
        ellipse.setFill(fillColor);
    }

    @Override
    public Shape getFxShape() {
        return ellipse;
    }

    @Override
    public void resize(double newRadiusX, double newRadiusY) {
        ellipse.setRadiusX(newRadiusX);
        ellipse.setRadiusY(newRadiusY);
    }

    @Override
    public MyShape cloneShape() {
        return new MyEllipsis(startX, startY, ellipse.getRadiusX(), ellipse.getRadiusY(),
                              (Color) ellipse.getStroke(), (Color) ellipse.getFill());
    }
    
     @Override
    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
        ellipse.setCenterX(x);
        ellipse.setCenterY(y);
    }
}
