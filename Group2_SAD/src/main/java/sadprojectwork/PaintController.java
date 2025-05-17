package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
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

    private BooleanProperty cursorMode = new SimpleBooleanProperty(true);
    private boolean lineMode = false;
    private boolean rectMode = false;
    private boolean ellipseMode = false;

    private Double startX = null;
    private Double startY = null;

    private MyShape currentShape = null;
    private MyShape selectedShape = null;
    
    private static PaintController instance;

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
    @FXML
    private ScrollPane drawingPane;
    @FXML
    private ContextMenu rightClickMenu;
    @FXML 
    private MenuItem cutMenuItem;
    @FXML 
    private MenuItem copyMenuItem;
    @FXML 
    private MenuItem deleteMenuItem;
    @FXML 
    private MenuItem resizeMenuItem;
    
    public PaintController(){
        instance = this;
    }
    
    public static PaintController getInstance(){
        return instance;
    }

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

        // Binds the value of the property cursorMode to the button cursorButton
        cursorMode.bindBidirectional(cursorButton.selectedProperty());
        
        // Prevents the user from unselecting colors
        borderColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null)
                borderColor.selectToggle(oldToggle);
        });
        fillColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null)
                fillColor.selectToggle(oldToggle);
        });
        
        // Prevents the user from unselecting cursor or shapes
        shapes.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null)
                shapes.selectToggle(oldToggle);
        });
        
        // Prevents the user from panning while not in cursor mode
        cursorMode.addListener((obs, wasCursor, isCursor) -> {
            if (!isCursor) {
                drawingPane.setPannable(false); // Disable panning
            } else {
                drawingPane.setPannable(true); // Enable panning
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

            if (cursorMode.get())
                return;

            if (rectMode) {
                currentShape = new MyRectangle(startX, startY, 0, 0, borderHex, fillHex, model);
                canvas.getChildren().add(currentShape.getFxShape());
                model.addShape(currentShape);
                //enableSelection(currentShape);
            } else if (lineMode) {
                currentShape = new MyLine(startX, startY, startX, startY, borderHex, model);
                canvas.getChildren().add(currentShape.getFxShape());
                model.addShape(currentShape);
                //enableSelection(currentShape);
            } else if (ellipseMode) {
                currentShape = new MyEllipsis(startX, startY, 0, 0, borderHex, fillHex, model);
                canvas.getChildren().add(currentShape.getFxShape());
                model.addShape(currentShape);
                //enableSelection(currentShape);
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

    /*private void enableSelection(MyShape shape) {
        shape.getFxShape().setOnMouseClicked(event -> {
            selectedShape = shape;
            highlightSelected(shape);  
            System.out.println("Selezionato: " + shape.getClass().getSimpleName());
            event.consume();  
        });
    }

    private void highlightSelected(MyShape shape) {
        for (javafx.scene.Node node : canvas.getChildren()) {
            if (node instanceof Shape resetShape) {
                resetShape.setStrokeWidth(1); // Reset
            }
        }
        shape.getFxShape().setStrokeWidth(3); // Highlight selected shape
    }*/

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
        if (colorPaint instanceof Color selectedColor) {
            borderHex = (Color) colorPaint;

            if (selectedShape != null) {
                Command changeColor = new ChangeColor(selectedShape, null, selectedColor);
                model.execute(changeColor);
            } else {
                borderHex = selectedColor;
            }
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
        if (colorPaint instanceof Color color) {
            fillHex = color;
            Color selectedColor = color;

            if (selectedShape != null) {
                Command changeColor = new ChangeColor(selectedShape, selectedColor, null);
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
        lineMode = false;
        rectMode = false;
        ellipseMode = true;
    }
    
    public void activeMenuItem(){
        boolean hasSelected = model.getSelectedShape() != null;
        
        cutMenuItem.setDisable(!hasSelected);
        copyMenuItem.setDisable(!hasSelected);
        deleteMenuItem.setDisable(!hasSelected);
        resizeMenuItem.setDisable(!hasSelected);
    }

    /**
     * Deletes the selected shape
     *
     * @param event
     */
    @FXML
    private void deleteShape(ActionEvent event) {
        MyShape selected = model.getSelectedShape();
        if (selected != null) {
            Command deleteCmd = new DeleteCommand(model, selected, canvas);
            model.execute(deleteCmd);
            activeMenuItem();
        }
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
        MyShape selected = model.getSelectedShape();
        if (selected != null) {
            Command cutCmd = new CutCommand(selectedShape, model, canvas);
            model.execute(cutCmd);
            model.setSelectedShape(null);
            activeMenuItem();
        }
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
