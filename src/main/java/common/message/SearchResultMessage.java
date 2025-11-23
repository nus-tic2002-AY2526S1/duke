package common.message;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.task.ReadOnlyTask;

public class SearchResultMessage implements Message {
    private final List<ReadOnlyTask> results;
    private final String[] keywords;

    /**
     * @param results  filtered task list (may be empty); this list is copied defensively
     * @param keywords search keyword for display
     */
    public SearchResultMessage(List<ReadOnlyTask> results, String[] keywords) {
        this.results = List.copyOf(results);
        this.keywords = keywords;
    }

    @Override
    public String message() {
        if (results.isEmpty()) {
            return """
                    There's no easy way to say this…
                    Mee searched high and low but found nada.
                    Try searching for something else.
                    """;
        }

        StringBuilder sb = new StringBuilder("Mee found these tasks:\n");

        for (int i = 0; i < results.size(); i++) {
            ReadOnlyTask task = results.get(i);
            String desc = task.getDescription();
            String highlighted = highlight(desc, keywords);

            // Replace search term with highlighted term
            String rendered = task.toString().replaceFirst(
                    Pattern.quote(desc),
                    Matcher.quoteReplacement(highlighted)
            );
            sb.append(String.format("%d. %s\n", i + 1, rendered));
        }
        return sb.toString().trim();
    }

    /* Implementation generated with the help of AI assistance */
    private String highlight(String text, String[] terms) {
        assert text != null : "text to highlight should not be null";
        assert terms != null : "terms should not be null";

        String result = text;
        for (String term : terms) {
            Pattern p = Pattern.compile("(?i)" + Pattern.quote(term));
            Matcher m = p.matcher(result);
            StringBuilder sb = new StringBuilder();
            while (m.find()) {
                m.appendReplacement(sb, "[" + m.group() + "]");
            }
            m.appendTail(sb);
            result = sb.toString();
        }
        return result;
    }
}
