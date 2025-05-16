
package sadprojectwork;

import javafx.scene.shape.Shape;

public abstract class MyShape {

    protected double startX;
    protected double startY;

    public MyShape(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public abstract Shape getFxShape();

    public abstract void resize(double newWidth, double newHeight);

    public abstract MyShape cloneShape();

    public void setPosition(double x, double y) {
        this.startX = x;
        this.startY = y;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }
}
