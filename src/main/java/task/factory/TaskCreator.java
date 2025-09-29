package task.factory;

import exception.MeeBotException;
import parser.json.SimpleJsonObject;
import task.Task;

public interface TaskCreator {
    Task createFromArgs(String args) throws MeeBotException;

    Task createFromJson(SimpleJsonObject obj) throws MeeBotException;
}
