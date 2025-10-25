package parser.commandargs;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;

/**
 * Utility class providing static utility methods for tokenizing and parsing
 * command-line arguments using regex patterns.
 */
public final class ArgTokenizer {
    private ArgTokenizer() {
    }

    /**
     * Tokenizes a string into an array of tokens using the specified regex pattern.
     * <p>
     * Validates that the input string matches the expected pattern and contains
     * the required number of groups. Each captured token is trimmed of whitespace
     * and validated to be non-empty.
     *
     * @param args          the input string to tokenize (must not be null or blank)
     * @param pattern       the regex pattern for tokenization
     * @param requiredToken the expected number of tokens to extract
     * @param errorType     the error type to throw if tokenization fails
     * @return an array of trimmed, non-empty tokens
     * @throws InvalidTaskFormatException if the input is null/blank, doesn't match
     *                                    the pattern, has wrong number of groups, or contains empty tokens
     */
    public static String[] tokenize(String args, Pattern pattern,
                                    int requiredToken, ErrorType errorType)
            throws InvalidTaskFormatException {

        Objects.requireNonNull(args, "Arguments must not be null");
        Objects.requireNonNull(pattern, "Pattern must not be null");
        assert requiredToken > 0 : "requiredToken must be greater than 0";

        String REPEAT_DELIMITER = "/repeat";
        String[] split = args.trim().split(REPEAT_DELIMITER, 2);
        String cmdArg = split[0].trim();
        String repeatArg = (split.length > 1) ? split[1].trim() : "none";

        Matcher matcher = pattern.matcher(cmdArg);
        if (!matcher.matches()) {
            throw errorType.createException();
        }
        String[] tokens = extractTokens(matcher, requiredToken, errorType);
        tokens[requiredToken] = repeatArg;
        return tokens;
    }

    private static String[] extractTokens(
            Matcher matcher, int requiredToken, ErrorType errorType)
            throws InvalidTaskFormatException {

        // add 1 for optional repeat field
        String[] tokens = new String[requiredToken + 1];
        for (int i = 0; i < requiredToken; i++) {
            String value = matcher.group(i + 1).trim();
            if (value.isEmpty()) {
                throw errorType.createException();
            }
            tokens[i] = value;
        }
        return tokens;
    }
}
