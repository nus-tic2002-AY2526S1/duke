public abstract class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty!");
        }
        this.description = description.trim();
        this.isDone = false;
    }

    public abstract TaskType getTaskType();

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    public void markAsDone() {
        if (this.isDone) {
            System.out.println("ℹ️  This task is already marked as done!");
        } else {
            this.isDone = true;
        }
    }

    public void markAsUndone() {
        if (!this.isDone) {
            System.out.println("ℹ️  This task is already marked as not done!");
        } else {
            this.isDone = false;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Task description cannot be empty!");
        }
        this.description = description.trim();
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    public boolean isValid() {
        return description != null && !description.trim().isEmpty();
    }
}