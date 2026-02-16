package parser.userInputParser;

import enums.CommandType;
import exceptions.GrootException;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Main parsing used for user input
 */
public class UserInputParser {
    /**
     * Get the relevant information
     *
     * @param input user input
     * @return a pair where the key is the command and value are the information for the command
     * @throws GrootException if there are any error during parsing
     */
    public static AbstractMap.SimpleEntry<CommandType, ArrayList<String>> parseUserInput(String input)
            throws GrootException {
        //split and trim the 2 parts of command {command, info}
        AbstractMap.SimpleEntry<CommandType, ArrayList<String>> inputs = splitInput(input);
        assert inputs.getKey() != null;
        assert inputs.getValue() != null;

        switch (inputs.getKey()) {
            case NONE, LIST -> {} // no additional parsing needed
            case VIEW -> ViewParser.parseView(inputs); //{"viewDate"}
            case MARK, UNMARK, DELETE, CLONE -> TaskNumberParser.parseTaskNumber(inputs); //{"taskNumber"}
            case TODO -> TodoParser.parseTodo(inputs); //{"taskName"}
            case DEADLINE -> DeadlineParser.parseDeadline(inputs); //{"taskName", "by"}
            case EVENT -> EventParser.parseEvent(inputs); //{"taskName", "from", "by"}
            case UPDATE -> UpdateParser.parseUpdate(inputs); //{"taskNumber", "update"...}
            case FIND -> FindParser.parseFind(inputs); //{"keyword}
        }
        return inputs;
    }

    /*
     * Convert command from string to CommandType enum
     */
    private static CommandType parseCommand(String command) throws GrootException.InvalidCommandException {
        assert command != null;

        try {
            command = command.trim();

            if (command.isEmpty()) {
                return CommandType.NONE;
            }

            return CommandType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GrootException.InvalidCommandException(command);
        }
    }

    /*
     * Split user input into 2 parts, command and information
     */
    private static AbstractMap.SimpleEntry<CommandType, ArrayList<String>> splitInput(String command)
            throws GrootException {
        String[] commandSplit = command.split(" ", 2);

        ArrayList<String> taskInfo = new ArrayList<>();
        CommandType commandType = parseCommand(commandSplit[0]);

        if (commandSplit.length < 2) { //only one word {""}
            taskInfo.add("");
        } else { //{...}
            taskInfo.add(commandSplit[1].trim());
        }

        //{commandType, {""}} or {commandType, {...}}
        return new AbstractMap.SimpleEntry<>(commandType, taskInfo);
    }
}
