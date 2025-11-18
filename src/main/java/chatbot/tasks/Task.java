package chatbot.tasks;

import chatbot.QianException;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represents a general task with a description and completion status.
 * Supports priority tagging and can be extended by specific task types.
 */


public abstract class Task {
    public String description;
    public boolean isDone;
    protected int priority = 3; // default: low priority

    public void setPriority(int level) throws QianException {
        if (level < 1 || level > 3) {
            throw new QianException("Priority must be 1 (high), 2 (medium), or 3 (low).");
        }
        this.priority = level;
    }

    public int getPriority() {
        return this.priority;
    }

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public Optional<LocalDateTime[]> getBusyPeriod() {
        return Optional.empty(); // default: none
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description + " (P" + priority + ")";
    }
}