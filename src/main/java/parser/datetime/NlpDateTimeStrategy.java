package parser.datetime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.zoho.hawking.HawkingTimeParser;
import com.zoho.hawking.datetimeparser.configuration.HawkingConfiguration;
import com.zoho.hawking.language.english.model.DatesFound;


/**
 * Natural language processing strategy for parsing human-readable date-time expressions.
 * <p>
 * This strategy uses the Hawking time parser library to interpret natural language inputs
 * such as "tomorrow at 3pm", "next Monday", or "in 2 hours". It is more flexible than
 * strict pattern matching but may have varying accuracy depending on the complexity of the input.
 * <p>
 * The parser uses the system's default timezone for all conversions. Time information
 * is extracted from the end of the parsed date range when available.
 *
 * @see StrictDateTimeStrategy
 */
public final class NlpDateTimeStrategy implements DateTimeStrategy {
    private static final HawkingTimeParser HAWKING = new HawkingTimeParser();
    private static final HawkingConfiguration CONFIG;

    static {
        CONFIG = new HawkingConfiguration();
        CONFIG.setTimeZone(ZoneId.systemDefault().toString());
    }

    /**
     * Attempts to parse a natural language date-time expression.
     * <p>
     * Examples of supported inputs:
     * <ul>
     *   <li>"tomorrow at 3pm"</li>
     *   <li>"in 2 days"</li>
     *   <li>"next Monday"</li>
     *   <li>"first Monday of next month"</li>
     * </ul>
     *
     * @param dateTimeString the natural language date-time string to parse
     * @return a {@link ParsedDateTime} object if parsing succeeds, or {@code null}
     *         if the input cannot be interpreted as a date-time expression
     */
    @Override
    public ParsedDateTime parse(String dateTimeString) {
        try {
            DatesFound found = HAWKING.parse(dateTimeString, new Date(), CONFIG, "eng");
            if (found != null && !found.getParserOutputs().isEmpty()) {
                var output = found.getParserOutputs().get(0);
                if (output.getDateRange() != null && output.getDateRange().getEnd() != null) {
                    LocalDateTime dt = LocalDateTime.ofInstant(
                            output.getDateRange().getEnd().toDate().toInstant(),
                            ZoneId.systemDefault()
                    );
                    return new ParsedDateTime(dt, Boolean.TRUE.equals(output.getIsExactTimePresent()));
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
