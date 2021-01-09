package com.epam.esm.converter;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDTO;

public interface GiftCertificateConverter {

    GiftCertificateDTO toDTO(GiftCertificate giftCertificate);

    GiftCertificate toEntity(GiftCertificateDTO giftCertificateDTO);

}