/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;
import shapes.MyShape;
import shapes.MyText;

/**
 *
 * @author gianl
 */
public class TextFactory implements ShapeFactory{
    
    @Override
    public MyShape create(double x, double y, double width, double height, double rotation) {
        return new MyText(x, y, "Text", "Arial", 14, rotation);
    }
    
}
