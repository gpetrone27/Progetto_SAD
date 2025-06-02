
package sadprojectwork.factory;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import sadprojectwork.shapes.MyPolygon;
import sadprojectwork.shapes.MyShape;

class PolygonFactory implements ShapeFactory{
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation) {
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x, y));
        return new MyPolygon(x, y, points, rotation);
    }
    
}
