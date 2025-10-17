package message;

import storage.TaskLoadResult;

/**
 * Static welcome message displayed when the application starts.
 */
public class WelcomeMessage implements Message {
    private static final String WELCOME_CONTENT =
            """
                    Hello, I'm MeeBot.
                    Ready to mee-t your needs today!
                    Type 'help' to find out what can mee stir up for you today.
                    """;
    private static final String LOAD_WITH_SUCCESS =
            """
                    Hello, I'm MeeBot.
                    %d tasks are loaded successfully.
                    Type 'list' to see your tasks, or 'help' for options.
                    """;

    private static final String LOAD_WITH_FAILURE =
            """
                    Hello, I'm MeeBot.
                    %d tasks are loaded successfully.
                    The other %d... well, they had their own vision.
                    See '%s' for tasks that did not make it, or type 'list' to see your tasks.
                    """;

    @Override
    public String message() {
        TaskLoadResult stats = TaskLoadResult.current();

        if (stats.isFreshStart()) return WELCOME_CONTENT;

        if (stats.allSuccessful()) {
            return String.format(LOAD_WITH_SUCCESS, stats.loadedTaskCount());
        }

        if (stats.hasFailures()) {
            return String.format(LOAD_WITH_FAILURE,
                    stats.loadedTaskCount(),
                    stats.failedTaskCount(),
                    stats.errorLogFile());
        }

        return WELCOME_CONTENT;
    }
}
