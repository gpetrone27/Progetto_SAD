
package sadprojectwork.mvc;

import sadprojectwork.shapes.MyText;
import sadprojectwork.shapes.Shapes;
import sadprojectwork.shapes.MyEllipse;
import sadprojectwork.shapes.MyPolygon;
import sadprojectwork.shapes.MyLine;
import sadprojectwork.shapes.MyShape;
import sadprojectwork.shapes.MyRectangle;
import sadprojectwork.decorator.BorderColorDecorator;
import sadprojectwork.decorator.FillColorDecorator;
import sadprojectwork.command.Command;
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
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class PaintModel {

    private Deque<Command> commandHistory;
    private Deque<Command> deletedCommands;
    private ObservableList<MyShape> shapes = FXCollections.observableArrayList();
    private ObjectProperty<MyShape> clipboard = new SimpleObjectProperty<>();

    public PaintModel() {
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
            writer.println("SHAPE;STARTX;STARTY;WIDTH;HEIGHT;FILL;BORDER;ROTATION;POINTS;TEXT;FONT;SIZE");
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
                double loadedRotation = Double.parseDouble(parts[7]);
                switch (loadedMode) {
                    case LINE -> {
                        BorderColorDecorator myLine = new BorderColorDecorator(new MyLine(loadedStartX, loadedStartY, loadedWidth, loadedHeight, loadedRotation), loadedBorder);
                        loadedShapes.add(myLine);
                    }
                    case RECTANGLE -> {
                        BorderColorDecorator myRectangle = new BorderColorDecorator(new FillColorDecorator(new MyRectangle(loadedStartX, loadedStartY, loadedWidth, loadedHeight, loadedRotation), loadedFill), loadedBorder);
                        loadedShapes.add(myRectangle);
                    }
                    case ELLIPSE -> {
                        BorderColorDecorator myEllipse = new BorderColorDecorator(new FillColorDecorator(new MyEllipse(loadedStartX, loadedStartY, loadedWidth, loadedHeight, loadedRotation), loadedFill), loadedBorder);
                        loadedShapes.add(myEllipse);
                    }
                    case POLYGON -> {
                        List<Point2D> points = new ArrayList<>();
                        String listOfPoints = parts[8]; // "x1~y1/x2~y2/.../xn~yn"
                        // Parse listOfPoints: format "x1~y1/x2~y2/.../xn~yn"
                        if (!listOfPoints.equals("null")) {
                            String[] pointPairs = listOfPoints.split("/");
                            for (String pair : pointPairs) {
                                String[] coords = pair.split("~");
                                double x = Double.parseDouble(coords[0]);
                                double y = Double.parseDouble(coords[1]);
                                points.add(new Point2D(x, y));
                            }
                        }
                        BorderColorDecorator myPolygon = new BorderColorDecorator(new FillColorDecorator(new MyPolygon(loadedStartX, loadedStartY, points, loadedRotation), loadedFill), loadedBorder);
                        loadedShapes.add(myPolygon);
                    }
                    case TEXT -> {
                        String loadedText = parts[9];
                        String loadedFont = parts[10];
                        double loadedSize = 0;
                        if (parts[11] != null) loadedSize = Double.parseDouble(parts[11]);
                        BorderColorDecorator myText = new BorderColorDecorator(new FillColorDecorator(new MyText(loadedStartX, loadedStartY,  loadedText, loadedFont,loadedSize, loadedRotation), loadedFill), loadedBorder);
                        loadedShapes.add(myText);
                    }
                }
            }
        } catch (IOException ex) { }

        return loadedShapes;
    }
}
