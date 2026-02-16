package checker;

import exceptions.UpdateException;

import java.util.ArrayList;

public class UpdateChecker {
    private static void checkUpdateSize(int updateSize) throws UpdateException.InvalidUpdateFormatException {
        if (updateSize % 2 == 1 || updateSize == 0) { // update size not even number == missing part
            throw new UpdateException.InvalidUpdateFormatException();
        }
    }

    public static void checkTodoUpdateValid(ArrayList<String> update) throws UpdateException {
        checkUpdateSize(update.size());

        boolean isInvalidField = !update.get(0).equals("taskName");

        boolean isUpdateInfoEmpty = update.get(1).isEmpty();

        if (isInvalidField) {
            throw new UpdateException.InvalidUpdateTodoFieldException();
        }
        if (isUpdateInfoEmpty) {
            throw new UpdateException.InvalidUpdateTodoInfoException();
        }
    }

    public static void checkDeadlineUpdateValid(ArrayList<String> update) throws UpdateException {
        checkUpdateSize(update.size());

        for (int i = 0; i < update.size(); i += 2) {
            String field = update.get(i);

            boolean isTaskName = field.equals("taskName");
            boolean isBy = field.equals("by");
            boolean isInvalidField = !isTaskName && !isBy;

            boolean isUpdateInfoEmpty = update.get(i + 1).isEmpty();

            if (isInvalidField) {
                throw new UpdateException.InvalidUpdateDeadlineFieldException();
            }
            if (isUpdateInfoEmpty) {
                throw new UpdateException.InvalidUpdateDeadlineInfoException();
            }
        }
    }

    public static void checkEventUpdateValid(ArrayList<String> update) throws UpdateException {
        checkUpdateSize(update.size());

        for (int i = 0; i < update.size(); i += 2) {
            String field = update.get(i);

            boolean isTaskName = field.equals("taskName");
            boolean isStart = field.equals("start");
            boolean isEnd = field.equals("end");
            boolean isInvalidUpdateField = !isTaskName && !isStart && !isEnd;

            boolean isUpdateInfoEmpty = update.get(i + 1).isEmpty();

            if (isInvalidUpdateField) {
                throw new UpdateException.InvalidUpdateEventFieldException();
            }
            if (isUpdateInfoEmpty) {
                throw new UpdateException.InvalidUpdateEventInfoException();
            }
        }
    }
}
