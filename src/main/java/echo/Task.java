package echo;

public abstract class Task {
    protected String description;
    protected boolean isDone;


    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    public String addedMessage(int taskCounter) {
        return "Got it. I've added this task: \n" + "  " +this + "\nNow you have " + taskCounter + " tasks in the list ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] "+ description;
    }

    public abstract String toSaveFormat();

}
