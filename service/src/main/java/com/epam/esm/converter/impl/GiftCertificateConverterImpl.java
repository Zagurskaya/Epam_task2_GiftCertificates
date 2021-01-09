package com.epam.esm.converter.impl;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDTO;
import org.springframework.stereotype.Component;


@Component
public class GiftCertificateConverterImpl implements GiftCertificateConverter {

    @Override
    public GiftCertificateDTO toDTO(GiftCertificate giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(giftCertificate.getId());
        giftCertificateDTO.setName(giftCertificate.getName());
        giftCertificateDTO.setDescription(giftCertificate.getDescription());
        giftCertificateDTO.setPrice(giftCertificate.getPrice());
        giftCertificateDTO.setDuration(giftCertificate.getDuration());
        giftCertificateDTO.setCreationDate(giftCertificate.getCreationDate());
        giftCertificateDTO.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        return giftCertificateDTO;
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(giftCertificateDTO.getId());
        giftCertificate.setName(giftCertificateDTO.getName());
        giftCertificate.setDescription(giftCertificateDTO.getDescription());
        giftCertificate.setPrice(giftCertificateDTO.getPrice());
        giftCertificate.setDuration(giftCertificateDTO.getDuration());
        giftCertificate.setCreationDate(giftCertificateDTO.getCreationDate());
        giftCertificate.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate());
        return giftCertificate;
    }


}
