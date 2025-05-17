
package sadprojectwork;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private Deque<Command> commandHistory;
    private Deque<Command> deletedCommands;
    private List<MyShape> shapes = new ArrayList<>();
    private MyShape selected;
    private MyShape clipboard;
    
    public Model() {
        commandHistory = new ArrayDeque<>();
        deletedCommands = new ArrayDeque<>();
    }
    
    public void execute(Command command) {
        commandHistory.addLast(command);
        command.execute();
    }
    
    public void undoLast() {
        Command last = commandHistory.removeLast();
        deletedCommands.addLast(last);
        last.undo();
    }
    
    public void redoLast() {
        Command last = deletedCommands.removeLast();
        commandHistory.addLast(last);
        last.execute();
    }
    
    public void addShape(MyShape s) {
        shapes.add(s);
    }

    public void removeShape(MyShape s) {
        shapes.remove(s);
    }

    public List<MyShape> getShapes() {
        return shapes;
    }

    public void setSelectedShape(MyShape s) {
        selected = s;
    }
    
    public void setClipboardShape(MyShape s){
        clipboard = s;
    }

    public MyShape getSelectedShape() {
        return selected;
    }
    
    public MyShape getClipboardShape(){
        return clipboard;
    }
}
