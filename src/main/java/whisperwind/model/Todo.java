package whisperwind.model;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.TODO;
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return TaskType.TODO.getPrefix() + "[INVALID] Invalid model.Todo model.Task";
            }
            return TaskType.TODO.getPrefix() + super.toString();
        } catch (Exception e) {
            return TaskType.TODO.getPrefix() + "[ERROR] Could not display task";
        }
    }

    public static Todo createTodo(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("model.Todo description cannot be empty!");
        }
        return new Todo(description.trim());
    }

    public boolean meetsTodoRequirements() {
        String desc = getDescription();
        return desc != null &&
                !desc.trim().isEmpty() &&
                desc.length() <= 1000;
    }
}