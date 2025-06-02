
package sadprojectwork.factory;

import java.util.HashMap;
import java.util.Map;
import sadprojectwork.shapes.MyShape;
import sadprojectwork.shapes.Shapes;

public class ShapeFactoryManager {

    private final Map<Shapes, ShapeFactory> registry = new HashMap<>();

    public ShapeFactoryManager() {
        registry.put(Shapes.RECTANGLE, new RectangleFactory());
        registry.put(Shapes.ELLIPSE, new EllipseFactory());
        registry.put(Shapes.LINE, new LineFactory());
        registry.put(Shapes.POLYGON, new PolygonFactory());
        registry.put(Shapes.TEXT, new TextFactory());
    }

    public MyShape createShape(Shapes type, double x, double y, double width, double height, double rotation, boolean isMirroredH, boolean isMirroredV) {
        ShapeFactory factory = registry.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Nessuna factory registrata per: " + type);
        }
        return factory.create(x, y, width, height, rotation, isMirroredH, isMirroredV);
    }
}
