public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Event end time cannot be empty!");
        }
        this.from = from.trim();
        this.to = to.trim();
    }

    public Event(String description, String from, String to, boolean validateTimes) {
        super(description);
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Event end time cannot be empty!");
        }

        String trimmedFrom = from.trim();
        String trimmedTo = to.trim();

        if (validateTimes) {
            if (trimmedFrom.length() < 2) {
                throw new IllegalArgumentException("Event start time seems too short!");
            }
            if (trimmedTo.length() < 2) {
                throw new IllegalArgumentException("Event end time seems too short!");
            }
            if (trimmedFrom.equals(trimmedTo)) {
                System.out.println("💡 Note: Start and end times are the same. Is this correct?");
            }
        }

        this.from = trimmedFrom;
        this.to = trimmedTo;
    }

    @Override
    public String toString() {
        try {
            if (!isValid()) {
                return "[E][INVALID] Invalid Event Task";
            }
            String displayFrom = (from == null || from.trim().isEmpty()) ? "unspecified" : from;
            String displayTo = (to == null || to.trim().isEmpty()) ? "unspecified" : to;
            return "[E]" + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
        } catch (Exception e) {
            return "[E][ERROR] Could not display event task";
        }
    }

    public String getFrom() {
        return (from != null) ? from : "unspecified";
    }

    public String getTo() {
        return (to != null) ? to : "unspecified";
    }

    public void setFrom(String from) {
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("Event start time cannot be empty!");
        }
        this.from = from.trim();
    }

    public void setTo(String to) {
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Event end time cannot be empty!");
        }
        this.to = to.trim();
    }

    @Override
    public boolean isValid() {
        return super.isValid() &&
                from != null &&
                !from.trim().isEmpty() &&
                to != null &&
                !to.trim().isEmpty();
    }

    public static Event createEvent(String description, String from, String to) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Event description cannot be empty!");
        }
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("Event start time cannot be empty!");
        }
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("Event end time cannot be empty!");
        }
        return new Event(description.trim(), from.trim(), to.trim());
    }

    public boolean meetsEventRequirements() {
        return isValid() &&
                getDescription().length() <= 1000 &&
                from.length() <= 100 &&
                to.length() <= 100;
    }

    public boolean hasReasonableTimeFormats() {
        if (from == null || to == null) return false;
        return from.length() >= 2 &&
                from.length() <= 100 &&
                to.length() >= 2 &&
                to.length() <= 100 &&
                !from.matches(".*[\\x00-\\x1F\\x7F].*") &&
                !to.matches(".*[\\x00-\\x1F\\x7F].*");
    }

    public boolean hasLogicalTimeOrder() {
        if (from == null || to == null) return false;
        return from.compareTo(to) <= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Event event = (Event) obj;
        return from.equals(event.from) && to.equals(event.to);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }

    public String getDurationDescription() {
        if (!isValid()) {
            return "Invalid event times";
        }
        return "From " + from + " to " + to;
    }
}