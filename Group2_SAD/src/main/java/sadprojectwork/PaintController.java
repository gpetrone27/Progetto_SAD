package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.shape.Shape;

public class PaintController implements Initializable {

    // Model reference
    private Model model = new Model();

    // Colors variables
    private Color borderHex = Color.BLACK;
    private Color fillHex = Color.BLACK;

    private boolean cursorMode = true;
    private boolean lineMode = false;
    private boolean rectMode = false;
    private boolean ellipseMode = false;

    private Double startX = null;
    private Double startY = null;

    private MyShape currentShape = null;

    private MyShape selectedShape = null;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane canvas;
    @FXML
    private ToggleGroup shapes;
    @FXML
    private ToggleGroup borderColor;
    @FXML
    private ToggleGroup fillColor;
    @FXML
    private ToggleButton cursorButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initBindings();
        initButtonActions();
        initButtonIcons();
        initCanvasEvents();
    }

    /**
     * Initializes the bindings of the application
     */
    private void initBindings() {

        // Prevents the user from unselecting a border color
        borderColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                borderColor.selectToggle(oldToggle);
            }
        });

        // Prevents the user from unselecting a fill color
        fillColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                fillColor.selectToggle(oldToggle);
            }
        });
    }

    /**
     * Initializes the actions of the buttons of the application
     */
    private void initButtonActions() {
    }

    /**
     * Initializes the icons displayed on the buttons of the application
     */
    private void initButtonIcons() {

        // Cursor button
        FontIcon cursorIcon = new FontIcon("fa-mouse-pointer");
        cursorIcon.setIconSize(24);
        cursorIcon.setIconColor(Color.BLACK);
        cursorButton.setGraphic(cursorIcon);

    }

    private void initCanvasEvents() {

        canvas.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();

            if (cursorMode) {
                return;
            }

            if (rectMode) {
                currentShape = new MyRectangle(startX, startY, 0, 0, borderHex, fillHex);
                canvas.getChildren().add(currentShape.getFxShape());
                enableSelection(currentShape);
            } else if (lineMode) {
                currentShape = new MyLine(startX, startY, startX, startY, borderHex);
                canvas.getChildren().add(currentShape.getFxShape());
                enableSelection(currentShape);
            } else if (ellipseMode) {
                currentShape = new MyEllipsis(startX, startY, 0, 0, borderHex, fillHex);
                canvas.getChildren().add(currentShape.getFxShape());
                enableSelection(currentShape);
            }
        });

        canvas.setOnMouseDragged(e -> {
            if (currentShape == null) {
                return;
            }

            double endX = e.getX();
            double endY = e.getY();

            double width = Math.abs(endX - startX);
            double height = Math.abs(endY - startY);

            currentShape.resize(width, height);
        });

        canvas.setOnMouseReleased(e -> {
            if (currentShape != null) {
                currentShape = null;
            }
        });
    }

    private void enableSelection(MyShape shape) {
        shape.getFxShape().setOnMouseClicked(event -> {
            selectedShape = shape;
            highlightSelected(shape);  // facoltativo
            System.out.println("Selezionato: " + shape.getClass().getSimpleName());
            event.consume();  // blocca il click dal propagarsi al canvas
        });
    }

    private void highlightSelected(MyShape shape) {
        for (javafx.scene.Node node : canvas.getChildren()) {
            if (node instanceof Shape) {
                ((Shape) node).setStrokeWidth(1); // reset
            }
        }
        shape.getFxShape().setStrokeWidth(3); // evidenzia selezionato
    }

    /**
     * Cleares the current drawing and opens a new "untitled" temporary file
     *
     * @param event
     */
    @FXML
    private void newDrawing(ActionEvent event) {
    }

    /**
     * Saves the current drawing in a file
     *
     * @param event
     */
    @FXML
    private void saveDrawing(ActionEvent event) {
    }

    /**
     * Loads a drawing from a file
     *
     * @param event
     */
    @FXML
    private void loadDrawing(ActionEvent event) {
    }

    /**
     * Undoes the last operation
     *
     * @param event
     */
    @FXML
    private void undoOperation(ActionEvent event) {
        model.undoLast();
    }

    /**
     * Redoes the last undone operation
     *
     * @param event
     */
    @FXML
    private void redoOperation(ActionEvent event) {
        model.redoLast();
    }

    /**
     * Updates the variable borderHex to match the user selection
     *
     * @param event
     */
    @FXML
    private void selectBorderColor(ActionEvent event) {

        // Reads the background color of the button that generated the event
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint colorPaint = colorButton.getBackground().getFills().get(0).getFill();

        // Updates the variable borderHex to match the selected color
        if (colorPaint instanceof Color) {
            borderHex = (Color) colorPaint;
        }
    }

    /**
     * Updates the variable fillHex to match the user selection
     *
     * @param event
     */
    @FXML
    private void selectFillColor(ActionEvent event) {

        // Reads the background color of the button that generated the event
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint colorPaint = colorButton.getBackground().getFills().get(0).getFill();

        // Updates the variable fillHex to match the selected color
        if (colorPaint instanceof Color) {
            fillHex = (Color) colorPaint;
            Color selectedColor = (Color) colorPaint;

            if (selectedShape != null) {
                Command changeColor = new ChangeColor(selectedShape, selectedColor);
                model.execute(changeColor);
            } else {
                fillHex = selectedColor;
            }
        }
    }

    /**
     * Selects the cursor: this allows to pan inside the canvas and select
     * shapes
     *
     * @param event
     */
    @FXML
    private void selectCursor(ActionEvent event) {
        cursorMode = true;
        lineMode = false;
        rectMode = false;
        ellipseMode = false;
    }

    /**
     * Selects the Line shape: this allows to draw lines
     *
     * @param event
     */
    @FXML
    private void selectShapeLine(ActionEvent event) {
        cursorMode = false;
        lineMode = true;
        rectMode = false;
        ellipseMode = false;
    }

    /**
     * Selects the Rectangle shape: this allows to draw rectangles
     *
     * @param event
     */
    @FXML
    private void selectShapeRectangle(ActionEvent event) {
        cursorMode = false;
        lineMode = false;
        rectMode = true;
        ellipseMode = false;
    }

    /**
     * Selects the Ellipsis shape: this allows to draw ellipsises
     *
     * @param event
     */
    @FXML
    private void selectShapeEllipsis(ActionEvent event) {
        cursorMode = false;
        lineMode = false;
        rectMode = false;
        ellipseMode = true;
    }

    /**
     * Deletes the selected shape
     *
     * @param event
     */
    @FXML
    private void deleteShape(ActionEvent event) {
    }

    /**
     * Copies the selected shape into the clipboard
     *
     * @param event
     */
    @FXML
    private void copyShape(ActionEvent event) {
    }

    /**
     * Pastes a shape from the clipboard
     *
     * @param event
     */
    @FXML
    private void pasteShape(ActionEvent event) {
    }

    /**
     * Copies the selected shape into the clipboard and deletes it
     *
     * @param event
     */
    @FXML
    private void cutShape(ActionEvent event) {
    }

    /**
     * Shows a popup window when the user clicks "Resize" in the right click
     * menu
     *
     * @param event
     */
    @FXML
    private void showResizeWindow(ActionEvent event) {

        // Creation of the window
        Stage popupWindow = new Stage();
        popupWindow.setTitle("Resize");
        popupWindow.initModality(Modality.APPLICATION_MODAL); // Blocks inputs to other windows

        // UI Elements
        TextField widthField = new TextField();
        TextField heightField = new TextField();
        Button submitButton = new Button("Submit");

        widthField.setPromptText("Width");
        heightField.setPromptText("Height");

        // Functions that is run when the user clicks the Submit button
        submitButton.setOnAction(e -> {
            try {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                resizeShape(width, height); // Add the shape as a parameter here as well
                popupWindow.close();
            } catch (NumberFormatException ex) {
            }
        });

        // Layout
        VBox layout = new VBox(
                10,
                new Label("Enter new dimensions"),
                new HBox(5, widthField, new Label("x"), heightField),
                submitButton
        );
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        // Shows the window
        popupWindow.setScene(new Scene(layout, 240, 160));
        popupWindow.showAndWait();
    }

    /**
     * Resizes the selected shape according to the new dimensions
     *
     * @param shape
     * @param newWidth
     * @param newHeight
     */
    private void resizeShape(int newWidth, int newHeight) {
    }

}
