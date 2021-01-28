package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.GiftCertificateTagRepository;
import com.epam.esm.TagRepository;
import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RepositoryTestConfig.class)
class GiftCertificateTagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private GiftCertificateTagRepository giftCertificateTagRepository;

    @Test
    void createRelationBetweenTagAndGiftCertificateTest() {
        Tag tag = new Tag();
        tag.setName("tagCreateRelation");
        Long tagId = tagRepository.create(tag);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("giftCertificateCreateRelation");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        Long giftCertificateId = giftCertificateRepository.create(giftCertificate);
        Long id = giftCertificateTagRepository.createRelationBetweenTagAndGiftCertificate(tagId, giftCertificateId);
        assertNotEquals(java.util.Optional.ofNullable(id), 0L);
    }

    @Test
    void deleteRelationBetweenTagAndGiftCertificateTest() {
        Tag tag = new Tag();
        tag.setName("tagDeleteRelation");
        Long tagId = tagRepository.create(tag);

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("giftCertificateDeleteRelation");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        Long giftCertificateId = giftCertificateRepository.create(giftCertificate);
        Long id = giftCertificateTagRepository.createRelationBetweenTagAndGiftCertificate(tagId, giftCertificateId);
        boolean result = giftCertificateTagRepository.deleteRelationBetweenTagAndGiftCertificate(tagId, giftCertificateId);
        assertTrue(result);
    }

    @Test
    void findListCertificateIdByTagIdTest() {
        List<Long> ids = giftCertificateTagRepository.findListCertificateIdByTagId(1L);
        assertNotNull(ids.size());
    }
}