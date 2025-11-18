package chatbot;

import chatbot.tasks.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Qian {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    public Qian(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();

        try {
            ArrayList<Task> loadedTasks = storage.load();
            tasks = new TaskList(loadedTasks);
            if (tasks.size() == 0) {
                ui.showMessage("Starting fresh — no saved tasks yet!");
            } else {
                ui.showMessage("Loaded " + tasks.size() + " task(s) from storage.");
            }
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String input = scanner.nextLine().trim();

            try {
                String[] parts = parser.parse(input);
                String command = parts[0];

                switch (command) {
                    case "bye":
                        ui.showGoodbye();
                        isRunning = false;
                        break;

                    case "list":
                        tasks.printTasks();
                        break;

                    case "mark":
                        tasks.markTask(parser.getTaskNumber(input));
                        break;

                    case "unmark":
                        tasks.unmarkTask(parser.getTaskNumber(input));
                        break;

                    case "todo":
                        tasks.addTask(new Todo(parser.getDescription(input, "todo")));
                        ui.showAddConfirmation(tasks);
                        break;

                    case "deadline":
                        String[] d = parser.parseDeadline(input);
                        tasks.addTask(new Deadline(d[0], d[1]));
                        ui.showAddConfirmation(tasks);
                        break;

                    case "event":
                        String[] e = parser.parseEvent(input);
                        tasks.addTask(new Event(e[0], e[1], e[2]));
                        ui.showAddConfirmation(tasks);
                        break;

                    case "delete":
                        tasks.deleteTask(parser.getTaskNumber(input));
                        break;

                    case "find":
                        String keyword = Parser.parseKeyword(input);
                        ui.showFindResults(tasks.findTasks(keyword));
                        break;

                    case "free":
                        int hours = Parser.parseFreeHours(input);
                        ui.showFreeTime(tasks.findFreeSlot(hours), hours);
                        break;

                    case "priority":
                        int[] priArgs = Parser.parsePriority(input);
                        Task updatedTask = tasks.updatePriority(priArgs[0], priArgs[1]);
                        ui.showPriorityUpdated(updatedTask);
                        break;


                    default:
                        if (Parser.isFreeTimeQuery(input)) {
                            int hours_2 = Parser.extractHoursFromSentence(input);
                            ui.showFreeTime(tasks.findFreeSlot(hours_2), hours_2);
                        } else {
                            throw new QianException("Hmmm... I don't understand that command.");
                        }

                }

                storage.save(tasks.getTasks());

            } catch (QianException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("Error saving data: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Runs the main interaction loop of the chatbot.
     */
    public static void main(String[] args) {
        new Qian("data/duke.txt").run();
    }
}