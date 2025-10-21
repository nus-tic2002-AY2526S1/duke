package parser;

import java.time.LocalDate;

import exception.FileContentException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;
import exception.InvalidTaskOperationException;
import parser.json.SimpleJsonObject;
import task.Recurrence;
import task.Recurrence.RecurrenceType;

/**
 * Utility class for parsing recurrence patterns from different input sources.
 * This class provides static methods to convert string representations and
 * JSON objects into {@link Recurrence} objects.
 */
public class RecurrenceParser {
    private RecurrenceParser() {
    }

    /**
     * Parses a string representation of recurrence from user command input.
     * <p>
     * <strong>Expected Format:</strong> {@code "<TYPE> <FREQUENCY>"}
     * where:
     * <ul>
     *     <li>TYPE is a valid {@link RecurrenceType} value (case-insensitive)</li>
     *     <li>FREQUENCY is a positive integer</li>
     * </ul>
     *
     * @param args       the string to parse (e.g., "weekly 2", "monthly 12").
     *                   If blank, returns {@link Recurrence#none(LocalDate)}
     * @param anchorDate the reference date for calculating date of last recurrence
     * @param errorType  the error type to use when creating exceptions for malformed input
     * @return a {@link Recurrence} object representing the parsed recurrence pattern
     * @throws InvalidTaskFormatException    if the input format is invalid (wrong number of tokens,
     *                                       invalid recurrence type, or invalid frequency)
     * @throws InvalidTaskOperationException if the frequency cannot be parsed as an integer
     */
    public static Recurrence parse(String args,
                                   LocalDate anchorDate,
                                   ErrorType errorType)
            throws InvalidTaskFormatException, InvalidTaskOperationException {
        if (args.isBlank() || args.equalsIgnoreCase("none")) {
            return Recurrence.none(anchorDate);
        }
        String[] tokens = args.trim().split("\\s+");
        if (tokens.length != 2) {
            throw errorType.createException();
        }

        try {
            int freq = Integer.parseInt((tokens[1]));
            assert freq > 0 : "Recurrence frequency must be positive";
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

    /**
     * Parses a recurrence pattern from a JSON object representation.
     * The JSON object must contain a "recurrence" field with nested "type" and "count" properties.
     * <p>
     * <strong>Expected JSON Structure:</strong>
     * <pre>{@code
     * {
     *   "recurrence": {
     *     "type": "WEEKLY",
     *     "count": "2"
     *   }
     * }
     * }</pre>
     *
     * @param obj        the JSON object containing recurrence data
     * @param anchorDate the reference date for calculating date of last recurrence
     * @return a {@link Recurrence} object representing the parsed pattern
     * @throws FileContentException if the JSON structure is invalid, required fields are missing,
     *                              the recurrence type is unrecognized, or the count cannot be parsed as an integer
     * @see Recurrence#of(RecurrenceType, int, LocalDate)
     * @see SimpleJsonObject
     */
    public static Recurrence parse(SimpleJsonObject obj,
                                   LocalDate anchorDate)
            throws FileContentException {
        Object recurObj = obj.get("recurrence");
        if (!(recurObj instanceof SimpleJsonObject recJson)) {
            throw new FileContentException(FileContentException.ErrorType.INVALID_INPUT);
        }
        String recType = recJson.requireNonEmpty("type").toUpperCase();
        int freq = Integer.parseInt(recJson.requireNonEmpty("count"));
        assert freq > 0 : "Recurrence count must be positive"; // if that’s a domain invariant
        return Recurrence.of(RecurrenceType.valueOf(recType), freq, anchorDate);
    }
}
