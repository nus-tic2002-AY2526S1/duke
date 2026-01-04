
package bongocat.tasks;
/**
 * Todo Class - Extends as a type of Task
 */
public class ToDo extends Task {

    public ToDo(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTypeIcon() {
        return "[T]";
    }

    @Override
    public String toSaveFormat() {
        return "T | " + (isDone ? "[X]" : "[ ]") + " | " + name;
    }
}
