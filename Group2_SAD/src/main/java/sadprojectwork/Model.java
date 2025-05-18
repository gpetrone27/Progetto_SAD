
package sadprojectwork;

import java.util.ArrayDeque;
import java.util.Deque;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {

    private Deque<Command> commandHistory;
    private Deque<Command> deletedCommands;
    private ObservableList<MyShape> shapes = FXCollections.observableArrayList();
    private ObjectProperty<MyShape> clipboard = new SimpleObjectProperty<>();

    public Model() {
        commandHistory = new ArrayDeque<>();
        deletedCommands = new ArrayDeque<>();
    }

    public void execute(Command command) {
        commandHistory.addLast(command);
        command.execute();
    }

    public void undoLast() {
        if(!commandHistory.isEmpty()) {
            Command last = commandHistory.removeLast();
            deletedCommands.addLast(last);
            last.undo();
        }
    }

    public void redoLast() {
        if(!deletedCommands.isEmpty()) {
            Command last = deletedCommands.removeLast();
            commandHistory.addLast(last);
            last.execute();
        }
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
     * Clears the entire model: shapes, command history, redo stack, and
     * clipboard.
     */
    public void clear() {
        shapes.clear();              
        commandHistory.clear();      
        deletedCommands.clear();     
        clipboard.set(null);
    }
}
