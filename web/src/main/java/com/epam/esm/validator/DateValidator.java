package com.epam.esm.validator;

public class DateValidator {

    public static void fieldValidate(String fieldValue, String fieldName, String pattern) throws IllegalArgumentException {

        if (fieldValue != null && !fieldValue.matches(pattern)) {
            throw new IllegalArgumentException("Invalid field:" + fieldName);
        }
    }
}
