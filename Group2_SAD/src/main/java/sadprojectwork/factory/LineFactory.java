
package sadprojectwork.factory;

import sadprojectwork.shapes.MyLine;
import sadprojectwork.shapes.MyShape;

public class LineFactory implements ShapeFactory {

    @Override
    public MyShape create(double x, double y, double width, double height, double rotation, boolean isMirroredH, boolean isMirroredV) {
        return new MyLine(x, y, width, height, rotation, isMirroredH, isMirroredV);
    }
    
}

