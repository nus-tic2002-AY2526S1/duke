import java.util.ArrayList;

import moonchester_data.Task;
import moonchester_data.UserList;
import moonchester_utils.MoonchesterException;
import moonchester_utils.MoonchesterHandler;
import moonchester_utils.MoonchesterParser;
import moonchester_utils.MoonchesterStorage;

public class Moonchester {
    public static void main(String[] args) throws MoonchesterException {
        ArrayList<String> lines = MoonchesterStorage.readLines();
        MoonchesterParser parser = new MoonchesterParser();
        ArrayList<Task> tasks = parser.retrieveObjects(lines);
        UserList userList = new UserList(tasks);
        MoonchesterHandler handler = new MoonchesterHandler(userList);
        handler.start();
    }
}
