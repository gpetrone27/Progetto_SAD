
package sadprojectwork;

import command.*;
import decorator.*;
import shapes.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

public class PaintController implements Initializable {

    // PaintModel reference
    private PaintModel model = new PaintModel();

    // Colors variables
    private Color borderHex = Color.BLACK;
    private Color fillHex = Color.TRANSPARENT;

    private ObjectProperty<Shapes> modeProperty = new SimpleObjectProperty<>(Shapes.CURSOR);
    private ObjectProperty<MyShape> currentShape = new SimpleObjectProperty<>(null);
    private ObjectProperty<MyShape> selectedShape = new SimpleObjectProperty<>(null);

    private Double startX = null;
    private Double startY = null;

    private double dragStartX, dragStartY;
    private double originalX, originalY;

    private double lastMouseX, lastMouseY; // Last registered mouse coordinates
    
    private double zoomBase = 1.0;
    private final double zoomStep = 0.25;
    private final double zoomMaxValue = 4.0;
    private final double zoomMinValue = 0.5;
    private Scale canvasScale = new Scale(1.0, 1.0, 0, 0);
    
    private boolean gridActived = false;
    private double gridCellsSize = 10;
    
    private String displayText = "";
    private String fontFamily = "Arial";
    private double textSize = 12;
    
    private double initialRotation;
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    public ScrollPane drawingPane;
    @FXML
    private Pane canvas;
    @FXML
    private ToggleGroup shapes;
    @FXML
    private ToggleGroup borderColor;
    @FXML
    private ToggleGroup fillColor;
    @FXML
    private ToggleButton noFillButton;
    @FXML
    private ToggleGroup fillColorPanel;
    @FXML
    private ToggleButton noFillButtonPanel;
    @FXML
    private ToggleGroup borderColorPanel;
    @FXML
    private ToggleButton cursorButton;
    @FXML
    private ToggleButton lineButton;
    @FXML
    private ToggleButton rectangleButton;
    @FXML
    private ToggleButton ellipseButton;
    @FXML
    private ToggleButton polygonButton;
    @FXML
    private ToggleButton textButton;
    @FXML
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem deleteMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private MenuItem frontMenuItem;
    @FXML
    private MenuItem backMenuItem;
    @FXML
    private VBox propertiesPanel;
    @FXML
    private TitledPane borderPanel;
    @FXML
    private TitledPane fillPanel;
    @FXML
    private TitledPane positionPanel;
    @FXML
    private ToggleButton gridButton;
    @FXML
    private Slider gridSizeSlider;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private Slider rotationSlider;
    @FXML
    private ComboBox<String> fontsComboBox;
    @FXML
    private ComboBox<String> sizeComboBox;
    @FXML
    private TextField displayTextField;

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
        
        // Sets the user data of shapes buttons
        cursorButton.setUserData(Shapes.CURSOR);
        lineButton.setUserData(Shapes.LINE);
        rectangleButton.setUserData(Shapes.RECTANGLE);
        ellipseButton.setUserData(Shapes.ELLIPSE);
        polygonButton.setUserData(Shapes.POLYGON);
        textButton.setUserData(Shapes.TEXT);
        
        // Available fonts
        fontsComboBox.getItems().addAll(Font.getFamilies());
        fontsComboBox.setValue("Arial");
        
        // Available sizes
        sizeComboBox.getItems().addAll("8", "10", "12", "14", "16", "18", "24", "36", "48", "72");
        sizeComboBox.setValue("12");
        
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
        frontMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() == null, selectedShape));
        backMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> selectedShape.get() == null, selectedShape));

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
        
        // Closes a Polygon when the user unselect MyPolygon mode
        modeProperty.addListener((obs, oldMode, newMode) -> {
            if (oldMode == Shapes.POLYGON && currentShape.get() != null) {
                MyPolygon myPolygon = (MyPolygon) ((FillColorDecorator) ((BorderColorDecorator) currentShape.get()).getDecoratedShape()).getDecoratedShape();
                myPolygon.closeShape(); 
                currentShape.set(null);
            }
        });


        // Prevents panning with right click
        drawingPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.isSecondaryButtonDown()) {
                event.consume();
            }
        });
        
        // Hides the fill color section in the properties panel if line is selected
        fillPanel.visibleProperty().bind(Bindings.createBooleanBinding(
            () -> {
                return selectedShape.get() != null && selectedShape.get().getFxShape().getClass() != Line.class;
            },
            selectedShape
        ));
        fillPanel.managedProperty().bind(Bindings.createBooleanBinding(
            () -> {
                return selectedShape.get() != null && selectedShape.get().getFxShape().getClass() != Line.class;
            },
            selectedShape
        ));
        
        // Sets text formatters to only accept numeric values in width and height fields
        widthField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        heightField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        
        rotationSlider.setOnMousePressed(e -> {
            if(selectedShape != null){
                initialRotation = selectedShape.get().getRotation();
            }
        });
        
        rotationSlider.setOnMouseReleased(e -> {
            if(selectedShape != null){
                double finalRotation = selectedShape.get().getRotation();
                if(finalRotation != initialRotation){
                    RotationCommand cmd = new RotationCommand(selectedShape.get(), initialRotation, finalRotation);
                    model.execute(cmd);
                }
            }
        });
        
        // Rotates the selected shape while the slider is being dragged
        rotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (selectedShape != null) {
                selectedShape.get().setRotation(newVal.doubleValue());
            }
        });
        
        // Adjusts the grid cells size while the slider is being dragged
        gridSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            gridCellsSize = newVal.doubleValue();
            redrawGrid();
        });
        
    }

    /**
     * Initializes the icons displayed on the buttons of the application.
     */
    private void initButtonIcons() {

        // Cursor button
        FontIcon cursorIcon = new FontIcon("fas-mouse-pointer");
        cursorIcon.setIconSize(28);
        cursorIcon.setIconColor(Color.BLACK);
        cursorButton.setGraphic(cursorIcon);
        
        // Line button
        FontIcon lineIcon = new FontIcon("fas-slash");
        lineIcon.setIconSize(20);
        lineIcon.setIconColor(Color.BLACK);
        lineButton.setGraphic(lineIcon);
        
        // Rectangle button
        FontIcon rectangleIcon = new FontIcon("far-square");
        rectangleIcon.setIconSize(20);
        rectangleIcon.setIconColor(Color.BLACK);
        rectangleButton.setGraphic(rectangleIcon);
        
        // Ellipse button
        FontIcon ellipseIcon = new FontIcon("far-circle");
        ellipseIcon.setIconSize(20);
        ellipseIcon.setIconColor(Color.BLACK);
        ellipseButton.setGraphic(ellipseIcon);
        
        // Polygon button
        FontIcon polygonIcon = new FontIcon("fas-draw-polygon"); // or "bi-pencil-square"
        polygonIcon.setIconSize(20);
        polygonIcon.setIconColor(Color.BLACK);
        polygonButton.setGraphic(polygonIcon);
        
        // Grid button
        FontIcon gridIcon = new FontIcon("bi-grid-3x3");
        gridIcon.setIconSize(24);
        gridIcon.setIconColor(Color.BLACK);
        gridButton.setGraphic(gridIcon);
        
        // Zoom in button
        FontIcon zoomInIcon = new FontIcon("bi-zoom-in");
        zoomInIcon.setIconSize(16);
        zoomInIcon.setIconColor(Color.BLACK);
        zoomInButton.setGraphic(zoomInIcon);
        
        // Zoom out button
        FontIcon zoomOutIcon = new FontIcon("bi-zoom-out");
        zoomOutIcon.setIconSize(16);
        zoomOutIcon.setIconColor(Color.BLACK);
        zoomOutButton.setGraphic(zoomOutIcon);
        
        // No Fill button
        FontIcon noFillIcon = new FontIcon("fas-ban");
        noFillIcon.setIconSize(16);
        noFillIcon.setIconColor(Color.RED);
        noFillButton.setGraphic(noFillIcon);

        // No Fill Panel button
        FontIcon noFillPanelIcon = new FontIcon("fas-ban");
        noFillPanelIcon.setIconSize(16);
        noFillPanelIcon.setIconColor(Color.RED);
        noFillButtonPanel.setGraphic(noFillPanelIcon);
        
        // Text button
        FontIcon textIcon = new FontIcon("fas-font");
        textIcon.setIconSize(20);
        textIcon.setIconColor(Color.BLACK);
        textButton.setGraphic(textIcon);
        
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
                        BorderColorDecorator myLine = new BorderColorDecorator(new MyLine(startX, startY, 0, 0, 0), borderHex);
                        addShape(myLine);
                        enableSelection(myLine);
                        currentShape.set(myLine);
                    }
                    case RECTANGLE -> {
                        BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(new MyRectangle(startX, startY, 0, 0, 0), fillHex), borderHex);
                        addShape(myRectangle);
                        enableSelection(myRectangle);
                        currentShape.set(myRectangle);
                    }
                    case ELLIPSE -> {
                        BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(new MyEllipse(startX, startY, 0, 0, 0), fillHex), borderHex);
                        addShape(myEllipse);
                        enableSelection(myEllipse);
                        currentShape.set(myEllipse);
                    }
                    case POLYGON -> {
                        if (currentShape.get() == null) {
                            BorderColorDecorator myPolygon = new BorderColorDecorator(new FillColorDecorator(new MyPolygon(startX, startY, null, 0), fillHex), borderHex);
                            addShape(myPolygon);
                            enableSelection(myPolygon);
                            currentShape.set(myPolygon);
                        }
                        else {
                            MyPolygon myPolygon = (MyPolygon) ((FillColorDecorator) ((BorderColorDecorator) currentShape.get()).getDecoratedShape()).getDecoratedShape();
                            boolean isPolygonClosed = myPolygon.addLineTo(startX, startY);
                            if (isPolygonClosed) {
                                currentShape.set(null);
                            }
                        }
                    }
                    case TEXT -> {
                        BorderColorDecorator myText = new BorderColorDecorator(new FillColorDecorator(new MyText(startX, startY, displayTextField.getText(),fontsComboBox.getValue(), Double.parseDouble(sizeComboBox.getValue()), 0), fillHex), borderHex);
                        addShape(myText);
                        enableSelection(myText);
                        currentShape.set(myText);
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
            }
            
            // Preview of the shape while the user is creating it
            else {
                if (modeProperty.get() != Shapes.POLYGON && modeProperty.get() != Shapes.TEXT) {
                    currentShape.get().resize(e.getX() - startX, e.getY() - startY); 
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
            if (currentShape.get() != null && modeProperty.get() != Shapes.POLYGON) {
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
        redrawGrid();
    }

    /**
     * Enables the selection of the given shape.
     * @param shape
     */
    public void enableSelection(MyShape shape) {
        shape.getFxShape().setOnMouseClicked(event -> {
            if (modeProperty.get() == Shapes.CURSOR) {
                selectedShape.set(shape);
                highlightSelected(shape);  
            }
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
        
        // Border color
        Color shapeBorderColor = (Color) shape.getFxShape().getStroke();
        for (Toggle toggle : borderColorPanel.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            Color btnColor = (Color) btn.getBackground().getFills().get(0).getFill();
            if (btnColor.equals(shapeBorderColor)) {
                borderColorPanel.selectToggle(btn);
                break;
            }
        }
        
        // Fill color
        Color shapeFillColor = (Color) shape.getFxShape().getFill();
        for (Toggle toggle : fillColorPanel.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            Color btnColor = (Color) btn.getBackground().getFills().get(0).getFill();
            if (btnColor.equals(shapeFillColor)) {
                fillColorPanel.selectToggle(btn);
                break;
            }
        }
        if (selectedShape.get().getFxShape().getFill() == Color.TRANSPARENT) {
            fillColorPanel.selectToggle(noFillButtonPanel);
        }
        
        // Width and height fields
        widthField.setText(Double.toString(shape.getWidth()));
        heightField.setText(Double.toString(shape.getHeight()));
        
        // Rotation
        rotationSlider.setValue(selectedShape.get().getRotation());
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
     * Sets the fill color to transparent.
     * @param event 
     */
    @FXML
    private void selectNoFill(ActionEvent event) {
        fillHex = Color.TRANSPARENT;
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

    @FXML
    private void changeToNoFill(ActionEvent event) {
        Command changeColor = new ChangeColorCommand(selectedShape.get(), Color.TRANSPARENT, null);
        model.execute(changeColor);
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
     * Brings a shape to the front.
     * @param event 
     */
    @FXML
    public void bringToFront(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command brngFrntCmd = new BringToFrontCommand(model, selectedShape.get());
            model.execute(brngFrntCmd);
        }
    }

    /**
     * Brings a shape to the back.
     * @param event 
     */
    @FXML
    public void bringToBack(ActionEvent event) {
        if (selectedShape.get() != null) {
            Command brngBckCmd = new BringToBackCommand(model, selectedShape.get()); 
            model.execute(brngBckCmd);
        }
    }
    
    /**
     * Resizes the selected shape when the width field is changed.
     * @param event 
     */
    @FXML
    private void resizeWidth(ActionEvent event) {
        Command resizeCmd = new ResizeCommand(selectedShape.get(), Double.parseDouble(widthField.getText()), selectedShape.get().getHeight());
        model.execute(resizeCmd);
    }

    /**
     * Resized the selected shape when the height field is changed.
     * @param event 
     */
    @FXML
    private void resizeHeight(ActionEvent event) {
        Command resizeCmd = new ResizeCommand(selectedShape.get(), selectedShape.get().getWidth(), Double.parseDouble(heightField.getText()));
        model.execute(resizeCmd);
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
    * Activates or deactivates the grid on the drawing panel.
    * @param event 
    */
    @FXML
    private void toggleGrid(ActionEvent event) {
        gridActived = !gridActived;
        redrawGrid();
    }
    
    /**
     * Redraws the grid.
     */
    private void redrawGrid() {

        // Clears all the lines of the grid
        canvas.getChildren().removeIf(node -> node instanceof Line && "grid".equals(node.getUserData()));

        if (!gridActived)
            return;

        double width = canvas.getWidth();
        double height = canvas.getHeight();

        List<Node> gridLines = new ArrayList<>();

        for (double x = 0; x < width; x += gridCellsSize) {
            Line verticalLine = new Line(x, 0, x, height);
            verticalLine.setStroke(Color.LIGHTGRAY);
            verticalLine.setStrokeWidth(0.5);
            verticalLine.setUserData("grid");
            gridLines.add(verticalLine);
        }

        for (double y = 0; y < height; y += gridCellsSize) {
            Line horizontalLine = new Line(0, y, width, y);
            horizontalLine.setStroke(Color.LIGHTGRAY);
            horizontalLine.setStrokeWidth(0.5);
            horizontalLine.setUserData("grid");
            gridLines.add(horizontalLine);
        }

        canvas.getChildren().addAll(0, gridLines);
        
    }

    /**
     * Zooms in the canvas, scaling it up.
     * @param event 
     */
    @FXML
    private void zoomIn(ActionEvent event) {
        if (zoomBase + zoomStep <= zoomMaxValue) {
            zoomBase += zoomStep;
            canvas.getTransforms().remove(canvasScale);
            canvasScale = new Scale(zoomBase, zoomBase, 0, 0);
            canvas.getTransforms().add(canvasScale);
        }
    }

    /**
     * Zooms out the canvas, scaling it down.
     * @param event 
     */
    @FXML
    private void zoomOut(ActionEvent event) {
        if (zoomBase - zoomStep >= zoomMinValue) {
            zoomBase -= zoomStep;
            canvas.getTransforms().remove(canvasScale);
            canvasScale = new Scale(zoomBase, zoomBase, 0, 0);
            canvas.getTransforms().add(canvasScale);
        }
    }

    /**
     * Updates the font family variable to match user selection.
     * @param event 
     */
    @FXML
    private void selectFont(ActionEvent event) {
        fontFamily = fontsComboBox.getValue();
    }

    /**
     * Updates the text size variable to match user selection.
     * @param event 
     */
    @FXML
    private void selectSize(ActionEvent event) {
        textSize = Double.parseDouble(sizeComboBox.getValue());
    }

    /**
     * Updates the text to write to match user selection.
     * @param event 
     */
    @FXML
    private void selectDisplayText(ActionEvent event) {
        displayText = displayTextField.getText();
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

    public PaintModel getModel() {
        return model;
    }

    public Pane getCanvas() {
        return canvas;
    }
    
}
