
package sadprojectwork.shapes;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents a custom text object in the drawing application.
 * Extends MyShape and wraps a JavaFX Text.
 */
public class MyText extends MyShape {

    // Strongly typed reference to wrapped FX shape
    private Text text;
    
    public MyText(double startX, double startY, String displayText, String fontFamily, double size, double rotation, boolean isMirroredH, boolean isMirroredV) {
        super(startX, startY);
        text = new Text(startX, startY, displayText);
        text.setFont(Font.font(fontFamily, size));
        this.fxShape = text;
        setRotation(rotation);
        if (isMirroredH) mirrorHorizontally();
        if (isMirroredV) mirrorVertically();
        
        width = getWidth();
        height = getHeight();
    }

    /**
     * Returns the font of the text.
     * @return font
     */
    public String getFontFamily() {
        return text.getFont().getFamily();
    }
    
    /**
     * Updates the font of the text.
     * @param newFont
     */
    public void setFontFamily(String newFont) {
        text.setFont(Font.font(newFont, getSize()));
    }
    
    /**
     * Returns the size of the font of the text.
     * @return size of font
     */
    public double getSize() {
        return text.getFont().getSize();
    }
    
    /**
     * Updates the font size of the text.
     * @param newSize
     */
    public void setSize(double newSize) {
        text.setFont(Font.font(text.getFont().getFamily(), newSize));
    }
    
    @Override
    public double getStartX() {
        return text.getX();
    }
    
    @Override
    public double getStartY() {
        return text.getY();
    }
    
    @Override
    public MyShape cloneShape() {
        return new MyText(getStartX(), getStartY(), text.getText(), getFontFamily(), getSize(), text.getRotate(), isMirroredHorizontally(), isMirroredVertically());
    }

    @Override
    public void moveTo(double x, double y) {
        startX = x;
        startY = y;
        text.setX(x);
        text.setY(y);
    }

    @Override
    public double getWidth() {
        return text.getLayoutBounds().getWidth();
    }

    @Override
    public double getHeight() {
        return text.getLayoutBounds().getHeight();
    }

    @Override
    public void resize(double newWidth, double newHeight) {
        System.out.println("Resizing a text is meaningless, change its font and size instead.");
    }
    
    @Override
    public String toCSV() {
        return Shapes.TEXT + ";" + startX + ";" + startY + ";" + getWidth() + ";" + getHeight() + ";" + getFxShape().getFill() + ";" + getFxShape().getStroke() + ";" + getFxShape().getRotate() + ";" + isMirroredHorizontally() + ";" + isMirroredVertically() + ";null;" + text.getText() + ";" + getFontFamily() + ";" + getSize();
    }
    
    public String getText() {
        return text.getText();
    }

    public void setText(String newText) {
        text.setText(newText);
    }

}
