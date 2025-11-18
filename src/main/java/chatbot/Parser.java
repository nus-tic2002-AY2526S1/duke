package chatbot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public String[] parse(String input) {
        return input.split(" ", 2);
    }

    public String getDescription(String input, String commandWord) throws QianException {
        String desc = input.replaceFirst(commandWord, "").trim();
        if (desc.isEmpty()) {
            throw new QianException("The description of a " + commandWord + " cannot be empty.");
        }
        return desc;
    }

    public int getTaskNumber(String input) throws QianException {
        String[] parts = input.split(" ");
        if (parts.length < 2) throw new QianException("Please specify a task number!");
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new QianException("Task number must be an integer!");
        }
    }

    public String[] parseDeadline(String input) throws QianException {
        if (!input.contains("/by")) throw new QianException("A deadline must have a /by part!");
        String[] parts = input.split("/by", 2);
        String desc = parts[0].replaceFirst("deadline", "").trim();
        String by = parts[1].trim();
        if (desc.isEmpty() || by.isEmpty()) throw new QianException("Deadline description or /by cannot be empty!");
        return new String[]{desc, by};
    }

    public String[] parseEvent(String input) throws QianException {
        if (!input.contains("/from") || !input.contains("/to"))
            throw new QianException("An event must have /from and /to parts!");
        String[] first = input.split("/from", 2);
        String desc = first[0].replaceFirst("event", "").trim();
        String[] time = first[1].split("/to", 2);
        if (desc.isEmpty() || time[0].trim().isEmpty() || time[1].trim().isEmpty())
            throw new QianException("The description, /from, or /to part cannot be empty!");
        return new String[]{desc, time[0].trim(), time[1].trim()};
    }

    public static String parseKeyword(String input) throws QianException {
        String[] parts = input.trim().split(" ", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            throw new QianException("Please provide a keyword to search for!");
        }
        return parts[1];
    }

    public static boolean isFreeTimeQuery(String input) {
        String lower = input.toLowerCase();
        return lower.contains("free") && lower.contains("hour");
    }

    public static int extractHoursFromSentence(String input) throws QianException {
        Pattern pattern = Pattern.compile("(\\d+)\\s*-?\\s*hour");
        Matcher matcher = pattern.matcher(input.toLowerCase());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new QianException("I couldn't find the number of hours in your request. Please mention it clearly (e.g., '4 hour free slot').");
        }
    }

    public static int parseFreeHours(String fullCommand) throws QianException {
        String[] parts = fullCommand.trim().split(" ", 2);
        if (parts.length < 2) throw new QianException("Please specify how many hours you need free!");
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new QianException("Invalid number of hours: " + parts[1]);
        }
    }

    public static int[] parsePriority(String input) throws QianException {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new QianException("Usage: priority <task number> <1|2|3>");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1; // convert to 0-based
            int level = Integer.parseInt(parts[2]);
            return new int[]{index, level};
        } catch (NumberFormatException e) {
            throw new QianException("Task index and priority must be numbers.");
        }
    }


}