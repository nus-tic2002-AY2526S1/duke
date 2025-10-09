public enum ConfirmationLevel {
    NONE("none", "No confirmation needed", 0),
    SIMPLE("simple", "Simple yes/no confirmation", 1),
    STRONG("strong", "Strong confirmation required", 2),
    DESTRUCTIVE("destructive", "Destructive action - type exact phrase", 3);

    private final String level;
    private final String description;
    private final int severity;

    ConfirmationLevel(String level, String description, int severity) {
        this.level = level;
        this.description = description;
        this.severity = severity;
    }

    public String getLevel() { return level; }
    public String getDescription() { return description; }
    public int getSeverity() { return severity; }

    public static ConfirmationLevel forOperation(DeleteOperation operation) {
        switch (operation) {
            case ALL:
                return DESTRUCTIVE;
            case COMPLETED:
            case PATTERN:
            case BULK:
                return STRONG;
            case BY_TYPE:
                return SIMPLE;
            case SINGLE:
            default:
                return NONE;
        }
    }
}