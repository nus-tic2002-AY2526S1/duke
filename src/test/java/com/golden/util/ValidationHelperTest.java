package com.golden.util;

import com.golden.exceptions.validationErrors.IllegalArgumentException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;


public class ValidationHelperTest {
//    @Test
//    public void dummyTest(){
//        assertEquals(2, 2);
//    }

    @Test
    public void isNumberInRange_success() throws Exception {
        // number at lower bound test
        assertEquals(true, ValidationHelper.isNumberInRange(10, 1));
        // number at upper bound test
        assertEquals(true, ValidationHelper.isNumberInRange(10, 10));
    }

    @Test
    public void isNumberInRange_exceptionThrown() {
        // number out of bounds
        try {
            assertEquals(false, ValidationHelper.isNumberInRange(10, -1));
            fail();     //if test reaches this line, test fails
        } catch (Exception e) {
            assertEquals("Illegal argument: '-1'. Number is out of range.", e.getMessage());
        }
    }

    @Test
    public void isValidIsoDate_success() {
        // valid date in yyyy-MM-dd format
        assertEquals(true, ValidationHelper.isValidIsoDate("2026-11-11"));

        // leap year date in yyyy-MM-dd format
        assertEquals(true, ValidationHelper.isValidIsoDate("2028-02-29"));
    }

    @Test
    public void isValidIsoDate_failure() {
        // valid date in wrong format
        assertEquals(false, ValidationHelper.isValidIsoDate("11-11-2026"));

        // valid date in wrong format v2
        assertEquals(false, ValidationHelper.isValidIsoDate("2026-Nov-11"));

        // invalid date in correct format
        assertEquals(false, ValidationHelper.isValidIsoDate("2026-13-11"));

        // invalid date - leap year day in wrong year
        assertEquals(false, ValidationHelper.isValidIsoDate("2026-02-29"));
    }

    @Test
    public void validateDateOrder_success() {
        LocalDate _11Nov2026 = LocalDate.of(2026, Month.NOVEMBER, 11);
        LocalDate _12Nov2026 = LocalDate.of(2026, Month.NOVEMBER, 12);
        LocalDate _01Jan2027 = LocalDate.of(2027, Month.JANUARY, 1);
        // valid date order within same year
        assertDoesNotThrow(() ->
                ValidationHelper.validateDateOrder(_11Nov2026, _12Nov2026)
        );
        // valid date order across two years
        assertDoesNotThrow(() ->
                ValidationHelper.validateDateOrder(_12Nov2026, _01Jan2027)
        );
    }

    @Test
    public void validateDateOrder_exceptionThrown() {
        LocalDate _11Nov2026 = LocalDate.of(2026, Month.NOVEMBER, 11);
        LocalDate _12Nov2026 = LocalDate.of(2026, Month.NOVEMBER, 12);
        LocalDate _01Jan2027 = LocalDate.of(2027, Month.JANUARY, 1);

        // order flouted -> IllegalArgumentException thrown
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                ValidationHelper.validateDateOrder(_12Nov2026, _11Nov2026)
        );

        String expected1 = String.format("Illegal argument: '%s' is NOT before '%s'!!",
                FormatHelper.displayAsMMMdyyyy(_12Nov2026),
                FormatHelper.displayAsMMMdyyyy(_11Nov2026));

        assertEquals(expected1, ex1.getMessage());

        // order flouted across years-> IllegalArgumentException thrown
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                ValidationHelper.validateDateOrder(_01Jan2027, _11Nov2026)
        );

        String expected2 = String.format("Illegal argument: '%s' is NOT before '%s'!!",
                FormatHelper.displayAsMMMdyyyy(_01Jan2027),
                FormatHelper.displayAsMMMdyyyy(_11Nov2026));

        assertEquals(expected2, ex2.getMessage());
    }

}