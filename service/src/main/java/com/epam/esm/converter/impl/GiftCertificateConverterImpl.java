package com.epam.esm.converter.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GiftCertificateConverterImpl implements GiftCertificateConverter {
    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public GiftCertificateConverterImpl(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
    }


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

    @Override
    public GiftCertificate toEntityPartFields(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateDTO.getId());
        giftCertificate.setId(giftCertificateDTO.getId());
        giftCertificate.setName(giftCertificateDTO.getName() != null ? giftCertificateDTO.getName() : giftCertificate.getName());
        giftCertificate.setDescription(giftCertificateDTO.getDescription() != null ? giftCertificateDTO.getDescription() : giftCertificate.getDescription());
        giftCertificate.setPrice(giftCertificateDTO.getPrice() != null ? giftCertificateDTO.getPrice() : giftCertificate.getPrice());
        giftCertificate.setDuration(giftCertificateDTO.getDuration() != null ? giftCertificateDTO.getDuration() : giftCertificate.getDuration());
        giftCertificate.setCreationDate(giftCertificateDTO.getCreationDate() != null ? giftCertificateDTO.getCreationDate() : giftCertificate.getCreationDate());
        giftCertificate.setLastUpdateDate(giftCertificateDTO.getLastUpdateDate() != null ? giftCertificateDTO.getLastUpdateDate() : giftCertificate.getLastUpdateDate());
        return giftCertificate;
    }


}
