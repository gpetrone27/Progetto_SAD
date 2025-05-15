
package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaintController implements Initializable {

    // Model reference
    private Model model = new Model();
    
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
        initBindings();
    }
    
    private void initBindings() {
        
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
        model.undoLast();
    }

    @FXML
    private void redoOperation(ActionEvent event) {
        model.redoLast();
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
    private void showResizeWindow(ActionEvent event) {
        
        // Creation of the window
        Stage popupWindow = new Stage();
        popupWindow.setTitle("Resize");
        popupWindow.initModality(Modality.APPLICATION_MODAL); // Blocks inputs to other windows
        
        // UI Elements
        TextField field1 = new TextField();
        TextField field2 = new TextField();
        Button submitButton = new Button("Submit");
        
        field1.setPromptText("Width");
        field2.setPromptText("Height");
        submitButton.setOnAction(e -> {
            try {
                int num1 = Integer.parseInt(field1.getText().trim());
                int num2 = Integer.parseInt(field2.getText().trim());
                resizeShape(num1, num2); // Add the shape as a parameter here as well
                popupWindow.close();
            } catch (NumberFormatException ex) {}
        });
        
        VBox layout = new VBox(10,
                new Label("Enter new dimensions"),
                new HBox(5, field1, new Label("x"), field2),
                submitButton
        );
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);
        
        popupWindow.setScene(new Scene(layout, 240, 160));
        popupWindow.showAndWait();
    }
    
    // TO DO: Add the shape to be resized as a parameter of the method
    private void resizeShape(int width, int height) {
        
    }
    
}
