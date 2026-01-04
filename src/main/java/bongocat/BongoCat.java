
package bongocat;

import bongocat.ui.Ui;
import bongocat.storage.Storage;
import bongocat.tasks.TaskList;
import bongocat.commands.Command;
import bongocat.parser.Parser;

/**
 * Main logic of BongCat
 */
public class BongoCat {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public BongoCat(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (BongoException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        new BongoCat("SavedFiles/BongoCat.txt").run();
    }
}
