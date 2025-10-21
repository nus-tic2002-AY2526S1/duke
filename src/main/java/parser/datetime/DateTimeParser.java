package parser.datetime;

import java.util.List;

import exception.InvalidDateTimeException;
import exception.InvalidDateTimeException.ErrorType;

/**
 * Facade for parsing date-time strings using multiple strategies in sequence.
 * <p>
 * This parser attempts to parse input strings by delegating to a chain of
 * {@link DateTimeStrategy} implementations in order:
 * <ol>
 *   <li>{@link StrictDateTimeStrategy} - strict pattern matching</li>
 *   <li>{@link NlpDateTimeStrategy} - natural language processing</li>
 * </ol>
 * The first strategy that successfully parses the input is used, and subsequent
 * strategies are not invoked.
 */
public final class DateTimeParser {
    private static final List<DateTimeStrategy> STRATEGIES = List.of(
            new StrictDateTimeStrategy(),
            new NlpDateTimeStrategy()
    );

    private DateTimeParser() {
    }

    /**
     * Parses a date-time string using all available strategies.
     * Strategies are tried in order until one successfully parses the input.
     * If no strategy can parse the input, an exception is thrown.
     *
     * @param input the date-time string to parse
     * @return a {@link ParsedDateTime} object containing the parsed date-time and metadata
     * @throws InvalidDateTimeException if no strategy can parse the input
     *                                  ({@link ErrorType#UNSUPPORTED_FORMAT}), or if the
     *                                  input contains invalid date-time values
     *                                  ({@link ErrorType#INVALID_DATETIME_VALUE})
     */
    public static ParsedDateTime parse(String input) throws InvalidDateTimeException {
        for (DateTimeStrategy strategy : STRATEGIES) {
            ParsedDateTime result = strategy.parse(input);
            if (result != null) {
                return result;
            }
        }
        throw new InvalidDateTimeException(ErrorType.UNSUPPORTED_FORMAT, input);
    }
}