package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;

public class FieldValidator {

    public static void validate(String fieldValue, String fieldName, String pattern) throws ValidationException {

        if (fieldValue != null && !fieldValue.matches(pattern)) {
            throw new ValidationException("Invalid field:" + fieldName);
        }
    }
}
