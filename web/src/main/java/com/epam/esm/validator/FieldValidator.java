package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;

import java.math.BigDecimal;

public class FieldValidator {

    public void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("incorrect value price");
        }
    }

    public void validateDuration(Long duration) {
        if (duration == null || duration <= 0) {
            throw new ValidationException("incorrect value duration");
        }
    }
}
