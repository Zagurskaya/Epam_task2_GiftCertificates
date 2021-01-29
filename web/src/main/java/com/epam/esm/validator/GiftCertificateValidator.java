package com.epam.esm.validator;

import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.GiftCertificateDTO;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator extends FieldValidator {

    public void validate(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getPrice() != null &&
                giftCertificateDTO.getDuration() != null &&
                giftCertificateDTO.getName() != null &&
                giftCertificateDTO.getDescription() != null) {
            validateString(giftCertificateDTO.getName());
            validateString(giftCertificateDTO.getDescription());
            validatePrice(giftCertificateDTO.getPrice());
            validateDuration(giftCertificateDTO.getDuration());
        } else {
            throw new ValidationException("incorrect value");
        }
    }

    public void validateUpdatePath(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getName() != null) {
            validateString(giftCertificateDTO.getName());
        }
        if (giftCertificateDTO.getDescription() != null) {
            validateString(giftCertificateDTO.getDescription());
        }
        if (giftCertificateDTO.getPrice() != null) {
            validatePrice(giftCertificateDTO.getPrice());
        }
        if (giftCertificateDTO.getDuration() != null) {
            validateDuration(giftCertificateDTO.getDuration());
        }
    }
}
