
package sadprojectwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import static sadprojectwork.Shapes.ELLIPSE;
import static sadprojectwork.Shapes.LINE;
import static sadprojectwork.Shapes.RECTANGLE;

public class Model {

    private Deque<Command> commandHistory;
    private Deque<Command> deletedCommands;
    private ObservableList<MyShape> shapes = FXCollections.observableArrayList();
    private ObjectProperty<MyShape> clipboard = new SimpleObjectProperty<>();

    public Model() {
        commandHistory = new ArrayDeque<>();
        deletedCommands = new ArrayDeque<>();
    }

    /**
     * Executes the command and adds it to the command history.
     * @param command 
     */
    public void execute(Command command) {
        commandHistory.addLast(command);
        command.execute();
    }

    /**
     * Undoes the last command, removes it from the command history and adds
     * it to the deleted commands history.
     */
    public void undoLast() {
        if(!commandHistory.isEmpty()) {
            Command last = commandHistory.removeLast();
            deletedCommands.addLast(last);
            last.undo();
        }
    }

    /**
     * Redoes the last deleted commands, removes it from the deleted commands
     * history and adds it to the commands history.
     */
    public void redoLast() {
        if(!deletedCommands.isEmpty()) {
            Command last = deletedCommands.removeLast();
            commandHistory.addLast(last);
            last.execute();
        }
    }
    
    public Deque<Command> getCommandHistory() {
        return commandHistory;
    }
    
    public Deque<Command> getDeletedCommands() {
        return deletedCommands;
    }

    public void addShape(MyShape s) {
        shapes.add(s);
    }

    public void removeShape(MyShape s) {
        shapes.remove(s);
    }

    public ObservableList<MyShape> getShapes() {
        return shapes;
    }

    public void setClipboard(MyShape s) {
        clipboard.set(s);
    }

    public MyShape getClipboard() {
        return clipboard.get();
    }
    
    public ObjectProperty<MyShape> clipboardProperty() {
        return clipboard;
    }

    /**
     * Clears the entire model: shapes, command history, deleted commands and
     * clipboard.
     */
    public void clear() {
        shapes.clear();              
        commandHistory.clear();      
        deletedCommands.clear();     
        clipboard.set(null);
    }
    
    /**
     * Saves the shapes in a CSV file.
     * @param file 
     */
    public void saveDrawing(File file) {
        
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("SHAPE;STARTX;STARTY;WIDTH;HEIGHT;FILL;BORDER");
            for (MyShape shape : shapes) {
                writer.println(shape.toCSV());
            }
        } catch (IOException e) { }
        
    }
    
    /**
     * Loads and returns the shapes from a CSV file.
     * @param file
     * @return list of loaded shapes
     */
    public List<MyShape> loadDrawing(File file) {
        
        clear();
        List<MyShape> loadedShapes = new ArrayList<>();

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
                switch (loadedMode) {
                    case LINE -> {
                        BorderColorDecorator myLine = new BorderColorDecorator(new MyLine(loadedStartX, loadedStartY, loadedWidth, loadedHeight), loadedBorder);
                        loadedShapes.add(myLine);
                    }
                    case RECTANGLE -> {
                        BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(new MyRectangle(loadedStartX, loadedStartY, loadedWidth, loadedHeight), loadedFill), loadedBorder);
                        loadedShapes.add(myRectangle);
                    }
                    case ELLIPSE -> {
                        BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(new MyEllipse(loadedStartX, loadedStartY, loadedWidth, loadedHeight), loadedFill), loadedBorder);
                        loadedShapes.add(myEllipse);
                    }
                }
            }
        } catch (IOException ex) { }

        return loadedShapes;
    }
}
