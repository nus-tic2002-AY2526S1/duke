public class InstructionManager {

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
                "save                    - Manually save your tasks"
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
                "4. delete completed (to clean up)"
        };
        for (String step : quickStart) {
            System.out.printf("║     %-70s ║%n", step);
        }
        System.out.println("║                                                                                ║");

        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("   💫 Ready to start? Try adding your first task with 'todo [description]'!");
        System.out.println("   🔍 Need advanced delete options? Type 'delete instruction'");
        System.out.println();
    }

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

    public static void showWelcomeMessage() {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              🌸 WHISPERWIND 🌸                                ║");
        System.out.println("║                         Your Personal Task Manager                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                    Type 'view instruction' for basic commands                 ║");
        System.out.println("║                    Type 'delete instruction' for delete guide                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("What can I do for you today? 💫");
    }

    public static void showGoodbyeMessage() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                 👋 GOODBYE!                                   ║");
        System.out.println("║                      Thanks for using Whisperwind! 🌸                         ║");
        System.out.println("║                   Your tasks are saved for next time! 💾                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════╝");
    }
}