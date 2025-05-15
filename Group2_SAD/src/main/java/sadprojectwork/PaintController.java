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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;


public class PaintController implements Initializable {

    // Model reference
    private Model model = new Model();

    // Colors variables
    private Color borderHex = Color.BLACK;
    private Color fillHex = Color.BLACK;

    private boolean lineMode = false;
    private boolean rectMode = false;
    private boolean ellipseMode = false;
    private Double startX = null;
    private Double startY = null;

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
        canvas.setOnMouseClicked(event -> {
            double clickX = event.getX();
            double clickY = event.getY();

            if (lineMode || rectMode || ellipseMode) {
                if (startX == null && startY == null) {
                    startX = clickX;
                    startY = clickY;
                } else {
                    double endX = clickX;
                    double endY = clickY;

                    double x = Math.min(startX, endX);
                    double y = Math.min(startY, endY);
                    double width = Math.abs(endX - startX);
                    double height = Math.abs(endY - startY);

                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setStroke(borderHex);
                    gc.setFill(fillHex);
                    gc.setLineWidth(2);

                    if (lineMode) {
                        gc.strokeLine(startX, startY, endX, endY);
                    } else if (rectMode) {
                        gc.fillRect(x, y, width, height);
                        gc.strokeRect(x, y, width, height);
                    } else if (ellipseMode) {
                        gc.fillOval(x, y, width, height);
                        gc.strokeOval(x, y, width, height);
                    }

                    // Reset
                    startX = null;
                    startY = null;
                    lineMode = false;
                    rectMode = false;
                    ellipseMode = false;
                }
            }
        });
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
    private void selectBorderColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint colorPaint = colorButton.getBackground().getFills().get(0).getFill();
        if (colorPaint instanceof Color) {
            borderHex = (Color) colorPaint;
        }
        System.out.println(borderHex);
    }

    @FXML
    private void selectFillColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint colorPaint = colorButton.getBackground().getFills().get(0).getFill();
        if (colorPaint instanceof Color) {
            fillHex = (Color) colorPaint;
        }
        System.out.println(fillHex);
    }

    @FXML
    private void selectShapeEllipsis(ActionEvent event) {
        // Attiva la modalità disegno di ellisse
        ellipseMode = true;
        
        // Disattiva le altre modalità
        rectMode = false;
        lineMode = false;
        
        // Resetta il punto iniziale del disegno
        startX = null;
        startY = null;
    }

    @FXML
    private void selectShapeRectangle(ActionEvent event) {
        // Attiva la modalità disegno di rettangolo
        rectMode = true;
        
        // Disattiva le altre modalità
        ellipseMode = false;
        lineMode = false;
        
        // Resetta il punto iniziale del disegno
        startX = null;
        startY = null;
    }

    @FXML
    private void selectShapeLine(ActionEvent event) {
        // Attiva la modalità disegno di linea
        lineMode = true;
        
        // Resetta il punto iniziale del disegno
        startX = null;
        startY = null;
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
            } catch (NumberFormatException ex) {
            }
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
