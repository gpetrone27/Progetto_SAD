/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import shapes.MyPolygon;
import shapes.MyShape;

/**
 *
 * @author gianl
 */
public class PolygonFactory implements ShapeFactory{
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation) {
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x, y));
        return new MyPolygon(x, y, points, rotation);
    }
    
}
