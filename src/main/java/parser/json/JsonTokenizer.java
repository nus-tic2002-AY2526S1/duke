package parser.json;

import java.util.Map;
import java.util.function.BiFunction;

import exception.FileContentException;
import exception.FileContentException.ErrorType;

/**
 * A lexical analyzer (tokenizer) for JSON text that breaks input into discrete tokens.
 * <p>
 * This tokenizer processes JSON input character by character and produces a stream
 * of {@link JsonToken} objects representing the structural elements, literals, and
 * values found in the JSON text.
 * <p>
 * <strong>Supported JSON Elements:</strong>
 * <ul>
 *   <li>Structural characters: <code>{ } [ ] : ,</code></li>
 *   <li>String literals with basic escape sequence handling</li>
 *   <li>Numeric literals (integers and floating-point)</li>
 *   <li>Boolean literals: <code>true</code>, <code>false</code></li>
 *   <li>Null literal: <code>null</code></li>
 *   <li>Automatic whitespace skipping</li>
 * </ul>
 * <p>
 * <strong>Limitations:</strong>
 * <ul>
 *   <li>String escape sequence handling assumes no consecutive backslashes</li>
 *   <li>Number parsing accepts malformed numbers (validation left to parser)</li>
 *   <li>No validation of JSON structure (handled by parser layer)</li>
 * </ul>
 * <p>
 * <em>Solution adapted from:
 * <a href="https://www.sunshine2k.de/articles/coding/jsonparser/simplejsonparser.html">
 * Simple JSON Parser Tutorial</a></em></p>
 *
 * @see JsonToken
 * @see JsonTokenType
 * @see SimpleJsonParser
 */
public final class JsonTokenizer {
    /**
     * Lookup table for single-character JSON tokens.
     * Maps characters to functions that create the appropriate token and advance position.
     */
    private static final Map<Character,
            BiFunction<JsonTokenizer, Character, JsonToken>> SINGLE_CHAR_LOOKUP =
            Map.of(
                    '{', (t, c) -> {
                        t.pos++;
                        return new JsonToken(JsonTokenType.CURLY_OPEN, "{");
                    },
                    '}', (t, c) -> {
                        t.pos++;
                        return new JsonToken(JsonTokenType.CURLY_CLOSE, "}");
                    },
                    '[', (t, c) -> {
                        t.pos++;
                        return new JsonToken(JsonTokenType.SQUARE_OPEN, "[");
                    },
                    ']', (t, c) -> {
                        t.pos++;
                        return new JsonToken(JsonTokenType.SQUARE_CLOSE, "]");
                    },
                    ':', (t, c) -> {
                        t.pos++;
                        return new JsonToken(JsonTokenType.COLON, ":");
                    },
                    ',', (t, c) -> {
                        t.pos++;
                        return new JsonToken(JsonTokenType.COMMA, ",");
                    }
            );
    /**
     * Lookup table for JSON literal tokens (true, false, null). Maps the first character
     * to functions that check for complete literals, create tokens and advance position.
     */
    private static final Map<Character,
            BiFunction<JsonTokenizer, Character, JsonToken>> LITERAL_LOOKUP =
            Map.of(
                    't', (t, c) -> {
                        if (t.input.startsWith("true", t.pos)) {
                            t.pos += 4;
                            return new JsonToken(JsonTokenType.TRUE, "true");
                        }
                        return null;
                    },
                    'f', (t, c) -> {
                        if (t.input.startsWith("false", t.pos)) {
                            t.pos += 5;
                            return new JsonToken(JsonTokenType.FALSE, "false");
                        }
                        return null;
                    },
                    'n', (t, c) -> {
                        if (t.input.startsWith("null", t.pos)) {
                            t.pos += 4;
                            return new JsonToken(JsonTokenType.NULL, "null");
                        }
                        return null;
                    }
            );

    private final String input;
    private int pos = 0;

    public JsonTokenizer(String input) {
        this.input = input.trim();
    }

    /**
     * Returns the next token from the input stream.
     * <p>
     * This method processes the input character by character, automatically skipping
     * whitespace and identifying JSON tokens. Each call advances the internal position
     * and returns the next logical token in the input.
     *
     * @return the next {@link JsonToken} in the input, or
     *         a token with type {@link JsonTokenType#EOF} when the end of input is reached
     * @throws FileContentException if an unexpected character is encountered that doesn't match any token pattern
     */
    public JsonToken nextToken() throws FileContentException {
        skipWhitespace();
        if (pos >= input.length()) {
            return new JsonToken(JsonTokenType.EOF, null);
        }

        char c = input.charAt(pos);

        // single-char lookup
        BiFunction<JsonTokenizer, Character, JsonToken> single = SINGLE_CHAR_LOOKUP.get(c);
        if (single != null) return single.apply(this, c);

        // literal lookup
        BiFunction<JsonTokenizer, Character, JsonToken> literal = LITERAL_LOOKUP.get(c);
        if (literal != null) {
            JsonToken token = literal.apply(this, c);
            if (token != null) return token;
        }
        // string
        if (c == '"') return stringToken();
        // number
        if (isDigit(c) || c == '-') return numberToken();

        throw new FileContentException(ErrorType.INVALID_INPUT);
    }

    /**
     * Parses a JSON string literal starting at the current position.
     * <p>
     * Expects the current position to be at an opening double quote and processes
     * all characters until an unescaped closing quote is found. The method handles
     * basic escape sequences but assumes no consecutive backslashes (e.g., {@code \\\\}).
     * <p>
     * <strong>Usage Note:</strong> This implementation has simplified escape handling
     * that may not correctly process all valid JSON escape sequences.
     *
     * @return a {@link JsonToken} of type {@link JsonTokenType#STRING} containing the parsed string content
     */
    private JsonToken stringToken() {
        ++pos;  // skip opening quote
        StringBuilder sb = new StringBuilder();
        while (pos < input.length()) {
            char c = input.charAt(pos++);
            if (c == '"' && input.charAt(pos - 2) != '\\') {
                break;  // break out of the loop only if hit an unescaped closing quote
            }
            sb.append(c);
        }
        return new JsonToken(JsonTokenType.STRING, sb.toString());
    }

    /**
     * Parses a JSON numeric literal starting at the current position.
     * <p>
     * Processes consecutive characters that can appear in JSON numbers:
     * digits (0-9), decimal point (.), and minus sign (-). The method does not
     * validate the numeric format; malformed numbers like {@code "--1.2.3"} will
     * be accepted and passed to the parser layer for validation.
     * <p>
     * Both integer and floating-point numbers are supported, following
     * the general JSON number format.
     *
     * @return a {@link JsonToken} of type {@link JsonTokenType#NUMBER} containing the parsed number string
     */
    private JsonToken numberToken() {
        int start = pos;
        while (pos < input.length() && (
                isDigit(input.charAt(pos))
                        || input.charAt(pos) == '.'
                        || input.charAt(pos) == '-')) {
            pos++;
        }
        String numStr = input.substring(start, pos);
        return new JsonToken(JsonTokenType.NUMBER, numStr);
    }

    /**
     * Advances the position past any whitespace characters.
     * <p>
     * Uses {@link Character#isWhitespace(char)} to identify whitespace,
     * which includes spaces, tabs, newlines, and other Unicode whitespace characters.
     * This method is called automatically by {@link #nextToken()} before token recognition.
     */
    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
