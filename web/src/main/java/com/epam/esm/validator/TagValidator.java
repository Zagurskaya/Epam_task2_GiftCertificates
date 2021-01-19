package com.epam.esm.validator;

import com.epam.esm.model.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagValidator extends DateValidator {

    private static final String INITIALS_SYMBOLS_AND_NUMBER_VALIDATION_REGEX = "[A-Za-z0-9]{1,}";

    public void createValidate(TagDTO tagDTO) {
        if (tagDTO.getName() == null) {
            throw new IllegalArgumentException("the field cannot be null : name");
        } else {
            fieldValidate(tagDTO.getName(), "name", INITIALS_SYMBOLS_AND_NUMBER_VALIDATION_REGEX);
        }

    }
}
