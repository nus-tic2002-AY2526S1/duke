package parser.commandargs;

import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing static utility methods for tokenizing and parsing
 * command-line arguments using regex patterns.
 */
public final class StringTokenizer {

    private StringTokenizer() {}

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

        String[] split = args.trim().split("/repeat", 2);
        Matcher matcher = pattern.matcher(split[0].trim());
        if (!matcher.matches()) {
            throw errorType.createException();
        }
        // add 1 for optional repeat field
        String[] tokens = new String[requiredToken + 1];
        for (int i = 0; i < requiredToken; i++) {
            tokens[i] = matcher.group(i + 1).trim();
            if (tokens[i].isEmpty()) throw errorType.createException();
        }
        tokens[requiredToken] = (split.length > 1 ? split[1].trim() : "none");
        return tokens;
    }
}
