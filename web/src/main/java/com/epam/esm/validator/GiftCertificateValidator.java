package com.epam.esm.validator;

import com.epam.esm.model.GiftCertificateDTO;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator extends FieldValidator {

    public void validate(GiftCertificateDTO giftCertificateDTO) {
        validatePrice(giftCertificateDTO.getPrice());
        validateDuration(giftCertificateDTO.getDuration());
    }

    public void validateUpdatePart(GiftCertificateDTO giftCertificateDTO) {
        if (giftCertificateDTO.getPrice() != null) {
            validatePrice(giftCertificateDTO.getPrice());
        }

        if (giftCertificateDTO.getDuration() != null) {
            validateDuration(giftCertificateDTO.getDuration());
        }
    }
}
