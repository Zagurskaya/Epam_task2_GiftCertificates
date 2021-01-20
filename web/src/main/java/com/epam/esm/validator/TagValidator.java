package com.epam.esm.validator;

import com.epam.esm.model.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagValidator extends DateValidator {

    public static final String ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN = "[a-zA-Z0-9\\s_-]{1,}";

    public void createValidate(TagDTO tagDTO) {
        if (tagDTO.getName() == null) {
            throw new IllegalArgumentException("the field cannot be null : name");
        } else {
            fieldValidate(tagDTO.getName(), "name", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

    }
}
