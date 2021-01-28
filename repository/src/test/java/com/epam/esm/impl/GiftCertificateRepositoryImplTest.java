package com.epam.esm.impl;

import com.epam.esm.GiftCertificateRepository;
import com.epam.esm.config.RepositoryTestConfig;
import com.epam.esm.exception.EntityAlreadyExistException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.Assertions;
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
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Test
    void findAllTest() {
        List<GiftCertificate> certificates = giftCertificateRepository.findAll();
        assertNotNull(certificates.size());
    }

    @Test
    void createTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("giftCertificate22");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        Long id = giftCertificateRepository.create(giftCertificate);
        GiftCertificate createdGiftCertificate = giftCertificateRepository.findById(id);
        assertEquals(giftCertificate.getName(), createdGiftCertificate.getName());
        assertEquals(giftCertificate.getDescription(), createdGiftCertificate.getDescription());
        assertEquals(giftCertificate.getDuration(), createdGiftCertificate.getDuration());
        assertTrue(giftCertificate.getPrice().compareTo(createdGiftCertificate.getPrice()) == 0);
    }

    @Test
    void createThrowsExceptionTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("giftCertificateThrowsExceptionTest");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        giftCertificateRepository.create(giftCertificate);
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> {
            giftCertificateRepository.create(giftCertificate);
        });
    }

    @Test
    void findByIdTest() {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(1L);
        assertNotNull(giftCertificate);
    }

    @Test
    void updateTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("giftCertificate2");
        giftCertificate.setDescription("Description2");
        giftCertificate.setPrice(new BigDecimal(40.0));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        boolean result = giftCertificateRepository.update(giftCertificate);
        GiftCertificate updateGiftCertificate = giftCertificateRepository.findById(1L);
        assertTrue(result);
        assertEquals(giftCertificate.getName(), updateGiftCertificate.getName());
        assertEquals(giftCertificate.getDescription(), updateGiftCertificate.getDescription());
        assertEquals(giftCertificate.getDuration(), updateGiftCertificate.getDuration());
        assertTrue(giftCertificate.getPrice().compareTo(updateGiftCertificate.getPrice()) == 0);
    }

    @Test
    void updatePartTest() {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(1L);
        giftCertificate.setName("giftCertificateUpdatePar");
        giftCertificate.setDescription("Description22");
        boolean result = giftCertificateRepository.updatePart(giftCertificate);
        GiftCertificate updateGiftCertificate = giftCertificateRepository.findById(1L);
        assertTrue(result);
        assertEquals(giftCertificate.getName(), updateGiftCertificate.getName());
        assertEquals(giftCertificate.getDescription(), updateGiftCertificate.getDescription());
    }

    @Test
    void deleteTest() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("giftCertificateName");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setDuration(20L);
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        Long id = giftCertificateRepository.create(giftCertificate);
        giftCertificateRepository.delete(id);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            giftCertificateRepository.findById(id);
        });
    }
}