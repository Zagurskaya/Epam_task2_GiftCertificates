package com.epam.esm.mapper;

import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.model.TagDTO;
import com.epam.esm.request.GiftCertificateRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GiftCertificateMapper {

    public GiftCertificateDTO toCreateDTO(GiftCertificateRequest giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName(giftCertificate.getName());
        giftCertificateDTO.setDescription(giftCertificate.getDescription());
        giftCertificateDTO.setPrice(new BigDecimal(giftCertificate.getPrice()));
        giftCertificateDTO.setDuration(Long.parseLong(giftCertificate.getDuration()));
        List<TagDTO> tagDTOList = new ArrayList<>();
        tagDTOList.addAll(giftCertificate.getTags());
        giftCertificateDTO.setTags(tagDTOList);
        return giftCertificateDTO;
    }

    public GiftCertificateDTO toUpdateDTO(GiftCertificateRequest giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = toCreateDTO(giftCertificate);
        giftCertificateDTO.setId(giftCertificate.getId());
        giftCertificateDTO.setCreationDate(getFormattedStringToLocalDateTime(giftCertificate.getCreationDate()));
        List<TagDTO> tagDTOList = new ArrayList<>();
        tagDTOList.addAll(giftCertificate.getTags());
        giftCertificateDTO.setTags(tagDTOList);
        return giftCertificateDTO;
    }

    public GiftCertificateDTO toUpdatePathDTO(GiftCertificateRequest giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(giftCertificate.getId());
        if (giftCertificate.getName() != null)
            giftCertificateDTO.setName(giftCertificate.getName());
        if (giftCertificate.getDescription() != null)
            giftCertificateDTO.setDescription(giftCertificate.getDescription());
        if (giftCertificate.getPrice() != null)
            giftCertificateDTO.setPrice(new BigDecimal(giftCertificate.getPrice()));
        if (giftCertificate.getDuration() != null)
            giftCertificateDTO.setDuration(Long.parseLong(giftCertificate.getDuration()));
        if (giftCertificate.getCreationDate() != null)
            giftCertificateDTO.setCreationDate(getFormattedStringToLocalDateTime(giftCertificate.getCreationDate()));
        List<TagDTO> tagDTOList = new ArrayList<>();
        tagDTOList.addAll(giftCertificate.getTags());
        giftCertificateDTO.setTags(tagDTOList);
        return giftCertificateDTO;
    }

    private LocalDateTime getFormattedStringToLocalDateTime(String localDateTime) {
        return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
