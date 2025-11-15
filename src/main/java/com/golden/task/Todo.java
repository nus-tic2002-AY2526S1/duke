package com.golden.task;

public class Todo extends Task{

    public Todo(String description, Priority priority) {
        super(description, priority);
    }
    public Todo(String description, boolean isDone, Priority priority) {
        super(description, isDone, priority);
    }

    /**
     * Method to generate the relevant type character for serialising the task or representing the task
     *
     * @return a character unique to 'Todo' task type
     */
    @Override
    protected char renderTypeTag(){
        return 'T';
    }
    @Override
    public String serialise(){
        return String.format("%c | %c | %s | %s", renderTypeTag(), renderStatusDigit(),
                getTaskDescription(), toPriorityString(getPriority()));
    }
    @Override
    public String toString(){
        return String.format("\t[%c][%c] %s (%s)", renderTypeTag(), renderStatusCharacter(),
                getTaskDescription(), toPriorityString(getPriority()));
    }
}