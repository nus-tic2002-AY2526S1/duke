package task;

/**
 * Represents a To-Do task.
 */
public class Todo extends Task {

    /**
     * @param description of the task.
     * @param index of task in a TaskList.
     */
    public Todo(String desc, int ind) {
        super(desc, ind);
    }

    @Override
    public String getTaskString() {
        String taskString;
        if (this.getStatus()) {
            taskString = "   [T][X]";
        } else {
            taskString = "   [T][ ]";
        }
        if (this.getPriority() != null) {
            return (taskString + "[" + this.getPriority().toString() + "] " + this.getDescription());
        } else {
            return (taskString + "[ ] " + this.getDescription());
        }
    }

    @Override
    public String getTaskStringWithIndex() {
        String taskString;
        if (this.getStatus()) {
            taskString = " " + this.getIndex() + ". [T][X]";
        } else {
            taskString = " " + this.getIndex() + ". [T][ ]";
        }
        if (this.getPriority() != null) {
            return (taskString + "[" + this.getPriority().toString() + "] " + this.getDescription());
        } else {
            return (taskString + "[ ] " + this.getDescription());
        }
    }
}
