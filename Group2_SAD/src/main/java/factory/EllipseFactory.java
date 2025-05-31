/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import shapes.MyEllipse;
import shapes.MyShape;

/**
 *
 * @author gianl
 */
public class EllipseFactory implements ShapeFactory {
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation) {
        return new MyEllipse(x, y, width, height, rotation);
    }
    
}
