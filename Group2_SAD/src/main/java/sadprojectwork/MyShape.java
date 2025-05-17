
package sadprojectwork;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class MyShape {

    protected double startX;
    protected double startY;
    private boolean selected = false;

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
    public boolean isSelected(){
        return selected;
    }
    
    public void setSelected(boolean selected){
        this.selected = selected;
        if (getFxShape() != null) {
            if (selected) {
                getFxShape().setStroke(Color.DODGERBLUE);
                getFxShape().setStrokeWidth(2);
            } else {
                getFxShape().setStroke(null);
            }
        }
    }
}
