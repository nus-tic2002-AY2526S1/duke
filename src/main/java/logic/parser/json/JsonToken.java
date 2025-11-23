package logic.parser.json;

/**
 * Represents a single token in JSON input, consisting of a type and value.
 * <p>
 * Used during JSON lexical analysis to break down JSON text into meaningful units
 * such as braces, brackets, strings, numbers, and keywords.
 *
 * @param type  the type of the JSON token
 * @param value the token's value, or null for structural tokens (braces, brackets, etc.)
 */
public record JsonToken(JsonTokenType type, String value) {

    @Override
    public String toString() {
        return type + (value != null ? " : " + value : "");
    }
}
