
package sadprojectwork.mvc;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import sadprojectwork.shapes.MyCompositeShape;
import sadprojectwork.shapes.MyShape;

public class PaintHelper {

    // Reference to the model
    private PaintModel model;
    
    // Pane where shapes are placed
    private Pane canvas;
    
    // Nevigation: Grid
    private Slider gridSizeSlider;
    private boolean gridActive = false;
    private double gridCellsSize = 10;
    
    public PaintHelper(PaintModel model, Pane canvas, Slider gridSizeSlider) {
        
        this.model = model;
        this.canvas = canvas;
        this.gridSizeSlider = gridSizeSlider;
        
        initListeners();
    }
    
    /**
     * Initializes the listeners of the helper.
     */
    private void initListeners() {
        
        // Adds a listener to update the canvas every time the model changes
        model.getShapes().addListener((ListChangeListener<MyShape>) change -> {
            redrawCanvas();
        });
        
        // Adjusts the grid cells size while the slider is being dragged
        gridSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            gridCellsSize = newVal.doubleValue();
            redrawGrid();
        });
    }
    
    /**
     * Redraws the canvas based on the model list of shapes.
     * Called whenever the model changes.
     */
    public void redrawCanvas() {
        canvas.getChildren().clear();
        for (MyShape shapeToAdd : model.getShapes()) {
            if (shapeToAdd instanceof MyCompositeShape cs) {
                for(MyShape s : cs.getShapes()) {
                    canvas.getChildren().add(s.getFxShape());
                }
            }
            else {
                canvas.getChildren().add(shapeToAdd.getFxShape());
            }
        }
        redrawGrid();
    }
    
    /**
     * Redraws the grid.
     */
    public void redrawGrid() {

        // Clears all the lines of the grid
        canvas.getChildren().removeIf(node -> node instanceof Line && "grid".equals(node.getUserData()));

        if (!gridActive)
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
     * Activates or deactivates the grid on the drawing panel.
     * Called by PaintController.
     */
    public void toggleGrid() {
        gridActive = !gridActive;
        redrawGrid();
    }
}
