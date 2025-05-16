
package sadprojectwork;

import java.util.ArrayDeque;
import java.util.Deque;

public class Model {

    private Deque<Command> commandHistory;
    private Deque<Command> deletedCommands;
    
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
}
