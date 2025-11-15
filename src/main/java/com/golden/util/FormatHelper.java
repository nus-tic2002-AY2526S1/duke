package com.golden.util;

import com.golden.task.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class FormatHelper {         // 'final' is used to prevent a new Helper class
    private static final String lineBreak =
            "--------------------------------------------------------------------------------------------------------------";

    // override default public constructor method and makes it private; prevents instantiation by other classes
    private FormatHelper(){}

    // Static utility methods means no need to create a Helper object, can be called directly: e.g. Helper.printReply(string)
    /**
     *  Returns a formatted string with indentation added at the start of every new line.
     *
     *  @return  the formatted string input with indentation added at the start of every new line.
     */
    private static String addIndentation(String s){
        return "\t" + s.replace("\n", "\n\t");
    }

    /**
     *  Returns a formatted string with two line separators framing the start and end of the message.
     *
     *  @return  the formatted string with a line separator added at the start and to the end of the string input.
     */
    private static String formatReply(String string){
        return addIndentation(lineBreak) + "\n"
                + addIndentation(string) + "\n"
                + addIndentation(lineBreak);
    }

    /**
     *  Prints a formatted reply string.
     *
     *  @return  print the formatted reply string.
     */
    public static void printFormattedReply(String rawReply){
        System.out.println(formatReply(rawReply));
    }

    /**
     *  Returns a transformed {@code LocalDate} object into a String in the following format: MMM d yyyy.
     *
     *  @return  the String version of this {@code LocalDate} object in the format: MMM d yyyy.
     *          This is a {@Throwable} instance (which may be {@code null}).
     */
    public static String displayAsMMMdyyyy(LocalDate dateObj){
        if (dateObj == null){
            throw new IllegalArgumentException("date must not be null.");
        }
        return dateObj.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }
    public static String renderTaskListToString(List<Task> matches) {
        StringBuilder taskString = new StringBuilder();
        if (matches.isEmpty()) {
            return "I'm sorry, no matches found.";
        } else {
            for (int i = 0; i < matches.size(); i++) {
                taskString.append(i+1).append(".")
                    .append(matches.get(i).toString())
                    .append('\n');
            }
        }
        return taskString.toString();
    }
}
