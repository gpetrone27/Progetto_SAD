
package sadprojectwork.factory;

import sadprojectwork.shapes.MyRectangle;
import sadprojectwork.shapes.MyShape;

public class RectangleFactory implements ShapeFactory{
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation, boolean isMirroredH, boolean isMirroredV) {
        return new MyRectangle(x, y, width, height, rotation, isMirroredH, isMirroredV);
    }
    
}
