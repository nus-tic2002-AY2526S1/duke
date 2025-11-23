package logic.parser.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import common.exception.FileContentException;
import common.exception.FileContentException.ErrorType;

/**
 * A simple representation of a JSON object that stores key-value pairs.
 * This class serves as a simplified container for holding JSON object data
 * as a collection of string keys mapped to object values.
 * <p>
 * <strong>Supported value types include:</strong>
 * {@link String} {@link Number} {@link Boolean} {@code null} and
 * {@link SimpleJsonObject} for nested JSON objects
 */
public class SimpleJsonObject {
    private final Map<String, Object> fields = new HashMap<>();

    /**
     * Stores a single member (key-value pair) of an JSON object.
     */
    public void put(String key, Object value) {
        fields.put(key, value);
    }

    /**
     * Retrieves the raw value associated with the given key.
     * <p>
     * The return type is {@link Object} and callers are expected to cast
     * the result to the appropriate type:
     * <ul>
     *   <li>Cast to {@link SimpleJsonObject} for nested objects.</li>
     *   <li>Cast to {@link Integer} for numbers.</li>
     *   <li>Cast to {@link Boolean} for true/false values.</li>
     * </ul>
     *
     * @param key the field name (key) to lookup
     * @return the associated value, or {@code null} if not present
     */
    public Object get(String key) {
        return fields.get(key);
    }

    /**
     * Retrieves the value for the specified key and ensures it is non-empty.
     *
     * @param key the key whose value to retrieve
     * @return the non-blank string value associated with the key
     * @throws FileContentException if the key is missing or its value is blank
     */
    public String requireNonEmpty(String key) throws FileContentException {
        return Optional.ofNullable(this.get(key))
                .map(Object::toString)
                .filter(s -> !s.isBlank())
                .orElseThrow(() -> new FileContentException(ErrorType.INVALID_INPUT)
                );
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
