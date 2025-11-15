package com.golden.util;

import com.golden.exceptions.validationErrors.IllegalArgumentException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationHelper {
    // override default public constructor method and makes it private; prevents instantiation by other classes
    private ValidationHelper(){}

    /** Returns True if given number is non-null and not blank, and in between lower limit
     *  and upper limit is non-null and not blank;
     *
     *  @return boolean value of this {@code Throwable} instance (which may be {@code null}).
     * */
    public static boolean isNumberInRange(int upperLimit, int number )
            throws IllegalArgumentException {
        if (number < 1 || number > upperLimit){
            throw new IllegalArgumentException(String.format("'%d'. Number is out of range.", number));
        }
        return true;
    }

    /** Returns True if date String is non-null, not blank and a valid date observing yyyy-MM-dd format.
     *
     *  @return boolean value of this {@code Throwable} instance (which may be {@code null}).
     * */
    public static boolean isValidIsoDate(String dateString) {
        if (dateString == null || dateString.isBlank()){
            return false;
        }
        try {
            // ISO_LOCAL_DATE expects exactly yyyy-MM-dd format
            LocalDate parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

            // Valid only if it's after today's date (future date)
            return parsedDate.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /** Checks that the order of the two {@code LocalDate} objects given is valid.
     *
     *  @return a {@code Throwable} instance when the order of the dates is flouted.
     * */
    public static void validateDateOrder(LocalDate before, LocalDate after) throws IllegalArgumentException {
        if (before.isAfter(after)){
            throw new IllegalArgumentException(String.format(
                    "'%s' is NOT before '%s'!!",
                    FormatHelper.displayAsMMMdyyyy(before), FormatHelper.displayAsMMMdyyyy(after)
            ));
        }
    }
}
