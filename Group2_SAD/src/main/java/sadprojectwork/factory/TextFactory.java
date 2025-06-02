
package sadprojectwork.factory;
import sadprojectwork.shapes.MyShape;
import sadprojectwork.shapes.MyText;

public class TextFactory implements ShapeFactory{
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation, boolean isMirroredH, boolean isMirroredV) {
        return new MyText(x, y, "Text", "Arial", 14, rotation, isMirroredH, isMirroredV);
    }
    
}
