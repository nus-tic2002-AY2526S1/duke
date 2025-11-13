package logic.parser.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import common.exception.FileContentException;
import common.exception.FileContentException.ErrorType;

/**
 * A simple JSON parser that parses a restricted subset of JSON format.
 * This parser expects the root element to be an array of objects and assumes
 * all object values are strings (despite supporting other types internally).
 * <p>
 * The parser uses a tokenizer-based approach with recursive descent parsing
 * to handle JSON structures. It throws {@link FileContentException} for invalid
 * JSON format or unexpected tokens.
 * <p>
 * <em>Solution adapted from:
 * <a href="https://www.sunshine2k.de/articles/coding/jsonparser/simplejsonparser.html">
 * Simple JSON Parser Tutorial</a></em>
 *
 * @see JsonTokenizer
 * @see SimpleJsonObject
 */
public final class SimpleJsonParser {
    private final JsonTokenizer tokenizer;
    private JsonToken current;

    /**
     * Creates a parser bound to the given tokenizer.
     * <p>
     * The constructor immediately fetches the first token so that {@code current}
     * always hold a valid token before parsing begins and allows parsing methods
     * to consistently inspect or consume {@code current}.
     *
     * @param tokenizer the tokenizer providing JSON tokens
     */
    public SimpleJsonParser(JsonTokenizer tokenizer) throws FileContentException {
        this.tokenizer = tokenizer;
        this.current = tokenizer.nextToken();
        assert current.type() != null : "Token type must not be null";
    }

    /**
     * Parses the complete JSON input, expecting an array the root element.
     * <p>
     * This method assumes the JSON structure follows the pattern:
     * <pre>[{"key1": "value1", "key2": "value2"}, {...}, ...]</pre>
     *
     * @return a list of {@link SimpleJsonObject} instances representing the parsed objects
     * @throws FileContentException if the JSON format is invalid or doesn't start with an array
     */
    public List<SimpleJsonObject> parseJson() throws FileContentException {
        List<SimpleJsonObject> result = new ArrayList<>();
        consume(JsonTokenType.SQUARE_OPEN);

        while (current.type() != JsonTokenType.SQUARE_CLOSE) {
            result.add(parseObject());
            if (current.type() == JsonTokenType.COMMA) {
                consume(JsonTokenType.COMMA);
            } else {
                break;
            }
        }

        assert result.stream().allMatch(Objects::nonNull) : "Parsed objects must never be null";
        consume(JsonTokenType.SQUARE_CLOSE);
        return result;
    }

    /**
     * Parses a single JSON object, extracting key-value pairs.
     * Expects the object to follow standard JSON object syntax:
     * <pre>{"key1": value1, "key2": value2, ...}</pre>
     *
     * @return a new {@link SimpleJsonObject} containing the parsed key-value pairs
     * @throws FileContentException if the object format is invalid
     */
    private SimpleJsonObject parseObject() throws FileContentException {
        SimpleJsonObject obj = new SimpleJsonObject();
        consume(JsonTokenType.CURLY_OPEN);

        while (current.type() != JsonTokenType.CURLY_CLOSE) {
            String key = current.value();
            assert key != null && !key.isEmpty() : "JSON object key must be non-null and non-empty";
            consume(JsonTokenType.STRING);
            consume(JsonTokenType.COLON);

            Object value = parseValue();
            obj.put(key, value);
            if (current.type() == JsonTokenType.COMMA) {
                consume(JsonTokenType.COMMA);
            } else {
                break;
            }
        }

        consume(JsonTokenType.CURLY_CLOSE);
        return obj;
    }

    /**
     * Parses a JSON value, which can be a string, number, boolean, null, or nested object.
     * <p>
     * Supported value types:
     * <ul>
     *   <li>Strings - returned as {@link String}</li>
     *   <li>Numbers - returned as {@link Integer}</li>
     *   <li>Booleans - returned as {@link Boolean}</li>
     *   <li>null - returned as null</li>
     *   <li>Objects - returned as {@link SimpleJsonObject} (recursive parsing)</li>
     * </ul>
     * <p>
     * Arrays are not supported and will cause a {@link FileContentException}.</p>
     *
     * @return the parsed value as an appropriate Java object
     * @throws FileContentException if the current token represents an unsupported value type
     */
    private Object parseValue() throws FileContentException {
        switch (current.type()) {
        case STRING:
            String s = current.value();
            consume(JsonTokenType.STRING);
            return s;
        case NUMBER:
            String n = current.value();
            consume(JsonTokenType.NUMBER);
            return Integer.valueOf(n);
        case TRUE:
            consume(JsonTokenType.TRUE);
            return Boolean.TRUE;
        case FALSE:
            consume(JsonTokenType.FALSE);
            return Boolean.FALSE;
        case NULL:
            consume(JsonTokenType.NULL);
            return null;
        case CURLY_OPEN:
            return parseObject(); // recursive nested object
        default:
            throw new FileContentException(FileContentException.ErrorType.INVALID_JSON_FORMAT);
        }
    }

    /**
     * Consumes the current token, verifying it matches the expected type.
     * Advances to the next token from the tokenizer.
     *
     * @param expected the expected token type
     * @throws FileContentException if the current token doesn't match the expected type
     */
    private void consume(JsonTokenType expected) throws FileContentException {
        if (current.type() != expected) {
            throw new FileContentException(ErrorType.INVALID_JSON_FORMAT);
        }
        current = tokenizer.nextToken();
        assert current != null : "Tokenizer must not return null";
    }
}
