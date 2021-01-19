package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.model.TagDTO;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator extends DateValidator {

    public static final String ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN = "[a-zA-Z0-9\\s_-]{1,}";
    private static final String MARK_VALIDATION_REGEX = "[0-9]{1,}";

    public void createValidate(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getName() == null) {
            throw new IllegalArgumentException("the field cannot be null : name");
        } else {
            fieldValidate(giftCertificateDTO.getName(), "name", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

        if (giftCertificateDTO.getDescription() == null) {
            throw new IllegalArgumentException("the field cannot be null : description");
        } else {
            fieldValidate(giftCertificateDTO.getDescription(), "description", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

        if (giftCertificateDTO.getPrice() == null) {
            throw new IllegalArgumentException("the field cannot be null : price");
        } else {
            fieldValidate(giftCertificateDTO.getPrice().toString(), "price", MARK_VALIDATION_REGEX);
        }

        if (giftCertificateDTO.getDuration() == null) {
            throw new IllegalArgumentException("the field cannot be null : duration");
        } else {
            fieldValidate(giftCertificateDTO.getDuration().toString(), "duration", MARK_VALIDATION_REGEX);
        }
    }

    public void updateValidate(GiftCertificateDTO giftCertificateDTO) {
        createValidate(giftCertificateDTO);
        if (giftCertificateDTO.getCreationDate() == null) {
            throw new IllegalArgumentException("the field cannot be null : duration");
        }
    }

    public void updatePartValidate(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getName() != null) {
            fieldValidate(giftCertificateDTO.getName(), "name", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

        if (giftCertificateDTO.getDescription() != null) {
            fieldValidate(giftCertificateDTO.getDescription(), "description", ALPHABET_NUMBER_UNDERSCORE_MINUS_BLANK_VALIDATE_PATTERN);
        }

        if (giftCertificateDTO.getPrice() != null) {
            fieldValidate(giftCertificateDTO.getPrice().toString(), "price", MARK_VALIDATION_REGEX);
        }

        if (giftCertificateDTO.getDuration() != null) {
            fieldValidate(giftCertificateDTO.getDuration().toString(), "duration", MARK_VALIDATION_REGEX);
        }
    }
}
