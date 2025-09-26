package parser;

import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;
import exception.InvalidTaskOperationException;
import task.Recurrence;
import task.Recurrence.RecurrenceType;

import java.time.LocalDate;

/**
 * Utility class for parsing recurrence patterns from string input.
 * This class provides static methods to convert string representations
 * of task recurrence into {@link Recurrence} objects.
 */
public class RecurrenceParser {
    private RecurrenceParser() {}

    /**
     * Parses a string representation of recurrence into a {@link Recurrence} object.
     *
     * @param args      the string to parse, expected format: "{TYPE} {FREQUENCY}".
     *                  If blank or "none" (case-insensitive), returns {@link Recurrence#none(LocalDate)}.
     * @param errorType the error type to use when creating exceptions for malformed input
     * @return a {@link Recurrence} object representing the parsed recurrence pattern
     * @throws InvalidTaskFormatException    if the input format is invalid (wrong number of tokens,
     *                                       invalid recurrence type, or invalid frequency)
     * @throws InvalidTaskOperationException if the frequency cannot be parsed as an integer
     */
    public static Recurrence parse(String args,
                                   LocalDate anchorDate,
                                   ErrorType errorType
    ) {
        if (args.isBlank() || args.equalsIgnoreCase("none")) {
            return Recurrence.none(anchorDate);
        }

        String[] tokens = args.trim().split("\\s+");
        if (tokens.length != 2) {
            throw errorType.createException();
        }
        try {
            int freq = Integer.parseInt((tokens[1]));
            RecurrenceType type = RecurrenceType.valueOf(tokens[0].toUpperCase());
            return new Recurrence(type, freq, anchorDate, null);
        } catch (NumberFormatException e) {
            throw new InvalidTaskOperationException(
                    InvalidTaskOperationException.ErrorType.INVALID_NUMBER_FORMAT,
                    tokens[1]
            );
        } catch (IllegalArgumentException e) {
            // could be invalid enum or invalid count from Recurrence constructor
            throw new InvalidTaskFormatException(ErrorType.RECURRENCE);
        }
    }
}
