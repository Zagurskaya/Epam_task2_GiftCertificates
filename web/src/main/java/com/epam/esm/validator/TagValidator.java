package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class TagValidator extends FieldValidator {

    public static final String ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN = "[a-zA-Z0-9\\s_-]{1,}";

    public void createValidate(TagDTO tagDTO) {
        if (tagDTO.getName() == null) {
            throw new ValidationException("the field cannot be null : name");
        } else {
            validate(tagDTO.getName(), "name", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

    }
}
