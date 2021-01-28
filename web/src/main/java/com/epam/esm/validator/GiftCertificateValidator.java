package com.epam.esm.validator;

import com.epam.esm.request.GiftCertificateRequest;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator extends FieldsValidator {

    public void validateCreate(GiftCertificateRequest giftCertificate) {
        validateNull(giftCertificate.getName(), "name");
        validateNull(giftCertificate.getDescription(), "description");
        validateNull(giftCertificate.getPrice(), "price");
        validateNull(giftCertificate.getDuration(), "duration");
        validateDecimal(giftCertificate.getPrice(), "price");
        validateLong(giftCertificate.getDuration(), "duration");
    }

    public void validateUpdate(GiftCertificateRequest giftCertificate) {
        validateNull(giftCertificate.getId(), "id");
        validateLong(giftCertificate.getId().toString(), "id");
        validateCreate(giftCertificate);
        validateNull(giftCertificate.getCreationDate(), "creation date");
        validateDate(giftCertificate.getCreationDate(), "creation date");
    }

    public void validateUpdatePart(GiftCertificateRequest giftCertificate) {
        validateNull(giftCertificate.getId(), "id");
        validateLong(giftCertificate.getId().toString(), "id");
        if (giftCertificate.getPrice() != null)
            validateDecimal(giftCertificate.getPrice(), "price");
        if (giftCertificate.getDuration() != null)
            validateLong(giftCertificate.getDuration(), "duration");
        if (giftCertificate.getCreationDate() != null)
            validateDate(giftCertificate.getCreationDate(), "creation date");
        if (giftCertificate.getLastUpdateDate() != null)
            validateDate(giftCertificate.getLastUpdateDate(), "last update date");
    }
}
