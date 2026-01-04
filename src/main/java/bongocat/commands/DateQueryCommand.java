package bongocat.commands;

import bongocat.tasks.*;
import bongocat.ui.Ui;
import bongocat.storage.Storage;
import bongocat.BongoException;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Returns date
 */
public class DateQueryCommand extends Command {

    private final LocalDate date;

    public DateQueryCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BongoException {
        ArrayList<Task> list = tasks.getAllTasks();
        ArrayList<Task> matches = new ArrayList<>();

        for (Task t : list) {
            if (t instanceof Deadline d && d.getBy().equals(date)) {
                matches.add(t);
            }
            else if (t instanceof Event e &&
                    (e.getFrom().equals(date) || e.getTo().equals(date))) {
                matches.add(t);
            }
        }

        if (matches.isEmpty()) {
            throw new BongoException("No tasks on " + date.toString());
        }

        System.out.println("Tasks on " + date + ":");
        int count = 1;
        for (Task t : matches) {
            System.out.println(count++ + ". " + t);
        }
    }
}
