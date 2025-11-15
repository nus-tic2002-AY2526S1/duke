package com.golden.task;

public abstract class Task {
    private String description;
    private boolean isDone;
    protected Priority priority;

    public Task(String description, Priority priority) {
        assert priority != null;
        setDescription(description.trim());
        setPriority(priority);
        setIsDone(false); // default if no isDone status given
    }
    public Task(String description, boolean isDone, Priority priority) {
        assert priority != null: "Priority must not be null.";
        setDescription(description.trim());
        setIsDone(isDone);
        setPriority(priority);
    }

    // Getters
    public String getTaskDescription() {
        return this.description;
    }
    public Priority getPriority() {
        return priority;
    }
    public boolean getIsDone(){
        return isDone;
    }

    // Setters
    public void setDescription(String taskDescription){
        this.description = taskDescription;
    }
    public void setPriority(Priority p){
        // extra guard in case priority is null
        this.priority = (p == null) ? Priority.LOW : p;
    }
    public void setIsDone(boolean isDone){
        this.isDone = isDone;
    }

    // Renderers, Serialisers, Print Formatters
    /**
     * Method to generate the relevant 'done' status marker of this task instance
     *
     * @return a 'X' if taks is done and blank (" ") if not done
     */
    protected char renderStatusCharacter(){
        return isDone ? 'X' : ' ';
    };
    protected char renderStatusDigit(){
        return isDone ? '1' : '0';
    };
    protected abstract char renderTypeTag();
    /**
     * Method to generate documentation for the task in a specific format
     *
     * @return a string in the format: [Task type} | [isDone status] | [task description] | (...) | [priority]
     */
    public abstract String serialise();
    public abstract String toString();
    public String toPriorityString(Priority priority) {
        assert priority != null;
        return priority.name();
    }
}