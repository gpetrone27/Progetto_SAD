
package sadprojectwork.factory;

import sadprojectwork.shapes.MyShape;

public interface ShapeFactory {
    MyShape create(double x, double y, double width, double height, double rotation);
}
