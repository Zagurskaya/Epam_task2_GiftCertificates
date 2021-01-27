package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificateDTO;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator extends FieldValidator {

    public static final String ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN = "[a-zA-Z0-9\\s_-]{1,}";
    private static final String MARK_VALIDATION_REGEX = "[0-9]{1,}";
    private static final String DATE_VALIDATION_REGEX = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$";

    public void validateFieldsCreateOperation(GiftCertificateDTO giftCertificateDTO) {
        validate(giftCertificateDTO.getName(), "name", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        validate(giftCertificateDTO.getDescription(), "description", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        validate(giftCertificateDTO.getPrice().toString(), "price", MARK_VALIDATION_REGEX);
        validate(giftCertificateDTO.getDuration().toString(), "duration", MARK_VALIDATION_REGEX);
    }

    public void validateFieldsUpdateOperation(GiftCertificateDTO giftCertificateDTO) {
        validateFieldsCreateOperation(giftCertificateDTO);
        validate(giftCertificateDTO.getCreationDate().toString(), "creation date", DATE_VALIDATION_REGEX);
    }

    public void validateFieldsUpdatePartOperation(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getName() != null) {
            validate(giftCertificateDTO.getName(), "name", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

        if (giftCertificateDTO.getDescription() != null) {
            validate(giftCertificateDTO.getDescription(), "description", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

        if (giftCertificateDTO.getPrice() != null) {
            validate(giftCertificateDTO.getPrice().toString(), "price", MARK_VALIDATION_REGEX);
        }

        if (giftCertificateDTO.getDuration() != null) {
            validate(giftCertificateDTO.getDuration().toString(), "duration", MARK_VALIDATION_REGEX);
        }
        if (giftCertificateDTO.getCreationDate() != null) {
            validate(giftCertificateDTO.getCreationDate().toString(), "creation date", DATE_VALIDATION_REGEX);
        }
        if (giftCertificateDTO.getLastUpdateDate() != null) {
            validate(giftCertificateDTO.getLastUpdateDate().toString(), "last update date", DATE_VALIDATION_REGEX);
        }
    }
}
