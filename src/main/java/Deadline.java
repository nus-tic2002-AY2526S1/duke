public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline time cannot be empty!");
        }
        this.by = by.trim();
    }

    public Deadline(String description, String by, boolean validateTimes) {
        super(description);
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline time cannot be empty!");
        }
        String trimmedBy = by.trim();
        if (validateTimes && trimmedBy.length() < 2) {
            throw new IllegalArgumentException("Deadline time seems too short!");
        }
        this.by = trimmedBy;
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return "[D][INVALID] Invalid Deadline Task";
            }
            if (by == null || by.trim().isEmpty()) {
                return "[D]" + super.toString() + " (by: unspecified)";
            }
            return "[D]" + super.toString() + " (by: " + by + ")";
        } catch (Exception e) {
            return "[D][ERROR] Could not display deadline task";
        }
    }

    public String getBy() {
        return (by != null) ? by : "unspecified";
    }

    public void setBy(String by) {
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline time cannot be empty!");
        }
        this.by = by.trim();
    }

    @Override
    public boolean isValid() {
        return super.isValid() &&
                by != null &&
                !by.trim().isEmpty();
    }

    public static Deadline createDeadline(String description, String by) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline description cannot be empty!");
        }
        if (by == null || by.trim().isEmpty()) {
            throw new IllegalArgumentException("Deadline time cannot be empty!");
        }
        return new Deadline(description.trim(), by.trim());
    }

    public boolean meetsDeadlineRequirements() {
        return isValid() &&
                getDescription().length() <= 1000 &&
                by.length() <= 100;
    }

    public boolean hasReasonableTimeFormat() {
        if (by == null) return false;
        return by.length() >= 2 &&
                by.length() <= 100 &&
                !by.matches(".*[\\x00-\\x1F\\x7F].*");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Deadline deadline = (Deadline) obj;
        return by.equals(deadline.by);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + by.hashCode();
        return result;
    }
}