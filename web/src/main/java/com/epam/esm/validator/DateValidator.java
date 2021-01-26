package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;

public class DateValidator {

    public static void fieldValidate(String fieldValue, String fieldName, String pattern) throws ValidationException {

        if (fieldValue != null && !fieldValue.matches(pattern)) {
            throw new ValidationException("Invalid field:" + fieldName);
        }
    }
}
