package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FieldsValidator {

    public void validateNull(Object param, String paramName) {
        if (param == null || param.toString().isEmpty()) {
            throw new ValidationException("the field cannot be null :" + paramName);
        }
    }

    public void validateLong(String param, String paramName) {
        try {
            Long.parseLong(param);
        } catch (NumberFormatException e) {
            throw new ValidationException(" is invalid, it should be numeric field :" + paramName);
        }
    }

    public void validateDate(String param, String paramName) {
        try {
            LocalDateTime.parse(param, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new ValidationException(" format should be in yyyy-MM-dd HH:mm:ss field:" + paramName);

        }
    }

    public void validateDecimal(String param, String paramName) {
        try {
            new BigDecimal(param);
        } catch (NumberFormatException e) {
            throw new ValidationException(" is invalid, it should be numeric field:" + paramName);
        }
    }
}