package com.golden.util;

import com.golden.exceptions.validationErrors.IllegalArgumentException;
import com.golden.exceptions.ParseException;
import com.golden.exceptions.parseErrors.MissingArgumentException;
import com.golden.task.Priority;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHelper {
    private static final Pattern WS = Pattern.compile("\\s+");
    // Finds the *first* " /<non-space>" boundary
    private static final Pattern FIRST_SLASH_SECTION = Pattern.compile("\\s/(?=\\S)");
    // Splits remaining text *at* every " /<non-space>" boundary, *keeping* the slash argument
    private static final Pattern NEXT_SLASH_SECTIONS  = Pattern.compile("(?=\\s/\\S)");

    private ParseHelper(){}

    /** Check the given (String) array for the exact number of parts required.
     *
     *  @return {@code Throwable} errors if the required number of arguments is flouted  */
    public static void requireArgs(String[] parts, int n, String what) throws ParseException {
        if (parts == null) {
            throw new ParseException(String.format("%s expected (no arguments provided).", what));
        }
        if (parts.length > n){
            throw new ParseException(String.format("too many arguments. %s", what));
        }
        if (parts.length < n) {
            throw new MissingArgumentException( what + " is missing!!");
        }
    }

    /** Parse the String input and split on any whitespace (spaces, tabs, newlines), collapsing runs.
     *
     *  @return an array of String(s) of this {@code Throwable} instance (which may be {@code null}).  */
    public static String[] splitOnWhitespaces(String input) throws MissingArgumentException {
        if (input == null || input.isBlank()){
            throw new MissingArgumentException("Expected some input!");
        }
        return WS.split(input.trim());
    }

    /**
     * Parse the String input and split on:
     *  [0]: leading text before any "/section"
     *  [1..]: each "/section" including the leading slash, e.g. "/from [date]"
     *
     *  If no "/section" is found, returns a single-element array.
     *
     *  @return an array of String(s) of this {@code Throwable} instance (which may be {@code null}).
     */
    public static String[] splitOnSlashSections(String input) throws MissingArgumentException {
        if (input == null) {
            throw new MissingArgumentException("Expected some input!");
        }
        String s = input.trim();
        if (s.isEmpty()){
            throw new MissingArgumentException("Expected some input!");
        }

        Matcher m = FIRST_SLASH_SECTION.matcher(s);
        if (!m.find()) {
            // no "/section" found → whole string is the head
            return new String[]{s};
        }

        // head is everything before the first " /<non-space>"
        String head = s.substring(0, m.start()).trim();
        // tail starts AT the slash (drop the preceding space, keep the slash)
        String tail = s.substring(m.start() + 1); // +1 skips the space before '/'

        // Now split tail into sections at each " /<non-space>" boundary (lookahead keeps the slash)
        String[] rawSections = NEXT_SLASH_SECTIONS.split(tail);

        // Clean up: ensure each section starts with "/", trim trailing spaces
        List<String> out = new ArrayList<>(1 + rawSections.length);
        if (!head.isEmpty()) out.add(head);

        for (String sec : rawSections) {
            String t = sec.trim();
            if (t.isEmpty()) continue;
            // Guarantee a leading slash
            if (t.charAt(0) != '/') t = "/" + t.replaceFirst("^\\s*/?", "");
            out.add(t);
        }

        return out.toArray(new String[0]);
    }

    /** Returns trimmed line that is non-null and not blank;
     * @return the trimmed string of this {@code Throwable} instance (which may be {@code null}).
     * */
    public static String isNonBlank(String s, String message) throws MissingArgumentException {
        if (s == null || s.trim().isEmpty()) {
            throw new MissingArgumentException(message);
        }
        return s.trim();
    }

    /** Convert the String input into a {@code LocalDate} object.
     *
     *  @return a {@code LocalDate} object of this {@code Throwable} instance (which may be {@code null}).  */
    public static LocalDate convertStringToDate (String dateString) throws ParseException {
        if (ValidationHelper.isValidIsoDate(dateString)){
            try {
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                throw new ParseException(dateString);
            }
        } else
            throw new ParseException(String.format(
                    "'%s'. \nPlease enter a valid future date in this format: yyyy-MM-dd.", dateString));
    }

    /** Convert the string input of priority into a validated {@code Priority} object.
     *
     *  @return a {@code Priority} object of this {@code Throwable} instance (which may be {@code null}).
     */
    public static Priority convertStringToPriority(String priorityString) throws IllegalArgumentException {
        assert priorityString != null && !priorityString.isBlank();
        String normalisedPriority;
        normalisedPriority = priorityString.trim().toUpperCase();

        for (Priority p : Priority.values()){
            if (p.name().equals(normalisedPriority)){
                return p;
            }
        }
        throw new IllegalArgumentException(String.format(
                "invalid priority: '%s'.", priorityString));
    }

}