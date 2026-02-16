package commands;

import enums.CommandType;
import exceptions.TaskNumberException;
import parser.userInputParser.TaskNumberParser;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;
import ui.UserInteraction;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;

public class CloneCommand extends Command {
    String description;
    LocalDateTime by;
    LocalDateTime from;
    LocalDateTime to;

    public CloneCommand(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> command,
                        ArrayList<Task> tasklist) throws TaskNumberException {
        super(tasklist);
        String taskNum =  command.getValue().get(0);
        int taskNumber = TaskNumberParser.getTaskNumber(taskNum, tasklist.size());
        Task task = tasklist.get(taskNumber-1);
        this.description = task.getDescription();
        if (task instanceof Deadline) {
            this.by = ((Deadline) task).getBy();
        }else if (task instanceof Event) {
            this.from = ((Event) task).getStartDateTime();
            this.to = ((Event) task).getEndDateTime();
        }
    }

    private void cloneTask(){
        Task task;
        if (by != null){
            task = new Deadline(description, by);
        }else if (from != null && to != null) {
            task = new Event(description, from, to);
        }else{
            task = new ToDo(description);
        }

        tasklist.add(task);

        UserInteraction.printMessage("Task cloned: " + task,
                "Now you have " + tasklist.size() + " tasks in the list.");
    }

    @Override
    public void execute() {
        cloneTask();
    }
}
