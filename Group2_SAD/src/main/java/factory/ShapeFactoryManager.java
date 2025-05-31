/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.util.HashMap;
import java.util.Map;
import shapes.MyShape;
import shapes.Shapes;

/**
 *
 * @author gianl
 */
public class ShapeFactoryManager {

    private final Map<Shapes, ShapeFactory> registry = new HashMap<>();

    public ShapeFactoryManager() {
        registry.put(Shapes.RECTANGLE, new RectangleFactory());
        registry.put(Shapes.ELLIPSE, new EllipseFactory());
        registry.put(Shapes.LINE, new LineFactory());
        registry.put(Shapes.POLYGON, new PolygonFactory());
        registry.put(Shapes.TEXT, new TextFactory());
    }

    public MyShape createShape(Shapes type, double x, double y, double width, double height, double rotation) {
        ShapeFactory factory = registry.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Nessuna factory registrata per: " + type);
        }
        return factory.create(x, y, width, height, rotation);
    }
}
