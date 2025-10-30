package ui.cli;

import java.util.ArrayList;
import java.util.List;

import message.Message;

/**
 * Handles console output formatting for MeeBot responses with right-aligned layout
 * and automatic text wrapping.
 */
public class MessageRenderer {
    private final int maxWidth;
    private final int consoleWidth;

    /**
     * @param maxWidth     maximum characters per line for message content
     * @param consoleWidth total console width for right-alignment calculation
     */
    public MessageRenderer(int maxWidth, int consoleWidth) {
        this.maxWidth = maxWidth;
        this.consoleWidth = consoleWidth;
    }

    /**
     * Displays messages in a right-aligned block with "MeeBot:" header.
     * Preserves original line breaks while applying word wrapping.
     */
    public void render(Message msg) {
        String content = msg.message();

        // Calculate padding to right-align message block within console
        int blockPadding = consoleWidth - maxWidth;
        System.out.println("\n" + " ".repeat(Math.max(blockPadding, 0)) + "MeeBot:");

        // Process each line separately to preserve intentional line breaks
        String[] sentences = content.split("\n");
        for (String sentence : sentences) {
            // Wrap each sentence and print each wrapped line
            for (String line : wrapText(sentence)) {
                System.out.println(" ".repeat(Math.max(blockPadding, 0)) + line);
            }
        }
    }

    /**
     * Wraps text to fit within a specified maximum line width.
     * Breaks text at word boundaries to avoid splitting words across lines.
     *
     * @param s The input text to be wrapped
     * @return A list of strings, each representing a line that fits within maxWidth
     */
    private List<String> wrapText(String s) {
        List<String> lines = new ArrayList<>();
        String[] words = s.split(" ");    // splits string into individual words
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            // Check if adding this word would exceed max line width
            // +1 accounts for the space before the word
            if (line.length() + word.length() + 1 > maxWidth) {
                // Case: current line is full, save it to List and start a new line with this word
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                // Case: word fits into current line, only add a space before appending word if line is not empty
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        // Add the last line with content
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }

        return lines;
    }
}
