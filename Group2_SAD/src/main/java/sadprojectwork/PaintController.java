
package sadprojectwork;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class PaintController implements Initializable {

    // Model reference
    private Model model = new Model();

    // Colors variables
    private Color borderHex = Color.BLACK;
    private Color fillHex = Color.BLACK;

    private BooleanProperty cursorMode = new SimpleBooleanProperty(true);
    private BooleanProperty shapeSelected = new SimpleBooleanProperty(false);
    private BooleanProperty hasClipboard = new SimpleBooleanProperty(false);

    private Shapes mode = Shapes.CURSOR;

    private Double startX = null;
    private Double startY = null;

    private MyShape currentShape = null;
    private MyShape selectedShape = null;

    private double dragStartX;
    private double dragStartY;
    private double originalX;
    private double originalY;

    private double lastMouseX;
    private double lastMouseY;

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
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private MenuItem resizeMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private VBox propertiesPanel;

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

        // Updates the last position of the mouse, relative to the canvas, whenever it moves
        canvas.setOnMouseMoved(event -> {
            lastMouseX = event.getX();
            lastMouseY = event.getY();
        });
        
        // Adds a listener to update the canvas every time the model changes
        model.getShapes().addListener((ListChangeListener<MyShape>) change -> {
            redrawCanvas();
        });
        
        // Binds the value of the property cursorMode to the button cursorButton
        cursorMode.bindBidirectional(cursorButton.selectedProperty());

        // Binds the disable property of the right click menu items to shapeSelected
        cutMenuItem.disableProperty().bind(shapeSelected.not());
        copyMenuItem.disableProperty().bind(shapeSelected.not());
        deleteMenuItem.disableProperty().bind(shapeSelected.not());
        resizeMenuItem.disableProperty().bind(shapeSelected.not());

        // Binds the disable property of the paste operation to the state of clipboard
        pasteMenuItem.disableProperty().bind(hasClipboard.not());

        // Binds the visible and managed property of the propertiesPanel to shapeSelected
        propertiesPanel.visibleProperty().bind(shapeSelected);
        propertiesPanel.managedProperty().bind(shapeSelected);

        // Prevents the user from unselecting colors
        borderColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                borderColor.selectToggle(oldToggle);
            }
        });
        fillColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                fillColor.selectToggle(oldToggle);
            }
        });

        // Prevents the user from unselecting cursor or shapes
        shapes.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                shapes.selectToggle(oldToggle);
            }
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

    /**
     * Initializes all the events related to the canvas
     */
    private void initCanvasEvents() {

        canvas.setOnMousePressed(e -> {
            
            startX = e.getX();
            startY = e.getY();

            // Saves the original coordinates when the move operation starts
            if (selectedShape != null) {
                dragStartX = startX;
                dragStartY = startY;
                originalX = selectedShape.getStartX();
                originalY = selectedShape.getStartY();
                e.consume();
                return;
            }
            
            // Draws the shape if any of them is selected
            switch(mode) {
                case LINE -> {
                    BorderColorDecorator myLine = new BorderColorDecorator(new MyLine(startX, startY, startX, startY), borderHex);
                    addShape(myLine);
                    enableSelection(myLine);
                    currentShape = myLine;
                }
                case RECTANGLE -> {
                    BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(new MyRectangle(startX, startY, 0, 0), fillHex), borderHex);
                    addShape(myRectangle);
                    enableSelection(myRectangle);
                    currentShape = myRectangle;
                }
                case ELLIPSE -> {
                    BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(new MyEllipse(startX, startY, 0, 0), fillHex), borderHex);
                    addShape(myEllipse);
                    enableSelection(myEllipse);
                    currentShape = myEllipse;
                }
            }
        });

        canvas.setOnMouseDragged(e -> {
            
            // Moves the shape while the user is dragging it
            if (currentShape == null) {
                
                if (selectedShape != null) {
                    
                    double dx = e.getX() - dragStartX;
                    double dy = e.getY() - dragStartY;

                    selectedShape.moveOf(dx, dy);

                    dragStartX = e.getX();
                    dragStartY = e.getY();

                    e.consume();
                }
            }
            
            // Preview of the shape while the user is creating it
            else {
                
                double endX = e.getX();
                double endY = e.getY();

                switch(mode) {
                    case LINE -> {
                        currentShape.resize(endX, endY);
                    }
                    default -> {
                        currentShape.resize(endX - startX, endY - startY);
                    }
                }
            }
        });

        canvas.setOnMouseReleased(e -> {

            // Finalizes the move command when the user releases the shape
            if (selectedShape != null && currentShape == null) {
                double newX = selectedShape.getStartX();
                double newY = selectedShape.getStartY();
                if (newX != originalX || newY != originalY) {
                    Command moveCmd = new MoveCommand(selectedShape, originalX, originalY, newX, newY);
                    model.execute(moveCmd);
                }
            }

            // Creation of the shape is completed
            if (currentShape != null) {
                currentShape = null;
            }

            e.consume();
        });

        // Disables selection when user clicks on blank canvas
        canvas.setOnMouseClicked(event -> {
            disableSelection();
        });
    }

    /**
     * Redraws the canvas based on the model list of shapes
     */
    private void redrawCanvas() {
        canvas.getChildren().clear();
        for (MyShape shapeToAdd : model.getShapes()) {
            canvas.getChildren().add(shapeToAdd.getFxShape());
        }
    }
    
    /**
     * Sets the mode
     */
    private void setMode(Shapes mode) {
        this.mode = mode;
    }
    
    /**
     * Enables the selection of the given shape
     * @param shape
     */
    private void enableSelection(MyShape shape) {
        shape.getFxShape().setOnMouseClicked(event -> {
            selectedShape = shape;
            shapeSelected.set(true);
            highlightSelected(shape);
            cursorMode.set(true);
            event.consume();
        });
    }

    /**
     * Deactivates the current selection and clears all effects
     */
    private void disableSelection() {
        selectedShape = null;
        shapeSelected.set(false);
        for (javafx.scene.Node node : canvas.getChildren()) {
            if (node instanceof Shape resetShape) {
                resetShape.setEffect(null); // Reset effects
            }
        }
    }

    /**
     * @param shape
     */
    private void highlightSelected(MyShape shape) {

        // Removes the effect from all shapes
        for (javafx.scene.Node node : canvas.getChildren()) {
            if (node instanceof Shape resetShape) {
                resetShape.setEffect(null); // Reset effects
            }
        }

        // Adds the effect to the selected shape
        DropShadow ds = new DropShadow();
        ds.setColor(Color.DODGERBLUE);
        ds.setRadius(10);
        shape.getFxShape().setEffect(ds);
    }

    /**
     * Cleares the current drawing and opens a new "untitled" temporary file
     * @param event
     */
    @FXML
    private void newDrawing(ActionEvent event) {
        model.clear();                
        selectedShape = null;
        shapeSelected.set(false);
        currentShape = null;
        hasClipboard.set(false);
        cursorMode.set(true);
        setMode(Shapes.CURSOR);
    }

    /**
     * Saves the current drawing in a file
     * @param event
     */
    @FXML
    private void saveDrawing(ActionEvent event) {
    }

    /**
     * Loads a drawing from a file
     * @param event
     */
    @FXML
    private void loadDrawing(ActionEvent event) {
    }

    /**
     * Undoes the last operation
     * @param event
     */
    @FXML
    private void undoOperation(ActionEvent event) {
        model.undoLast();
        hasClipboard.set(model.getClipboard() != null);
    }

    /**
     * Redoes the last undone operation
     * @param event
     */
    @FXML
    private void redoOperation(ActionEvent event) {
        model.redoLast();
        hasClipboard.set(model.getClipboard() != null);
    }

    /**
     * Updates the variable borderHex to match the user selection
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
                Command changeColor = new ChangeColorCommand(selectedShape, null, selectedColor);
                model.execute(changeColor);
            } else {
                borderHex = selectedColor;
            }
        }
    }

    /**
     * Updates the variable fillHex to match the user selection
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
                Command changeColor = new ChangeColorCommand(selectedShape, selectedColor, null);
                model.execute(changeColor);
            } else {
                fillHex = selectedColor;
            }
        }
    }

    /**
     * Selects the cursor: this allows to pan inside the canvas and select
     * shapes
     * @param event
     */
    @FXML
    private void selectCursor(ActionEvent event) {
        setMode(Shapes.CURSOR);
    }

    /**
     * Selects the Line shape: this allows to draw lines
     * @param event
     */
    @FXML
    private void selectShapeLine(ActionEvent event) {
        disableSelection();
        setMode(Shapes.LINE);
    }

    /**
     * Selects the Rectangle shape: this allows to draw rectangles
     * @param event
     */
    @FXML
    private void selectShapeRectangle(ActionEvent event) {
        disableSelection();
        setMode(Shapes.RECTANGLE);
    }

    /**
     * Selects the Ellipse shape: this allows to draw ellipsises
     * @param event
     */
    @FXML
    private void selectShapeEllipse(ActionEvent event) {
        disableSelection();
        setMode(Shapes.ELLIPSE);
    }

    /**
     * Copies the selected shape into the clipboard and deletes it
     * @param event
     */
    @FXML
    private void cutShape(ActionEvent event) {
        if (selectedShape != null) {
            Command cutCmd = new CutCommand(model, selectedShape);
            model.execute(cutCmd);
            shapeSelected.set(false);
        }
        hasClipboard.set(model.getClipboard() != null);
    }

    /**
     * Copies the selected shape into the clipboard
     * @param event
     */
    @FXML
    private void copyShape(ActionEvent event) {
        if (selectedShape != null) {
            Command copyCmd = new CopyCommand(model, selectedShape);
            model.execute(copyCmd);
        }
        hasClipboard.set(model.getClipboard() != null);
    }

    /**
     * Pastes a shape from the clipboard
     * @param event
     */
    @FXML
    private void pasteShape(ActionEvent event) {
        PasteCommand pasteCmd = new PasteCommand(model, lastMouseX, lastMouseY);
        model.execute(pasteCmd);
        enableSelection(pasteCmd.getPastedShape());
    }

    /**
     * Deletes the selected shape
     * @param event
     */
    @FXML
    private void deleteShape(ActionEvent event) {
        if (selectedShape != null) {
            Command deleteCmd = new DeleteCommand(model, selectedShape);
            model.execute(deleteCmd);
            shapeSelected.set(false);
        }
    }

    /**
     * Resizes the selected shape according to the new dimensions
     * @param shape
     * @param newWidth
     * @param newHeight
     */
    private void resizeShape(MyShape shape, int newWidth, int newHeight) {
        if (shape != null) {
            Command resizeCmd = new ResizeCommand(shape, newWidth, newHeight);
            model.execute(resizeCmd);
        }
    }
    
    /**
     * Adds a shape to the model
     * @param shape 
     */
    private void addShape(MyShape shape) {
        AddShapeCommand addCmd = new AddShapeCommand(model, shape);
        model.execute(addCmd);
    }

    /**
     * Shows a popup window when the user clicks "Resize" in the right click
     * menu
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
                resizeShape(selectedShape, width, height);
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

}
