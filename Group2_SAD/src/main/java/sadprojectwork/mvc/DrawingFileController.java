package sadprojectwork.mvc;

import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.scene.layout.AnchorPane;
import sadprojectwork.shapes.MyShape;

public class DrawingFileController {

    private PaintModel model;
    private AnchorPane rootPane; // Serve per aprire finestre dialog (FileChooser)
    private PaintController mainController;  // riferimento al controller principale

    public DrawingFileController(PaintModel model, AnchorPane rootPane, PaintController mainController) {
        this.model = model;
        this.rootPane = rootPane;
        this.mainController = mainController;
    }

    public void newDrawing() {
        model.clear();
    }

    public void saveDrawing() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save drawing as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File("src/main/resources/drawings"));
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

        if (file != null) {
            model.saveDrawing(file);
        }
    }

    public void loadDrawing() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load drawing from CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialDirectory(new File("src/main/resources/drawings"));
        File file = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        if (file != null) {
            List<MyShape> loadedShapes = model.loadDrawing(file);
            // Chiama addShape nel controller principale per ogni forma
            for (MyShape s : loadedShapes) {
                mainController.addShape(s);
                mainController.enableSelection(s); // se vuoi abilitare la selezione come prima
            }
        }
    }
}
