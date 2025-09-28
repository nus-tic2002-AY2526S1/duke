package parser.datetime;

import exception.InvalidDateTimeException;

/**
 * Strategy interface for parsing date-time strings into {@link ParsedDateTime} objects.
 * <p>
 * Implementations define different parsing approaches (e.g., strict pattern matching,
 * natural language processing) and return {@code null} if the input cannot be parsed
 * by that strategy.
 *
 * @see DateTimeParser
 * @see StrictDateTimeStrategy
 * @see NlpDateTimeStrategy
 */
public interface DateTimeStrategy {
    ParsedDateTime parse(String input) throws InvalidDateTimeException;
}
