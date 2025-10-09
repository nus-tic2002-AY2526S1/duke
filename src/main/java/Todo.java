public class Todo extends Task {
    //added to improve error handling system
    public Todo(String description) {
        super(InputSanitizer.sanitizeDescription(description));
    }

    public Todo(String description, boolean validate) {
        super(description);
        if (validate && (description == null || description.trim().isEmpty())) {
            throw new IllegalArgumentException("Todo description cannot be empty!");
        }
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return "[T][INVALID] Invalid Todo Task";
            }
            return "[T]" + super.toString();
        } catch (Exception e) {
            return "[T][ERROR] Could not display task";
        }
    }

    @Override
    public boolean isValid() {
        return super.isValid() && getDescription() != null;
    }

    public static Todo createTodo(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Todo description cannot be empty!");
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