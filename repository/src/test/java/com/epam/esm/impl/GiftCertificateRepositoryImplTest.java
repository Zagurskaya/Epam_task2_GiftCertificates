package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    private static Long createId;

    @Test
    @Order(1)
    void createTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("giftCertificate1");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        this.createId = giftCertificateRepository.create(giftCertificate);
        assertNotEquals(java.util.Optional.ofNullable(createId), 0L);
    }

    @Test
    @Order(2)
    void findAllTest() {
        List<GiftCertificate> certificates = giftCertificateRepository.findAll();
        assertNotNull(certificates.size());
    }

    @Test
    @Order(3)
    void findByIdTest() {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(createId);
        assertNotNull(giftCertificate);
    }

    @Test
    @Order(4)
    void findByNameTest() {
        GiftCertificate giftCertificate = giftCertificateRepository.findByName("giftCertificate1");
        assertNotNull(giftCertificate);
    }

    @Test
    @Order(5)
    void updateTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(createId);
        giftCertificate.setName("giftCertificate2");
        giftCertificate.setDescription("Description2");
        giftCertificate.setPrice(new BigDecimal(40));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        boolean result = giftCertificateRepository.update(giftCertificate);
        assertTrue(result);
    }

    @Test
    @Order(6)
    void updatePartTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(createId);
        giftCertificate.setName("giftCertificate22");
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        boolean result = giftCertificateRepository.updatePart(giftCertificate);
        assertTrue(result);
    }

    @Test
    @Order(7)
    void deleteTest() {
        boolean result = giftCertificateRepository.delete(createId);
        assertTrue(result);
    }
}