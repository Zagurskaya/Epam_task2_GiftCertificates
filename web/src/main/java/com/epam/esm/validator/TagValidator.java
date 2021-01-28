package com.epam.esm.validator;

import com.epam.esm.request.TagRequest;
import org.springframework.stereotype.Component;

@Component
public class TagValidator extends FieldsValidator {

    public void createValidate(TagRequest tag) {
        validateNull(tag.getName(), "name");
    }
}
