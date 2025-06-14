
package sadprojectwork.mvc;

import sadprojectwork.shapes.MyText;
import sadprojectwork.shapes.MyCompositeShape;
import sadprojectwork.shapes.Shapes;
import sadprojectwork.shapes.MyLine;
import sadprojectwork.shapes.MyPolygon;
import sadprojectwork.shapes.MyShape;
import sadprojectwork.decorator.BorderColorDecorator;
import sadprojectwork.decorator.FillColorDecorator;
import sadprojectwork.command.PasteCommand;
import sadprojectwork.command.DeleteCommand;
import sadprojectwork.command.CopyCommand;
import sadprojectwork.command.RotationCommand;
import sadprojectwork.command.BringToBackCommand;
import sadprojectwork.command.MoveCommand;
import sadprojectwork.command.ResizeCommand;
import sadprojectwork.command.Command;
import sadprojectwork.command.ChangeColorCommand;
import sadprojectwork.command.CutCommand;
import sadprojectwork.command.MirrorCommand;
import sadprojectwork.command.AddShapeCommand;
import sadprojectwork.command.BringToFrontCommand;
import sadprojectwork.factory.ShapeFactoryManager;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;

public class PaintController implements Initializable {

    // PaintModel reference
    private PaintModel model = new PaintModel();

    // PaintHelper reference
    private PaintHelper helper;
    
    // Colors variables
    private Color borderHex = Color.BLACK;
    private Color fillHex = Color.TRANSPARENT;

    private ObjectProperty<Shapes> modeProperty = new SimpleObjectProperty<>(Shapes.CURSOR);
    private ObjectProperty<MyShape> currentShape = new SimpleObjectProperty<>(null);
    public ListProperty<MyShape> selectedShapes = new SimpleListProperty<>(FXCollections.observableArrayList());

    private Double startX = null;
    private Double startY = null;

    private double dragStartX, dragStartY;
    private double originalX, originalY;
    private double lastMouseX, lastMouseY;
    
    // First point of the polygon
    Circle firstPoint;
    
    // Navigation: Zoom
    private double zoomBase = 1.0;
    private final double zoomStep = 0.25;
    private final double zoomMaxValue = 4.0;
    private final double zoomMinValue = 0.5;
    private Scale canvasScale = new Scale(1.0, 1.0, 0, 0);
    
    // Rotation
    private List<Double> initialRotations = new ArrayList<>();
    
    // Shape Factory
    private ShapeFactoryManager factoryManager = new ShapeFactoryManager();

    @FXML
    private AnchorPane rootPane;
    @FXML
    public ScrollPane drawingPane;
    @FXML
    private Pane canvas;
    @FXML
    private VBox sidePanel;
    @FXML
    private ToggleGroup shapes;
    @FXML
    private ToggleGroup borderColor;
    @FXML
    private ToggleGroup borderColorPanel;
    @FXML
    private ToggleGroup fillColor;
    @FXML
    private ToggleGroup fillColorPanel;
    @FXML
    private ComboBox<String> fontsComboBox;
    @FXML
    private ComboBox<String> sizeComboBox;
    @FXML
    private TextField displayTextField;
    @FXML
    private VBox defaultBorder;
    @FXML
    private VBox borderPanel;
    @FXML
    private VBox defaultFill;
    @FXML
    private VBox fillPanel;
    @FXML
    private ToggleButton noFillButton;
    @FXML
    private ToggleButton noFillButtonPanel;
    @FXML
    private Separator paletteSeparator;
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
    private MenuItem mirrorHorMenuItem;
    @FXML
    private MenuItem mirrorVerMenuItem;
    @FXML
    private MenuItem groupMenuItem;
    @FXML
    private MenuItem ungroupMenuItem;
    @FXML
    private ToggleButton gridButton;
    @FXML
    private Slider gridSizeSlider;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private VBox shapesPanel;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private CheckBox keepProportions;
    @FXML
    private VBox linePanel;
    @FXML
    private TextField lengthField;
    @FXML
    private VBox textPanel;
    @FXML
    private ComboBox<String> fontsComboBoxSide;
    @FXML
    private ComboBox<String> sizeComboBoxSide;
    @FXML
    private Slider rotationSlider;
    @FXML
    private Separator panelSeparator;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        model = new PaintModel();
        helper = new PaintHelper(model, canvas, gridSizeSlider);
        
        initButtons();
        initBindings();
        initListeners();
        initCanvasEvents();
    }

    /**
     * Initializes the actions of the buttons of the application.
     */
    private void initButtons() {
        
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
        fontsComboBoxSide.getItems().addAll(Font.getFamilies());
        
        // Available sizes
        sizeComboBox.getItems().addAll("8", "10", "12", "14", "16", "18", "24", "36", "48", "72");
        sizeComboBox.setValue("24");
        sizeComboBoxSide.getItems().addAll("8", "10", "12", "14", "16", "18", "24", "36", "48", "72");
        
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
     * Initializes the bindings of the application.
     */
    private void initBindings() {
        
        BooleanBinding selectedBinding = Bindings.createBooleanBinding(() -> selectedShapes.isEmpty(), selectedShapes);
        BooleanBinding exactlyOneSelectedBinding = Bindings.createBooleanBinding(() -> selectedShapes.size() != 1, selectedShapes);
        BooleanBinding oneSelectedBinding = Bindings.createBooleanBinding(() -> selectedShapes.size() == 1, selectedShapes);
        BooleanBinding textBinding = Bindings.createBooleanBinding(() -> selectedShapes.size() == 1 && !(selectedShapes.get(0) instanceof MyCompositeShape) && selectedShapes.get(0).getFxShape().getClass() == Text.class, selectedShapes);
        BooleanBinding notSimpleShapeBinding = Bindings.createBooleanBinding(() -> selectedShapes.size() == 1 && !(selectedShapes.get(0) instanceof MyCompositeShape) && selectedShapes.get(0).getFxShape().getClass() != Text.class && selectedShapes.get(0).getFxShape().getClass() != Line.class, selectedShapes);
        BooleanBinding compositeBinding = Bindings.createBooleanBinding(() -> selectedShapes.size() == 1 && selectedShapes.get(0) instanceof MyCompositeShape, selectedShapes);
        BooleanBinding lineBinding = Bindings.createBooleanBinding(() -> selectedShapes.size() == 1 && !(selectedShapes.get(0) instanceof MyCompositeShape) && selectedShapes.get(0).getFxShape().getClass() == Line.class, selectedShapes);
        BooleanBinding pasteBinding = Bindings.or(Bindings.createBooleanBinding(() -> model.getClipboard() == null, model.clipboardProperty()), Bindings.createBooleanBinding(() -> modeProperty.get() != Shapes.CURSOR, modeProperty));
        
        // Hides the paste operation if the clipboard is empty or if the mode is not cursor
        pasteMenuItem.disableProperty().bind(pasteBinding);
        
        // Disables the right click menu actions if no shape is selected
        cutMenuItem.disableProperty().bind(exactlyOneSelectedBinding);
        copyMenuItem.disableProperty().bind(exactlyOneSelectedBinding);
        deleteMenuItem.disableProperty().bind(selectedBinding);
        frontMenuItem.disableProperty().bind(selectedBinding);
        backMenuItem.disableProperty().bind(selectedBinding);
        mirrorHorMenuItem.disableProperty().bind(selectedBinding);
        mirrorVerMenuItem.disableProperty().bind(selectedBinding);
        groupMenuItem.disableProperty().bind(exactlyOneSelectedBinding.not());
        ungroupMenuItem.disableProperty().bind(exactlyOneSelectedBinding);

        // Hides the side panel if no shape is selected
        sidePanel.visibleProperty().bind(selectedBinding.not());
        sidePanel.managedProperty().bind(selectedBinding.not());
        
        // Hides/Shows the default/shape border color panel if a shape is selected or not
        borderPanel.visibleProperty().bind(selectedBinding.not());
        borderPanel.managedProperty().bind(selectedBinding.not());
        defaultBorder.visibleProperty().bind(selectedBinding);
        defaultBorder.managedProperty().bind(selectedBinding);
        
        // Hides/Shows the default/shape fill color panel if a shape different from a Line is selected or not
        fillPanel.visibleProperty().bind(Bindings.and(selectedBinding.not(), lineBinding.not()));
        fillPanel.managedProperty().bind(Bindings.and(selectedBinding.not(), lineBinding.not()));
        defaultFill.visibleProperty().bind(selectedBinding);
        defaultFill.managedProperty().bind(selectedBinding);
        
        // Hides/Shows the default/shape no fill button if a shape is selected or not
        noFillButtonPanel.visibleProperty().bind(Bindings.and(selectedBinding.not(), lineBinding.not()));
        noFillButtonPanel.managedProperty().bind(Bindings.and(selectedBinding.not(), lineBinding.not()));
        noFillButton.visibleProperty().bind(selectedBinding);
        noFillButton.managedProperty().bind(selectedBinding);
        
        // Hides the separator if the selected shape is a Line
        paletteSeparator.visibleProperty().bind(lineBinding.not());
        paletteSeparator.managedProperty().bind(lineBinding.not());
        
        // Hides the panel separator if multiple shapes are selected
        panelSeparator.visibleProperty().bind(oneSelectedBinding);
        panelSeparator.managedProperty().bind(oneSelectedBinding);
        
        // Hides the shapes dimensions panel if the selected shape is a Text or a Line
        shapesPanel.visibleProperty().bind(Bindings.or(compositeBinding, notSimpleShapeBinding));
        shapesPanel.managedProperty().bind(Bindings.or(compositeBinding, notSimpleShapeBinding));
        
        // Shows the text properties panel if the selected shape is a Text
        textPanel.visibleProperty().bind(textBinding);
        textPanel.managedProperty().bind(textBinding);
        
        // Shows the line properties panel if the selected shape is a Line
        linePanel.visibleProperty().bind(lineBinding);
        linePanel.managedProperty().bind(lineBinding);

    }
    
    /**
     * Initializes the listeners of the application.
     */
    private void initListeners() {
        
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
        
        // Sets text formatters to only accept numeric values in dimensions fields
        widthField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        heightField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        lengthField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));
        
        rotationSlider.setOnMousePressed(e -> {
            initialRotations.clear();
            for (MyShape s : selectedShapes) {
                initialRotations.add(s.getRotation());
            }
        });
        
        rotationSlider.setOnMouseReleased(e -> {
            for (int i = 0; i < selectedShapes.size(); i++) {
                double finalRotation = selectedShapes.get(i).getRotation();
                if (finalRotation != initialRotations.get(i)) {
                    RotationCommand cmd = new RotationCommand(selectedShapes.get(i), initialRotations.get(i), finalRotation);
                    model.execute(cmd);
                }
            }
        });
        
        // Rotates the selected shape while the slider is being dragged
        rotationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            for (MyShape s : selectedShapes) {
                s.setRotation(newVal.doubleValue());
            }
        });
        
        // Closes the active polygon if the user clicks on any part of the screen other than the drawing canvas
        Platform.runLater(() -> {
            rootPane.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                if (modeProperty.get() == Shapes.POLYGON) {
                    Point2D localClicked = drawingPane.sceneToLocal(e.getSceneX(), e.getSceneY());
                    if (!drawingPane.contains(localClicked)) {
                        closeActivePolygon();
                    }
                }
            });
            rootPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                if (modeProperty.get() == Shapes.POLYGON) {
                    closeActivePolygon();
                }
            });
        });
        
    }

    /**
     * Initializes all the events related to the canvas.
     */
    private void initCanvasEvents() {
 
        // Clips the canvas to the max dimensions
        canvas.setClip(new Rectangle(5000, 5000));
        
        // Updates the last position of the mouse, relative to the canvas, whenever it moves
        canvas.setOnMouseMoved(e -> {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        });
        
        canvas.setOnMousePressed(e -> {

            if (e.getButton() == MouseButton.PRIMARY) {

                startX = e.getX();
                startY = e.getY();

                // Saves the original coordinates when the move operation starts
                if (selectedShapes.size() == 1) {
                    dragStartX = startX;
                    dragStartY = startY;
                    originalX = selectedShapes.get(0).getStartX();
                    originalY = selectedShapes.get(0).getStartY();
                    e.consume();
                    return;
                }

                // Draws the shape if any of them is selected
                switch(modeProperty.get()) {
                    case LINE -> {
                        MyShape shape = factoryManager.createShape(Shapes.LINE, startX, startY, 0, 0, 0, false, false);
                        BorderColorDecorator myLine = new BorderColorDecorator(shape, borderHex);
                        addShape(myLine);
                        enableSelection(myLine);
                        currentShape.set(myLine);
                    }
                    case RECTANGLE -> {
                        MyShape shape = factoryManager.createShape(Shapes.RECTANGLE, startX, startY, 0, 0, 0, false, false);
                        BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(shape, fillHex), borderHex);
                        addShape(myRectangle);
                        enableSelection(myRectangle);
                        currentShape.set(myRectangle);
                    }
                    case ELLIPSE -> {
                        MyShape shape = factoryManager.createShape(Shapes.ELLIPSE, startX, startY, 0, 0, 0, false, false);
                        BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(shape, fillHex), borderHex);
                        addShape(myEllipse);
                        enableSelection(myEllipse);
                        currentShape.set(myEllipse);
                    }
                    case POLYGON -> {
                        if (currentShape.get() == null) {
                            MyShape shape = factoryManager.createShape(Shapes.POLYGON, startX, startY, 0, 0, 0, false, false);
                            BorderColorDecorator myPolygon = new BorderColorDecorator(new FillColorDecorator(shape, fillHex), borderHex);
                            addShape(myPolygon);
                            enableSelection(myPolygon);
                            currentShape.set(myPolygon);
                            firstPoint = new Circle(startX, startY, 5);
                            firstPoint.setFill(Color.BLACK);
                            DropShadow ds = new DropShadow();
                            ds.setColor(Color.DODGERBLUE);
                            ds.setRadius(10);
                            firstPoint.setEffect(ds);
                            canvas.getChildren().add(firstPoint);
                        }
                        else {
                            MyPolygon myPolygon = (MyPolygon) ((FillColorDecorator) ((BorderColorDecorator) currentShape.get()).getDecoratedShape()).getDecoratedShape();
                            boolean isPolygonClosed = myPolygon.addPoint(new Point2D(startX, startY));
                            if (isPolygonClosed) {
                                currentShape.set(null);
                                canvas.getChildren().remove(firstPoint);
                            }
                        }
                    }
                    case TEXT -> {
                        if (!displayTextField.getText().equals("")) {
                            MyShape shape = factoryManager.createShape(Shapes.TEXT, startX, startY, 0, 0, 0, false, false);
                            if (shape instanceof MyText myText) {
                                myText.setFontFamily(fontsComboBox.getValue());
                                myText.setSize(Double.parseDouble(sizeComboBox.getValue()));
                                myText.setText(displayTextField.getText());
                            }
                            BorderColorDecorator myText = new BorderColorDecorator(new FillColorDecorator(shape, fillHex), borderHex);
                            addShape(myText);
                            enableSelection(myText);
                        }
                    }
                }
            }
        });

        canvas.setOnMouseDragged(e -> {

            // Moves the shape while the user is dragging it
            if (currentShape.get() == null) {

                if (selectedShapes.size() == 1 && e.getButton() == MouseButton.PRIMARY) {

                    double dx = e.getX() - dragStartX;
                    double dy = e.getY() - dragStartY;

                    selectedShapes.get(0).moveOf(dx, dy);

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
            if (selectedShapes.size() == 1 && currentShape.get() == null) {
                double newX = selectedShapes.get(0).getStartX();
                double newY = selectedShapes.get(0).getStartY();
                if (newX != originalX || newY != originalY) {
                    Command moveCmd = new MoveCommand(selectedShapes.get(0), originalX, originalY, newX, newY);
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
     * Finalizes the creation of the active polygon.
     */
    private void closeActivePolygon() {
        if (currentShape.get() != null && currentShape.get().getFxShape().getClass() == Polyline.class) {
            MyPolygon myPolygon = (MyPolygon) ((FillColorDecorator) ((BorderColorDecorator) currentShape.get()).getDecoratedShape()).getDecoratedShape();
            myPolygon.closePolygon();
            currentShape.set(null);
            canvas.getChildren().remove(firstPoint);
        }
    }
    
    /**
     * Enables the selection of the given shape.
     * @param shape
     */
    public void enableSelection(MyShape shape) {
        
        if (shape instanceof MyCompositeShape cs) {
            for (MyShape s : cs.getShapes()) {
                s.getFxShape().setOnMouseClicked(event -> {
                    if (modeProperty.get() == Shapes.CURSOR) {
                        selectedShapes.setAll(cs);
                        highlightSelected();
                    }
                    event.consume();
                });
            }
        }
        else {
            shape.getFxShape().setOnMouseClicked(event -> {
                if (modeProperty.get() == Shapes.CURSOR) {
                    if (event.getButton() != MouseButton.SECONDARY || selectedShapes.size() <= 1) {
                        
                        // Checks if a group is selected
                        boolean containsGroup = false;
                        for (MyShape s : selectedShapes) {
                            if (s instanceof MyCompositeShape) {
                                containsGroup = true;
                            }
                        }

                        if (!containsGroup) {
                            if (event.isControlDown()) {
                                if (selectedShapes.contains(shape)) {
                                    selectedShapes.remove(shape);
                                }
                                else {
                                    selectedShapes.add(shape);
                                }
                            }
                            else {
                                selectedShapes.setAll(shape);
                            }  
                        }
                        else {
                            selectedShapes.setAll(shape);
                        }
                    }
                    highlightSelected();  
                }
                event.consume();
            }); 
        }
    }

    /**
     * Deactivates the current selection and clears all effects.
     */
    public void clearSelection() {
        selectedShapes.clear();
        for (MyShape s : model.getShapes()) {
            if (s instanceof MyCompositeShape cs) {
                for (MyShape shape : cs.getShapes()) {
                    shape.getFxShape().setEffect(null);
                }
            }
            else if (s != null) {
                s.getFxShape().setEffect(null);  
            }
        }
    }

    /**
     * Creates the effect shown when a shape is selected.
     * Reads the parameters of the shapes to set them in the parameters panel
     * on the right side of the screen.
     * @param shape
     */
    private void highlightSelected() {

        // Removes the effect from all shapes
        for (MyShape s : model.getShapes()) {
            if (s instanceof MyCompositeShape cs) {
                for (MyShape shape : cs.getShapes()) {
                    shape.getFxShape().setEffect(null);
                }
            }
            else if (s != null) {
                s.getFxShape().setEffect(null);  
            }
        }

        // Adds the effect to the selected shapes
        for (MyShape s : selectedShapes) {
            if (s instanceof MyCompositeShape cs) {
                for (MyShape shape : cs.getShapes()) {
                    shape.getFxShape().setEffect(new DropShadow(10, Color.DODGERBLUE));
                }
            }
            else if (s != null) {
                s.getFxShape().setEffect(new DropShadow(10, Color.DODGERBLUE));
            }
        }
        
        if (selectedShapes.size() == 1) {
            
            MyShape shape = selectedShapes.get(0);
            
            if (shape instanceof MyCompositeShape) {
                // TO DO: Choose how to select fill and border colors
            }
            else {
                
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
                if (selectedShapes.get(0).getFxShape().getFill() == Color.TRANSPARENT) {
                    fillColorPanel.selectToggle(noFillButtonPanel);
                } 
                
                if (selectedShapes.get(0).getFxShape().getClass() == Text.class) {

                    MyText selectedText = (MyText) ((FillColorDecorator) ((BorderColorDecorator) selectedShapes.get(0)).getDecoratedShape()).getDecoratedShape();

                    // Font
                    fontsComboBoxSide.setValue(selectedText.getFontFamily());

                    // Size
                    sizeComboBoxSide.setValue(Integer.toString((int) selectedText.getSize()));
                }

                if (selectedShapes.get(0).getFxShape().getClass() == Line.class) {

                    MyLine selectedLine = (MyLine) ((BorderColorDecorator) selectedShapes.get(0)).getDecoratedShape();

                    // Length
                    lengthField.setText(Double.toString(Math.round(selectedLine.getLength() * 100.0) / 100.0));
                }
            }

            // Width and height fields
            widthField.setText(Double.toString(Math.round(shape.getWidth() * 100.0) / 100.0));
            heightField.setText(Double.toString(Math.round(shape.getHeight() * 100.0) / 100.0));

            // Rotation
            rotationSlider.setValue(selectedShapes.get(0).getRotation());
        }
    }

    /**
     * Cleares the current drawing.
     * @param event
     */
    @FXML
    private void newDrawing(ActionEvent event) {
        model.clear();
        selectedShapes.clear();
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
        fileChooser.setInitialDirectory(new File("src/main/resources/drawings"));
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
        fileChooser.setInitialDirectory(new File("src/main/resources/drawings"));
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
        for (MyShape s : selectedShapes) {
            if (s instanceof MyCompositeShape cs) {
                for (MyShape shape : cs.getShapes()) {
                    Command changeColor = new ChangeColorCommand(shape, null, (Color) selectedColor);
                    model.execute(changeColor);       
                }
            }
            else {
                Command changeColor = new ChangeColorCommand(s, null, (Color) selectedColor);
                model.execute(changeColor);
            }
        }
    }

    /**
     * Updates the fill color of the selected shape.
     * @param event
     */
    @FXML
    private void changeFillColor(ActionEvent event) {
        ToggleButton colorButton = (ToggleButton) event.getSource();
        Paint selectedColor = colorButton.getBackground().getFills().get(0).getFill();
        for (MyShape s : selectedShapes) {            
            if (s instanceof MyCompositeShape cs) {
                for (MyShape shape : cs.getShapes()) {
            Command changeColor = new ChangeColorCommand(shape, (Color) selectedColor, null);
                    model.execute(changeColor);       
                }
            }
            else {
                Command changeColor = new ChangeColorCommand(s, (Color) selectedColor, null);
                model.execute(changeColor); 
            }
        }
    }

    @FXML
    private void changeToNoFill(ActionEvent event) {
        for (MyShape s : selectedShapes) {            
            if (s instanceof MyCompositeShape cs) {
                for (MyShape shape : cs.getShapes()) {
                    Command changeColor = new ChangeColorCommand(shape, Color.TRANSPARENT, null);
                    model.execute(changeColor);        
                }
            }
            else {
                Command changeColor = new ChangeColorCommand(s, Color.TRANSPARENT, null);
                model.execute(changeColor);  
            }
        }
    }

    /**
     * Copies the selected shape into the clipboard and deletes it.
     * @param event
     */
    @FXML
    public void cutShape(ActionEvent event) {
        if (selectedShapes.size() == 1) {
            Command cutCmd = new CutCommand(model, selectedShapes.get(0));
            model.execute(cutCmd);
            selectedShapes.clear();
        }
    }

    /**
     * Copies the selected shape into the clipboard.
     * @param event
     */
    @FXML
    public void copyShape(ActionEvent event) {
        if (selectedShapes.size() == 1) {
            Command copyCmd = new CopyCommand(model, selectedShapes.get(0));
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
        MyShape pastedShape = pasteCmd.getPastedShape();
        enableSelection(pastedShape);
        selectShape(pastedShape);
    }

    /**
     * Deletes the selected shape.
     * @param event
     */
    @FXML
    public void deleteShape(ActionEvent event) {
        if (!selectedShapes.isEmpty()) {
            for (MyShape s : selectedShapes) {
                Command deleteCmd = new DeleteCommand(model, s);
                model.execute(deleteCmd);
            }
            selectedShapes.clear();
        }
    }
    
    /**
     * Brings a shape to the front.
     * @param event 
     */
    @FXML
    public void bringToFront(ActionEvent event) {
        if (!selectedShapes.isEmpty()) {
            for (MyShape s : selectedShapes) {
                Command brngFrntCmd = new BringToFrontCommand(model, s); 
                model.execute(brngFrntCmd);
            }
        }
    }

    /**
     * Brings a shape to the back.
     * @param event 
     */
    @FXML
    public void bringToBack(ActionEvent event) {
        if (!selectedShapes.isEmpty()) {
            for (MyShape s : selectedShapes) {
                Command brngBckCmd = new BringToBackCommand(model, s); 
                model.execute(brngBckCmd);
            }
        }
    }
    
    /**
     * Resizes the selected shape when the width field is changed.
     * @param event 
     */
    @FXML
    private void resizeWidth(ActionEvent event) {
        
        if (selectedShapes.size() == 1) {
            
            Command resizeCmd;

            double oldWidth = selectedShapes.get(0).getWidth();
            double oldHeight = selectedShapes.get(0).getHeight();

            double aspectRatio = oldWidth / oldHeight;

            double newWidth = Double.parseDouble(widthField.getText());
            double newHeight = oldHeight;

            if (keepProportions.isSelected()) {
                newHeight = newWidth / aspectRatio;
            }

            resizeCmd = new ResizeCommand(selectedShapes.get(0), newWidth, newHeight);
            model.execute(resizeCmd);

            // Update width and height fields
            widthField.setText(Double.toString(Math.round(selectedShapes.get(0).getWidth() * 100.0) / 100.0));
            heightField.setText(Double.toString(Math.round(selectedShapes.get(0).getHeight() * 100.0) / 100.0));
        }
    }

    /**
     * Resizes the selected shape when the height field is changed.
     * @param event 
     */
    @FXML
    private void resizeHeight(ActionEvent event) {
        
        if (selectedShapes.size() == 1) {
            
            Command resizeCmd;

            double oldWidth = selectedShapes.get(0).getWidth();
            double oldHeight = selectedShapes.get(0).getHeight();

            double aspectRatio = oldWidth / oldHeight;

            double newWidth = oldWidth;
            double newHeight = Double.parseDouble(heightField.getText());

            if (keepProportions.isSelected()) {
                newWidth = newHeight * aspectRatio;
            }

            resizeCmd = new ResizeCommand(selectedShapes.get(0), newWidth, newHeight);
            model.execute(resizeCmd);

            // Update width and height fields
            widthField.setText(Double.toString(Math.round(selectedShapes.get(0).getWidth() * 100.0) / 100.0));
            heightField.setText(Double.toString(Math.round(selectedShapes.get(0).getHeight() * 100.0) / 100.0));
        }
    }

    /**
     * Resizes the selected line when the length field is changed.
     * @param event 
     */
    @FXML
    private void resizeLength(ActionEvent event) {
        
        if (selectedShapes.size() == 1) {
            
            // Resizes the selected line with the chosen length
            MyLine selectedLine = (MyLine) ((BorderColorDecorator) selectedShapes.get(0)).getDecoratedShape();
            selectedLine.resizeLength(Double.parseDouble(lengthField.getText()));

            // Update length field
            lengthField.setText(Double.toString(Math.round(selectedLine.getLength() * 100.0) / 100.0));
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
    * Activates or deactivates the grid on the drawing panel.
    * @param event 
    */
    @FXML
    private void toggleGrid(ActionEvent event) {
        helper.toggleGrid();
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
     * Updates the font family of the selected text.
     * @param event 
     */
    @FXML
    private void changeFont(ActionEvent event) {
        if (selectedShapes.size() == 1) {
            MyText selectedText = (MyText) ((FillColorDecorator) (((BorderColorDecorator) selectedShapes.get(0)).getDecoratedShape())).getDecoratedShape();
            selectedText.setFontFamily(fontsComboBoxSide.getValue());
        }
    }

    /**
     * Updates the size of the selected text.
     * @param event 
     */
    @FXML
    private void changeSize(ActionEvent event) {
        if (selectedShapes.size() == 1) {
            MyText selectedText = (MyText) ((FillColorDecorator) (((BorderColorDecorator) selectedShapes.get(0)).getDecoratedShape())).getDecoratedShape();
            selectedText.setSize(Double.parseDouble(sizeComboBoxSide.getValue()));
        }
    }

    /**
     * Mirrors the selected shapes horizontally.
     * @param event
     */
    @FXML
    public void mirrorHorizontally(ActionEvent event) {
        for (MyShape s : selectedShapes) {
            Command mirrHCmd = new MirrorCommand(s, true);
            model.execute(mirrHCmd);
        }
    }

    /**
     * Mirrors the selected shapes vertically.
     * @param event
     */
    @FXML
    public void mirrorVertically(ActionEvent event) {
        for (MyShape s : selectedShapes) {
            Command mirrVCmd = new MirrorCommand(s, false);
            model.execute(mirrVCmd);
        }
    }

    /**
     * Groups the selected shapes into a single MyCompositeShape,
     * preserving z-index order and updating the selection.
     * @param event 
     */
    @FXML
    private void groupSelected(ActionEvent event) {
        if(selectedShapes.size() > 1) {

            // Sorts the shapes according to their z-index
            List<MyShape> ordered = new ArrayList<>(selectedShapes);
            ordered.sort(Comparator.comparingInt(shape -> model.getShapes().indexOf(shape)));
            // Z-index of the first shape in the ordered list
            int insertIndex = model.getShapes().indexOf(ordered.get(0));
        
            MyCompositeShape group = new MyCompositeShape(0, 0);
            for (MyShape shape : ordered) {
                group.addShape(shape);
                model.removeShape(shape);
            }    
        
            // Adds the composite shape in the z-index 
            model.getShapes().add(insertIndex, group);
            enableSelection(group);
            selectedShapes.setAll(group);
        }
    }
    
    /**
    * Ungroups the selected MyCompositeShape, restoring its individual shapes
    * to the model and updating the selection.
    * @param event 
    */
    @FXML
    private void ungroupSelected(ActionEvent event) {
        if (selectedShapes.size() == 1 && selectedShapes.get(0) instanceof MyCompositeShape group) {
            selectedShapes.clear();
            model.removeShape(group);
            for (MyShape shape : group.getShapes()) {
                model.addShape(shape);
                enableSelection(shape);
                selectedShapes.add(shape);
            }
            group.clear();
        }
    }

    public MyShape getSelectedShape() {
        if (selectedShapes.size() == 1) {
            return selectedShapes.get(0); 
        }
        else {
            return null;
        }
    }
    
    public void selectShape(MyShape shape) {
        selectedShapes.setAll(shape);
        highlightSelected();
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