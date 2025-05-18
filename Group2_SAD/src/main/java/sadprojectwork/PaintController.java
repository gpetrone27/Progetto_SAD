
package sadprojectwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.Toggle;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class PaintController implements Initializable {

    // Model reference
    private Model model = new Model();

    // Colors variables
    private Color borderHex = Color.BLACK;
    private Color fillHex = Color.BLACK;

    private ObjectProperty<Shapes> modeProperty = new SimpleObjectProperty<>(Shapes.CURSOR);
    private ObjectProperty<MyShape> currentShape = new SimpleObjectProperty<>(null);
    private ObjectProperty<MyShape> selectedShape = new SimpleObjectProperty<>(null);

    private Double startX = null;
    private Double startY = null;

    private double dragStartX;
    private double dragStartY;
    private double originalX;
    private double originalY;

    private double lastMouseX;
    private double lastMouseY;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ScrollPane drawingPane;
    @FXML
    private Pane canvas;
    @FXML
    private ToggleGroup shapes;
    @FXML
    private ToggleGroup borderColor;
    @FXML
    private ToggleGroup fillColor;
    @FXML
    private ToggleGroup borderColor1;
    @FXML
    private ToggleGroup fillColor1;
    @FXML
    private ToggleButton cursorButton;
    @FXML
    private ToggleButton ellipseButton;
    @FXML
    private ToggleButton rectangleButton;
    @FXML
    private ToggleButton lineButton;
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
        initButtonActions();
        initBindings();
        initButtonIcons();
        initCanvasEvents();
    }

    /**
     * Initializes the actions of the buttons of the application
     */
    private void initButtonActions() {
        cursorButton.setUserData(Shapes.CURSOR);
        lineButton.setUserData(Shapes.LINE);
        rectangleButton.setUserData(Shapes.RECTANGLE);
        ellipseButton.setUserData(Shapes.ELLIPSE);
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
        
        // Binds the disable property of the right click menu items to shapeSelected
        cutMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() == null, selectedShape));
        copyMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() == null, selectedShape));
        deleteMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() == null, selectedShape));
        resizeMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() == null, selectedShape));

        // Binds the disable property of the paste operation to the state of clipboard and the cursor selection
        pasteMenuItem.disableProperty().bind(Bindings.or(Bindings.createBooleanBinding(() -> model.getClipboard() == null, model.clipboardProperty()), Bindings.createBooleanBinding(() -> modeProperty.get() != Shapes.CURSOR, modeProperty)));

        // Binds the visible and managed property of the propertiesPanel to shapeSelected
        propertiesPanel.visibleProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() != null, selectedShape));
        propertiesPanel.managedProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() != null, selectedShape));

        // Prevents the user from unselecting colors
        borderColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                borderColor.selectToggle(oldToggle);
            }
        });
        borderColor1.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                borderColor1.selectToggle(oldToggle);
            }
        });
        fillColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                fillColor.selectToggle(oldToggle);
            }
        });
        fillColor1.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                fillColor1.selectToggle(oldToggle);
            }
        });

        // Prevents the user from unselecting cursor or shapes
        shapes.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                shapes.selectToggle(oldToggle);
            }
            else {
                modeProperty.set((Shapes) newToggle.getUserData());
            }
        });
        
        // Updates toggle buttons when the selected mode changes
        modeProperty.addListener((obs, oldMode, newMode) -> {
            for (Toggle toggle : shapes.getToggles()) {
                if (toggle.getUserData() == newMode) {
                    shapes.selectToggle(toggle);
                    break;
                }
            }
        });

        // Prevents the user from panning while not in cursor mode
        modeProperty.addListener((obs, wasCursor, isCursor) -> {
            if (isCursor == Shapes.CURSOR) {
                drawingPane.setPannable(true); // Enable panning
            } else {
                drawingPane.setPannable(false); // Disable panning
            }
        });

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
            if (selectedShape.get() != null) {
                dragStartX = startX;
                dragStartY = startY;
                originalX = selectedShape.get().getStartX();
                originalY = selectedShape.get().getStartY();
                e.consume();
                return;
            }
            
            // Draws the shape if any of them is selected
            switch(modeProperty.get()) {
                case LINE -> {
                    BorderColorDecorator myLine = new BorderColorDecorator(new MyLine(startX, startY, startX, startY), borderHex);
                    addShape(myLine);
                    enableSelection(myLine);
                    currentShape.set(myLine);
                }
                case RECTANGLE -> {
                    BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(new MyRectangle(startX, startY, 0, 0), fillHex), borderHex);
                    addShape(myRectangle);
                    enableSelection(myRectangle);
                    currentShape.set(myRectangle);
                }
                case ELLIPSE -> {
                    BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(new MyEllipse(startX, startY, 0, 0), fillHex), borderHex);
                    addShape(myEllipse);
                    enableSelection(myEllipse);
                    currentShape.set(myEllipse);
                }
            }
        });

        canvas.setOnMouseDragged(e -> {
            
            // Moves the shape while the user is dragging it
            if (currentShape.get() == null) {
                
                if (selectedShape.get() != null) {
                    
                    double dx = e.getX() - dragStartX;
                    double dy = e.getY() - dragStartY;

                    selectedShape.get().moveOf(dx, dy);

                    dragStartX = e.getX();
                    dragStartY = e.getY();

                    e.consume();
                }
            }
            
            // Preview of the shape while the user is creating it
            else {
                
                double endX = e.getX();
                double endY = e.getY();

                switch(modeProperty.get()) {
                    case LINE -> {
                        currentShape.get().resize(endX, endY);
                    }
                    default -> {
                        currentShape.get().resize(endX - startX, endY - startY);
                    }
                }
            }
        });

        canvas.setOnMouseReleased(e -> {

            // Finalizes the move command when the user releases the shape
            if (selectedShape.get() != null && currentShape.get() == null) {
                double newX = selectedShape.get().getStartX();
                double newY = selectedShape.get().getStartY();
                if (newX != originalX || newY != originalY) {
                    Command moveCmd = new MoveCommand(selectedShape.get(), originalX, originalY, newX, newY);
                    model.execute(moveCmd);
                }
            }

            // Creation of the shape is completed
            if (currentShape.get() != null) {
                currentShape.set(null);
            }

            e.consume();
        });

        // Disables selection when user clicks on blank canvas
        canvas.setOnMouseClicked(event -> {
            clearSelection();
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
     * Enables the selection of the given shape
     * @param shape
     */
    private void enableSelection(MyShape shape) {
        shape.getFxShape().setOnMouseClicked(event -> {
            selectedShape.set(shape);
            highlightSelected(shape);
            event.consume();
        });
    }

    /**
     * Deactivates the current selection and clears all effects
     */
    private void clearSelection() {
        selectedShape.set(null);
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
        selectedShape.set(null);
        currentShape.set(null);
    }

    /**
     * Saves the current drawing in a file
     * @param event
     */
    @FXML
    private void saveDrawing(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save drawing as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("SHAPE;STARTX;STARTY;WIDTH;HEIGHT;FILL;BORDER");
                for (MyShape shape : model.getShapes()) {
                    writer.println(shape.toCSV());
                }
            } catch (IOException e) {}
        }
    }
    
    /**
     * Loads a drawing from a file
     * @param event
     */
    @FXML
    private void loadDrawing(ActionEvent event) {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load drawing from CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        
        if (file != null) {
            model.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    Shapes loadedMode = Shapes.valueOf(parts[0]);
                    double loadedStartX = Double.parseDouble(parts[1]);
                    double loadedStartY = Double.parseDouble(parts[2]);
                    double loadedWidth = Double.parseDouble(parts[3]);
                    double loadedHeight = Double.parseDouble(parts[4]);
                    Color loadedFill = Color.valueOf(parts[5]);
                    Color loadedBorder = Color.valueOf(parts[6]);
                    switch(loadedMode) {
                        case LINE -> {
                            BorderColorDecorator myLine = new BorderColorDecorator(new MyLine(loadedStartX, loadedStartY, loadedStartX, loadedStartY), loadedBorder);
                            myLine.resize(loadedWidth, loadedHeight);
                            addShape(myLine);
                            enableSelection(myLine);
                        }
                        case RECTANGLE -> {
                            BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(new MyRectangle(loadedStartX, loadedStartY, 0, 0), loadedFill), loadedBorder);
                            myRectangle.resize(loadedWidth, loadedHeight);
                            addShape(myRectangle);
                            enableSelection(myRectangle);
                        }
                        case ELLIPSE -> {
                            BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(new MyEllipse(loadedStartX, loadedStartY, 0, 0), loadedFill), loadedBorder);
                            myEllipse.resize(loadedWidth, loadedHeight);
                            addShape(myEllipse);
                            enableSelection(myEllipse);
                        }
                    }
                }
            } catch (IOException ex) { }
        }
    }

    /**
     * Undoes the last operation
     * @param event
     */
    @FXML
    private void undoOperation(ActionEvent event) {
        model.undoLast();
    }

    /**
     * Redoes the last undone operation
     * @param event
     */
    @FXML
    private void redoOperation(ActionEvent event) {
        model.redoLast();
    }

    /**
     * Updates the variable borderHex to match the user selection
     * @param event
     */
    @FXML
    private void selectBorderColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill(); // Reads the background color of the button that generated the event
        borderHex = (Color) selectedColor;
    }

    /**
     * Updates the variable fillHex to match the user selection
     * @param event
     */
    @FXML
    private void selectFillColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill(); // Reads the background color of the button that generated the event
        fillHex = (Color) selectedColor;
    }

    /**
     * Copies the selected shape into the clipboard and deletes it
     * @param event
     */
    @FXML
    private void cutShape(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command cutCmd = new CutCommand(model, selectedShape.get());
            model.execute(cutCmd);
        }
    }

    /**
     * Copies the selected shape into the clipboard
     * @param event
     */
    @FXML
    private void copyShape(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command copyCmd = new CopyCommand(model, selectedShape.get());
            model.execute(copyCmd);
        }
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
        if (selectedShape.get() != null) {
            Command deleteCmd = new DeleteCommand(model, selectedShape.get());
            model.execute(deleteCmd);
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
                resizeShape(selectedShape.get(), width, height);
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
     * Updates the border color of the selected shape
     * @param event 
     */
    @FXML
    private void changeBorderColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill();
        Command changeColor = new ChangeColorCommand(selectedShape.get(), null, (Color) selectedColor);
        model.execute(changeColor);
    }

    
    /**
     * Updates the fill color of the selected shape
     * @param event
     */
    @FXML
    private void changeFillColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill();
        Command changeColor = new ChangeColorCommand(selectedShape.get(), (Color) selectedColor, null);
        model.execute(changeColor);
    }

}
