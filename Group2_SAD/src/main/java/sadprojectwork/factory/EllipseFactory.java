
package sadprojectwork.factory;

import sadprojectwork.shapes.MyEllipse;
import sadprojectwork.shapes.MyShape;

public class EllipseFactory implements ShapeFactory {
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation) {
        return new MyEllipse(x, y, width, height, rotation);
    }
    
}
