
package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToggleGroup;

public class PaintController implements Initializable {

    @FXML
    private ToggleGroup shapes;
    @FXML
    private Canvas canvas;
    @FXML
    private ToggleGroup borderColor;
    @FXML
    private ToggleGroup fillColor;
    @FXML
    private ContextMenu rightClickMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void newDrawing(ActionEvent event) {
    }

    @FXML
    private void saveDrawing(ActionEvent event) {
    }

    @FXML
    private void loadDrawing(ActionEvent event) {
    }

    @FXML
    private void undoOperation(ActionEvent event) {
    }

    @FXML
    private void redoOperation(ActionEvent event) {
    }

    @FXML
    private void selectShapeEllipsis(ActionEvent event) {
    }

    @FXML
    private void selectShapeRectangle(ActionEvent event) {
    }

    @FXML
    private void selectBorderBlack(ActionEvent event) {
    }

    @FXML
    private void selectBorderWhite(ActionEvent event) {
    }

    @FXML
    private void selectBorderRed(ActionEvent event) {
    }

    @FXML
    private void selectBorderOrange(ActionEvent event) {
    }

    @FXML
    private void selectBorderYellow(ActionEvent event) {
    }

    @FXML
    private void selectBorderGreen(ActionEvent event) {
    }

    @FXML
    private void selectBorderBlue(ActionEvent event) {
    }

    @FXML
    private void selectBorderPurple(ActionEvent event) {
    }

    @FXML
    private void selectFillBlack(ActionEvent event) {
    }

    @FXML
    private void selectFillWhite(ActionEvent event) {
    }

    @FXML
    private void selectFillRed(ActionEvent event) {
    }

    @FXML
    private void selectFillOrange(ActionEvent event) {
    }

    @FXML
    private void selectFillYellow(ActionEvent event) {
    }

    @FXML
    private void selectFillGreen(ActionEvent event) {
    }

    @FXML
    private void selectFillBlue(ActionEvent event) {
    }

    @FXML
    private void selectFillPurple(ActionEvent event) {
    }

    @FXML
    private void selectShapeLine(ActionEvent event) {
    }

    @FXML
    private void deleteShape(ActionEvent event) {
    }

    @FXML
    private void copyShape(ActionEvent event) {
    }

    @FXML
    private void pasteShape(ActionEvent event) {
    }

    @FXML
    private void cutShape(ActionEvent event) {
    }

    @FXML
    private void resizeShape(ActionEvent event) {
    }
    
}
