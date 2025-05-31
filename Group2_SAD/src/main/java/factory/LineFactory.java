/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import shapes.MyLine;
import shapes.MyShape;

/**
 *
 * @author gianl
 */
public class LineFactory implements ShapeFactory {

    @Override
    public MyShape create(double x, double y, double width, double height, double rotation) {
        return new MyLine(x, y, width, height, rotation);
    }
    
}

