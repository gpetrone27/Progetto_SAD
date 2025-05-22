
package shapes;

import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class MyPolygon extends MyShape {
    
    private Path path;

    public MyPolygon(double startX, double startY) {
        super(startX, startY);
        path = new Path();
        path.setStrokeWidth(3);
        addMoveTo(startX, startY);
        this.fxShape = path;
    }
    
    public void addMoveTo(double x, double y) {
        path.getElements().add(new MoveTo(x, y));
    }
    
    public boolean addLineTo(double x, double y) {
        if (Math.abs(x - startX) <= 10 && Math.abs(y - startY) <= 10) {
            path.getElements().add(new ClosePath());
            return true;
        }
        else {
            path.getElements().add(new LineTo(x, y));
            return false;
        }
    }
    
    @Override
    public void resize(double newFirstDim, double newSecondDim) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MyShape cloneShape() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toCSV() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void moveTo(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}