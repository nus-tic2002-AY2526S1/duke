package parser.json;

public record JsonToken(JsonTokenType type, String value) {

    @Override
    public String toString() {
        return type + (value != null ? " : " + value : "");
    }
}
