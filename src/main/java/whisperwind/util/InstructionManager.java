package whisperwind.util;

/**
 * The {@code InstructionManager} class is responsible for displaying user instructions,
 * guides, and messages for the Whisperwind task management application.
 * <p>
 * It provides visual, text-based guides to help users understand the available commands,
 * such as task management, deletion operations, and quick start instructions.
 * </p>
 *
 * <p>All methods are static so they can be accessed without creating an instance.</p>
 */
public class InstructionManager {

    /**
     * Displays the basic command instructions for Whisperwind, including
     * task management, adding tasks, deleting basics, and a quick start guide.
     * <p>
     * This method prints a formatted set of instructions in the console
     * with visual borders and icons to improve readability.
     * </p>
     */
    public static void showBasicInstructions() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            🌸 WHISPERWIND BASIC COMMANDS 🌸                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");

        // TASK MANAGEMENT SECTION
        System.out.println("║  📋 TASK MANAGEMENT                                                           ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] basicCommands = {
                "list                    - View all your tasks",
                "mark 3                  - Mark task 3 as completed",
                "mark 1,3,5             - Mark multiple tasks as completed",
                "unmark 3                - Mark task 3 as not completed",
                "unmark 2,4,6           - Mark multiple tasks as not completed",
                "find book               - Search for tasks containing 'book'",
                "find meeting            - Find all tasks about meetings",
                "save                    - Manually save your tasks",
                "schedule today          - View today's schedule",
                "schedule tomorrow       - View tomorrow's schedule",
                "schedule upcoming       - View next 7 days",
                "schedule 2024-12-25     - View schedule for specific date",
                "schedule 2024-12-20 to 2024-12-25 - View date range schedule"
        };
        for (String cmd : basicCommands) {
            System.out.printf("║     %-70s ║%n", cmd);
        }
        System.out.println("║                                                                                ║");

        // ADD TASKS SECTION
        System.out.println("║  📝 ADDING TASKS                                                              ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] addCommands = {
                "todo Buy groceries             - Simple task",
                "deadline Return book /by Friday - Task with deadline",
                "event Meeting /from 2pm /to 3pm - Event with times"
        };
        for (String cmd : addCommands) {
            System.out.printf("║     %-70s ║%n", cmd);
        }
        System.out.println("║                                                                                ║");

        // DELETE BASICS SECTION
        System.out.println("║  🗑️  DELETE BASICS                                                            ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] deleteBasics = {
                "delete 3                 - Remove task 3",
                "delete 1,3,5            - Remove multiple tasks",
                "delete completed         - Remove all completed tasks",
                "delete all               - Remove ALL tasks (careful!)",
                "",
                "💡 Type 'delete instruction' for advanced delete options!"
        };
        for (String cmd : deleteBasics) {
            System.out.printf("║     %-70s ║%n", cmd);
        }
        System.out.println("║                                                                                ║");

        // QUICK START SECTION
        System.out.println("║  🎯 QUICK START                                                               ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] quickStart = {
                "1. todo 'Your first task'",
                "2. list (to see your tasks)",
                "3. mark 1 (to complete task 1)",
                "4. find book (to search for tasks)",
                "5. schedule today (to view today's schedule)",
                "6. delete completed (to clean up)"
        };
        for (String step : quickStart) {
            System.out.printf("║     %-70s ║%n", step);
        }
        System.out.println("║                                                                                ║");

        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("   💫 Ready to start? Try adding your first task with 'todo [description]'!");
        System.out.println("   🔍 Need to find something? Try 'find [keyword]'");
        System.out.println("   📅 Want to see your schedule? Type 'schedule today'");
        System.out.println("   🗑️  Need advanced delete options? Type 'delete instruction'");
        System.out.println();
    }

    /**
     * Displays advanced delete instructions for Whisperwind, explaining
     * bulk deletion, pattern matching, deletion by type, and advanced safety features.
     * <p>
     * This method is meant for users who want finer control over their task deletion commands.
     * </p>
     */
    public static void showDeleteInstructions() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         🗑️ WHISPERWIND DELETE MASTER GUIDE 🗑️                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");

        // BULK DELETE SECTION
        System.out.println("║  🔢 BULK DELETE MULTIPLE TASKS                                                ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] bulkExamples = {
                "delete 1,3,5           - Remove tasks 1, 3, and 5",
                "delete 2,4,6,8         - Remove even-numbered tasks",
                "delete 1-5             - Coming soon: range support!"
        };
        for (String example : bulkExamples) {
            System.out.printf("║     %-70s ║%n", example);
        }
        System.out.println("║                                                                                ║");

        // PATTERN MATCHING SECTION
        System.out.println("║  🔍 SMART PATTERN MATCHING                                                    ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] patterns = {
                "delete book*            - Tasks STARTING with 'book'",
                "delete *meeting         - Tasks ENDING with 'meeting'",
                "delete *urgent*         - Tasks CONTAINING 'urgent'",
                "delete report           - Tasks EXACTLY matching 'report'",
                "",
                "💡 Works with any text in task descriptions!"
        };
        for (String pattern : patterns) {
            System.out.printf("║     %-70s ║%n", pattern);
        }
        System.out.println("║                                                                                ║");

        // TASK TYPE DELETION SECTION
        System.out.println("║  📁 DELETE BY TASK TYPE                                                       ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] typeDeletes = {
                "delete todo             - Remove ALL todo tasks",
                "delete deadline         - Remove ALL deadline tasks",
                "delete event            - Remove ALL event tasks",
                "",
                "💡 Perfect for cleaning up specific task categories!"
        };
        for (String type : typeDeletes) {
            System.out.printf("║     %-70s ║%n", type);
        }
        System.out.println("║                                                                                ║");

        // ADVANCED FEATURES SECTION
        System.out.println("║  ⚡ ADVANCED FEATURES                                                          ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] advanced = {
                "delete completed        - Remove all checked tasks",
                "delete all              - Nuclear option! (requires confirmation)",
                "",
                "🛡️  Safety Features:",
                "  • Preview before deletion",
                "  • Confirmation prompts",
                "  • Skip invalid task numbers",
                "  • Auto-save after every delete"
        };
        for (String feature : advanced) {
            System.out.printf("║     %-70s ║%n", feature);
        }
        System.out.println("║                                                                                ║");

        // REAL-WORLD EXAMPLES SECTION
        System.out.println("║  🎯 REAL-WORLD SCENARIOS                                                      ║");
        System.out.println("║  ───────────────────────────────────────────────────────────────────────────  ║");
        String[] scenarios = {
                "After completing a project:",
                "  delete project*               - Remove all project tasks",
                "",
                "Weekly cleanup:",
                "  delete completed              - Clear done tasks",
                "",
                "Category reorganization:",
                "  delete todo                   - Start fresh with todos",
                "",
                "Bulk cleanup:",
                "  delete 1,3,5,7,9             - Remove specific tasks"
        };
        for (String scenario : scenarios) {
            System.out.printf("║     %-70s ║%n", scenario);
        }
        System.out.println("║                                                                                ║");

        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("   🚀 Pro Tip: Combine patterns! Try 'delete *meeting' to clean up all meetings!");
        System.out.println("   📖 Return to basic commands with 'view instruction'");
        System.out.println();
    }

    /**
     * Displays a welcome message when Whisperwind starts.
     * <p>
     * Includes helpful hints for accessing instructions and delete guides.
     * </p>
     */
    public static void showWelcomeMessage() {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              🌸 WHISPERWIND 🌸                                ║");
        System.out.println("║                         Your Personal Task Manager                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                    Type 'view instruction' for basic commands                 ║");
        System.out.println("║                    Type 'delete instruction' for delete guide                 ║");
        System.out.println("║                    Type 'find KEYWORD' to search for tasks                    ║");
        System.out.println("║                    Type 'schedule today' to view today's schedule             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("What can I do for you today? 💫");
    }

    /**
     * Displays a farewell message when the user exits Whisperwind.
     * <p>
     * Confirms that tasks have been saved and thanks the user.
     * </p>
     */
    public static void showGoodbyeMessage() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                 👋 GOODBYE!                                   ║");
        System.out.println("║                      Thanks for using Whisperwind! 🌸                         ║");
        System.out.println("║                   Your tasks are saved for next time! 💾                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Displays comprehensive archive instructions and usage guide for Whisperwind.
     * <p>
     * This method presents a formatted visual guide showing all available archive commands,
     * their purposes, and usage examples. The guide is displayed within a bordered box
     * for better readability and user experience.
     * </p>
     *
     * <p><b>Archive Features Covered:</b></p>
     * <ul>
     *   <li>Archiving all tasks to start fresh</li>
     *   <li>Archiving only completed tasks</li>
     *   <li>Archiving by task type (todo, deadline, event)</li>
     *   <li>Listing and viewing archive files</li>
     *   <li>Archive file location and format information</li>
     * </ul>
     *
     * <p><b>Example Usage:</b></p>
     * <pre>
     * {@code
     * // Display archive instructions to user
     * InstructionManager.showArchiveInstructions();
     * }
     * </pre>
     * <p><b>Visual Format:</b></p>
     * The instructions are displayed in a bordered box with:
     * - Header title "WHISPERWIND ARCHIVE GUIDE"
     * - Descriptive introduction
     * - Command list with descriptions
     * - Helpful tips and file location information
     */
    public static void showArchiveInstructions() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            📦 WHISPERWIND ARCHIVE GUIDE 📦                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                                                ║");
        System.out.println("║  Archive commands help you keep records while maintaining a clean task list!   ║");
        System.out.println("║                                                                                ║");

        String[] archiveCommands = {
                "archive all       - Backup ALL tasks and clear your list",
                "archive completed - Archive only checked-off tasks",
                "archive todo      - Archive all simple todo tasks",
                "archive deadline  - Archive all deadline tasks",
                "archive event     - Archive all event tasks",
                "archive list      - View all available archive files",
                "archive view 1    - See contents of archive file #1",
                "",
                "💡 Archives are saved in ./data/archive/ as readable text files",
                "💾 Perfect for starting fresh while keeping records!"
        };

        for (String cmd : archiveCommands) {
            System.out.printf("║   %-70s ║%n", cmd);
        }
        System.out.println("║                                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}