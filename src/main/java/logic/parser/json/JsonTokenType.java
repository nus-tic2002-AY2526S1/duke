package logic.parser.json;

/**
 * Enumeration representing the lexical elements that can appear in a JSON document,
 * including structural characters, literals, and data types. Each token type
 * corresponds to specific characters or patterns in the JSON specification.
 *
 * @see JsonTokenizer
 */
public enum JsonTokenType {
    CURLY_OPEN,     // {
    CURLY_CLOSE,    // }
    SQUARE_OPEN,    // [
    SQUARE_CLOSE,   // ]
    COLON,          // :
    COMMA,          // ,
    STRING,         // "..."
    NUMBER,         // 1234, -12.34
    TRUE,           // true
    FALSE,          // false
    NULL,           // null
    EOF             // end of input
}
