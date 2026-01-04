package bongocat.tasks;

/**
 * Base Task class
 */
public abstract class Task {

    protected String name;
    protected boolean isDone;

    public Task(String name) {
        assert name != null : "Task name cannot be null";
        this.name = name.trim();
        this.isDone = false;
    }


    public String getName() {
        return this.name;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markNotDone() {
        this.isDone = false;
    }

    public String statusBox() {
        return isDone ? "[X]" : "[ ]";
    }

    public abstract String getTypeIcon();

    public abstract String toSaveFormat();

    @Override
    public String toString() {
        return getTypeIcon() + statusBox() + " " + name;
    }
}
