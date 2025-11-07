package common.message;

/**
 * Static farewell message displayed when the application exits.
 */
public class GoodbyeMessage implements Message {

    private static final String BYE_CONTENT =
            "Until we mee-t again, stay organized!";

    @Override
    public String message() {
        return BYE_CONTENT;
    }
}
