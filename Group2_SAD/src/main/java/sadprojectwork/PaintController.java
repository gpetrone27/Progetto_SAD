
package sadprojectwork;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
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
    private ToggleGroup borderColorPanel;
    @FXML
    private ToggleGroup fillColorPanel;
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
     * Initializes the actions of the buttons of the application.
     */
    private void initButtonActions() {
        cursorButton.setUserData(Shapes.CURSOR);
        lineButton.setUserData(Shapes.LINE);
        rectangleButton.setUserData(Shapes.RECTANGLE);
        ellipseButton.setUserData(Shapes.ELLIPSE);
    }

    /**
     * Initializes the bindings of the application.
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
        borderColorPanel.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                borderColorPanel.selectToggle(oldToggle);
            }
        });
        fillColor.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                fillColor.selectToggle(oldToggle);
            }
        });
        fillColorPanel.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                fillColorPanel.selectToggle(oldToggle);
            }
        });

        // Prevents the user from unselecting cursor or shapes
        shapes.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                shapes.selectToggle(oldToggle);
            } else {
                modeProperty.set((Shapes) newToggle.getUserData());
            }
            clearSelection();
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

        // Prevents panning with right click
        drawingPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isSecondaryButtonDown()) {
                event.consume();
            }
        });

    }

    /**
     * Initializes the icons displayed on the buttons of the application.
     */
    private void initButtonIcons() {

        // Cursor button
        FontIcon cursorIcon = new FontIcon("fa-mouse-pointer");
        cursorIcon.setIconSize(24);
        cursorIcon.setIconColor(Color.BLACK);
        cursorButton.setGraphic(cursorIcon);

    }

    /**
     * Initializes all the events related to the canvas.
     */
    private void initCanvasEvents() {

        canvas.setOnMousePressed(e -> {

            if (e.getButton() == MouseButton.PRIMARY) {

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
            }
        });

        canvas.setOnMouseDragged(e -> {

            // Moves the shape while the user is dragging it
            if (currentShape.get() == null) {

                if (selectedShape.get() != null && e.getButton() == MouseButton.PRIMARY) {

                    double dx = e.getX() - dragStartX;
                    double dy = e.getY() - dragStartY;

                    selectedShape.get().moveOf(dx, dy);

                    dragStartX = e.getX();
                    dragStartY = e.getY();

                    e.consume();
                }
            } // Preview of the shape while the user is creating it
            else {

                double endX = e.getX();
                double endY = e.getY();

                switch (modeProperty.get()) {
                    case LINE -> {
                        ((MyLine) ((BorderColorDecorator) currentShape.get()).decoratedShape).resizeTo(endX, endY);
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
     * Redraws the canvas based on the model list of shapes.
     */
    private void redrawCanvas() {
        canvas.getChildren().clear();
        for (MyShape shapeToAdd : model.getShapes()) {
            canvas.getChildren().add(shapeToAdd.getFxShape());
        }
    }

    /**
     * Enables the selection of the given shape.
     * @param shape
     */
    public void enableSelection(MyShape shape) {
        shape.getFxShape().setOnMouseClicked(event -> {
            selectedShape.set(shape);
            highlightSelected(shape);
            event.consume();
        });
    }

    /**
     * Deactivates the current selection and clears all effects.
     */
    public void clearSelection() {
        selectedShape.set(null);
        for (MyShape s : model.getShapes()) {
            s.getFxShape().setEffect(null); // Reset effects
        }
    }

    /**
     * Creates the effect shown when a shape is selected.
     * Reads the parameters of the shapes to set them in the parameters panel
     * on the right side of the screen.
     * @param shape
     */
    private void highlightSelected(MyShape shape) {

        // Removes the effect from all shapes
        for (MyShape s : model.getShapes()) {
            s.getFxShape().setEffect(null); // Reset effects
        }

        // Adds the effect to the selected shape
        DropShadow ds = new DropShadow();
        ds.setColor(Color.DODGERBLUE);
        ds.setRadius(10);
        shape.getFxShape().setEffect(ds);

        // Reads shape's parameters and sets them in the parameters panel
        Color shapeBorderColor = (Color) shape.getFxShape().getStroke();
        Color shapeFillColor = (Color) shape.getFxShape().getFill();
        for (Toggle toggle : borderColorPanel.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            Color btnColor = (Color) btn.getBackground().getFills().get(0).getFill();
            if (btnColor.equals(shapeBorderColor)) {
                borderColorPanel.selectToggle(btn);
                break;
            }
        }
        for (Toggle toggle : fillColorPanel.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            Color btnColor = (Color) btn.getBackground().getFills().get(0).getFill();
            if (btnColor.equals(shapeFillColor)) {
                fillColorPanel.selectToggle(btn);
                break;
            }
        }
    }

    /**
     * Cleares the current drawing.
     * @param event
     */
    @FXML
    private void newDrawing(ActionEvent event) {
        model.clear();
        selectedShape.set(null);
        currentShape.set(null);
    }

    /**
     * Saves the current drawing in a file.
     * @param event
     */
    @FXML
    private void saveDrawing(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save drawing as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

        if (file != null) {
            model.saveDrawing(file);
        }
    }

    /**
     * Loads a drawing from a file.
     * @param event
     */
    @FXML
    private void loadDrawing(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load drawing from CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        if (file != null) {
            List<MyShape> loadedShapes = model.loadDrawing(file);
            for(MyShape s : loadedShapes) {
                addShape(s);
                enableSelection(s);
            }
        }
    }

    /**
     * Undoes the last operation.
     * @param event
     */
    @FXML
    public void undoOperation(ActionEvent event) {
        model.undoLast();
    }

    /**
     * Redoes the last undone operation.
     * @param event
     */
    @FXML
    private void redoOperation(ActionEvent event) {
        model.redoLast();
    }

    /**
     * Updates the variable borderHex to match the user selection.
     * @param event
     */
    @FXML
    private void selectBorderColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill(); // Reads the background color of the button that generated the event
        borderHex = (Color) selectedColor;
    }

    /**
     * Updates the variable fillHex to match the user selection.
     * @param event
     */
    @FXML
    private void selectFillColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill(); // Reads the background color of the button that generated the event
        fillHex = (Color) selectedColor;
    }

    /**
     * Copies the selected shape into the clipboard and deletes it.
     * @param event
     */
    @FXML
    public void cutShape(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command cutCmd = new CutCommand(model, selectedShape.get());
            model.execute(cutCmd);
        }
    }

    /**
     * Copies the selected shape into the clipboard.
     * @param event
     */
    @FXML
    public void copyShape(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command copyCmd = new CopyCommand(model, selectedShape.get());
            model.execute(copyCmd);
        }
    }

    /**
     * Pastes a shape from the clipboard.
     * @param event
     */
    @FXML
    public void pasteShape(ActionEvent event) {
        PasteCommand pasteCmd = new PasteCommand(model, lastMouseX, lastMouseY);
        model.execute(pasteCmd);
        enableSelection(pasteCmd.getPastedShape());
    }

    /**
     * Deletes the selected shape.
     * @param event
     */
    @FXML
    public void deleteShape(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command deleteCmd = new DeleteCommand(model, selectedShape.get());
            model.execute(deleteCmd);
        }
    }

    /**
     * Resizes the selected shape according to the new dimensions.
     * @param shape
     * @param firstDim
     * @param secondDim
     */
    private void resizeShape(MyShape shape, double firstDim, double secondDim) {
        if (shape != null) {
            Command resizeCmd = new ResizeCommand(shape, firstDim, secondDim);
            model.execute(resizeCmd);
        }
    }

    /**
     * Adds a shape to the model.
     * @param shape
     */
    public void addShape(MyShape shape) {
        AddShapeCommand addCmd = new AddShapeCommand(model, shape);
        model.execute(addCmd);
    }

    /**
     * Shows a popup window when the user clicks "Resize" in the right click
     * menu.
     * @param event
     */
    @FXML
    private void showResizeWindow(ActionEvent event) {

        // Creation of the window
        Stage popupWindow = new Stage();
        popupWindow.setTitle("Resize");
        popupWindow.initModality(Modality.APPLICATION_MODAL); // Blocks inputs to other windows

        // Layout
        VBox layout;

        if (selectedShape.get().getFxShape().getClass() != Line.class) {

            // UI Elements
            TextField widthField = new TextField();
            TextField heightField = new TextField();
            Button submitButton = new Button("Submit");

            widthField.setPromptText("Width");
            heightField.setPromptText("Height");

            // Functions that is run when the user clicks the Submit button
            submitButton.setOnAction(e -> {

                try {
                    double width = Double.parseDouble(widthField.getText().trim());
                    double height = Double.parseDouble(heightField.getText().trim());
                    resizeShape(selectedShape.get(), width, height);
                    popupWindow.close();

                } catch (NumberFormatException ex) {
                }
            });

            layout = new VBox(
                    10,
                    new Label("Enter new dimensions"),
                    new HBox(5, widthField, new Label("x"), heightField),
                    submitButton
            );
        } else {
            // UI Elements
            TextField lengthField = new TextField();
            Button submitButton = new Button("Submit");

            lengthField.setPromptText("Length");

            // Functions that is run when the user clicks the Submit button
            submitButton.setOnAction(e -> {
                try {
                    resizeShape(selectedShape.get(), Double.parseDouble(lengthField.getText().trim()), 0);
                    popupWindow.close();
                } catch (NumberFormatException ex) {
                }
            });

            layout = new VBox(
                    10,
                    new Label("Enter new dimensions"),
                    lengthField,
                    submitButton
            );
        }
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);

        // Shows the window
        popupWindow.setScene(new Scene(layout, 240, 160));
        popupWindow.showAndWait();
    }

    /**
     * Updates the border color of the selected shape.
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
     * Updates the fill color of the selected shape.
     * @param event
     */
    @FXML
    private void changeFillColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill();
        Command changeColor = new ChangeColorCommand(selectedShape.get(), (Color) selectedColor, null);
        model.execute(changeColor);
    }

    public MyShape getSelectedShape() {
        return selectedShape.get();
    }
    
    public void selectShape(MyShape shape) {
        selectedShape.set(shape);
        highlightSelected(shape);
    }

    public void setCanvas(Pane canvas) {
        this.canvas = canvas;
    }

    public Model getModel() {
        return model;
    }

    public Pane getCanvas() {
        return canvas;
    }
    
}
