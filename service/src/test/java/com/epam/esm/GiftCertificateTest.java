package com.epam.esm;

import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.impl.GiftCertificateServiceImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificateDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GiftCertificateTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private GiftCertificateConverter giftCertificateConverter;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagConverter tagConverter;
    @Mock
    private GiftCertificateTagRepository relationRepository;

    private GiftCertificateService giftCertificateService;

    @Before
    public void init() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, giftCertificateConverter, tagRepository, tagConverter, relationRepository);
    }

    @Test
    public void shouldGetGiftCertificateDTOListForFindAllMethodCall() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("certificate");
        giftCertificate.setDescription("description");
        giftCertificate.setDuration(1L);
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("certificate");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1L);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setCreationDate(LocalDateTime.now());
        giftCertificateDTO.setLastUpdateDate(LocalDateTime.now());
        List<GiftCertificate> giftCertificates = Collections.singletonList(giftCertificate);
        when(giftCertificateRepository.findAll()).thenReturn(giftCertificates);
        when(giftCertificateConverter.toDTO(giftCertificate)).thenReturn(giftCertificateDTO);
        List<GiftCertificateDTO> returnedGiftCertificates = giftCertificateService.findAll();
        assertEquals(giftCertificateDTO, returnedGiftCertificates.get(0));
    }

    @Test
    public void shouldExecuteMethodCreateGiftCertificateWithoutExceptions() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("certificate");
        giftCertificate.setDescription("description");
        giftCertificate.setDuration(1L);
        giftCertificate.setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("certificate");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1L);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setTags(new ArrayList<>());
        when(giftCertificateRepository.create(giftCertificate)).thenReturn(1L);
        when(giftCertificateConverter.toEntity(giftCertificateDTO)).thenReturn(giftCertificate);
        giftCertificateService.create(giftCertificateDTO);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenCatchingExceptionFromGiftCertificateRepositoryForCreateMethodCall() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("certificate");
        giftCertificate.setDescription("description");
        giftCertificate.setDuration(1L);
        giftCertificate.setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("certificate");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1L);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setTags(new ArrayList<>());
        when(giftCertificateRepository.create(any())).thenThrow(new RuntimeException());
        giftCertificateService.create(giftCertificateDTO);
    }

    @Test
    public void shouldGetGiftCertificateDTOForFindByIdMethodCall() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("GiftCertificate1");
        giftCertificate.setDescription("description");
        giftCertificate.setDuration(1L);
        giftCertificate.setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1L);
        giftCertificateDTO.setName("GiftCertificate1");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1L);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setTags(new ArrayList<>());
        when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(giftCertificate);
        when(giftCertificateConverter.toDTO(giftCertificate)).thenReturn(giftCertificateDTO);
        GiftCertificateDTO giftCertificateById = giftCertificateService.findById(giftCertificateDTO.getId());
        assertEquals(giftCertificateDTO, giftCertificateById);
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenCatchingExceptionFromRepositoryForFindByIdMethodCall() {
        Long id = 1L;
        when(giftCertificateRepository.findById(id)).thenThrow(new EntityNotFoundException("GiftCertificate not found with id"));
        giftCertificateService.findById(id);
    }

    @Test
    public void shouldTrueForDeleteMethodCall() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("GiftCertificate1");
        giftCertificate.setDescription("description");
        giftCertificate.setDuration(1L);
        giftCertificate.setPrice(new BigDecimal(10));
        when(giftCertificateRepository.findById(giftCertificate.getId())).thenReturn(giftCertificate);
        when(giftCertificateRepository.delete(giftCertificate.getId())).thenReturn(true);
        assertTrue(giftCertificateService.delete(giftCertificate.getId()));
    }
}