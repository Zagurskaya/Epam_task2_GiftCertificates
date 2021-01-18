package com.epam.esm;


import com.epam.esm.converter.GiftCertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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

    private GiftCertificateService giftCertificateService;

    @Before
    public void init() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, giftCertificateConverter, tagRepository, tagConverter);
    }

    @Test
    public void shouldGetGiftCertificateDTOListForFindAllMethodCall() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("certificate");
        giftCertificate.setDescription("description");
        giftCertificate.setDuration(1);
        giftCertificate.setPrice(new BigDecimal(10));
        giftCertificate.setCreationDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("certificate");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1);
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
        giftCertificate.setDuration(1);
        giftCertificate.setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("certificate");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1);
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
        giftCertificate.setDuration(1);
        giftCertificate.setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("certificate");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setTags(new ArrayList<>());
        when(giftCertificateRepository.create(any())).thenThrow(new RuntimeException());
        giftCertificateService.create(giftCertificateDTO);
    }

    @Test
    public void shouldGetGiftCertificateDTOForFindByIdMethodCall() {
        Optional<GiftCertificate> giftCertificate = Optional.of(new GiftCertificate());
        giftCertificate.get().setId(1L);
        giftCertificate.get().setName("GiftCertificate1");
        giftCertificate.get().setDescription("description");
        giftCertificate.get().setDuration(1);
        giftCertificate.get().setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1L);
        giftCertificateDTO.setName("GiftCertificate1");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setTags(new ArrayList<>());
        when(giftCertificateRepository.findById(giftCertificate.get().getId())).thenReturn(giftCertificate);
        when(giftCertificateConverter.toDTO(giftCertificate.get())).thenReturn(giftCertificateDTO);
        GiftCertificateDTO giftCertificateById = giftCertificateService.findById(giftCertificateDTO.getId());
        assertEquals(giftCertificateDTO, giftCertificateById);
    }

    @Test(expected = DaoException.class)
    public void shouldThrowDaoExceptionWhenCatchingExceptionFromRepositoryForFindByIdMethodCall() {
        Long id = 1L;
        when(giftCertificateRepository.findById(id)).thenThrow(new DaoException("GiftCertificate not found with id"));
        giftCertificateService.findById(id);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowServiceExceptionForDeleteMethodCall() {
        Long id = 1L;
        Optional<GiftCertificate> giftCertificate = Optional.of(new GiftCertificate());
        giftCertificate.get().setId(id);
        giftCertificate.get().setName("GiftCertificate1");
        giftCertificate.get().setDescription("description");
        giftCertificate.get().setDuration(1);
        giftCertificate.get().setPrice(new BigDecimal(10));
        when(giftCertificateRepository.findById(id)).thenThrow(new ServiceException("GiftCertificate not found with id " + id));
        giftCertificateService.delete(id);
    }

    @Test
    public void shouldTrueForDeleteMethodCall() {
        Optional<GiftCertificate> giftCertificate = Optional.of(new GiftCertificate());
        giftCertificate.get().setId(1L);
        giftCertificate.get().setName("GiftCertificate1");
        giftCertificate.get().setDescription("description");
        giftCertificate.get().setDuration(1);
        giftCertificate.get().setPrice(new BigDecimal(10));
        when(giftCertificateRepository.findById(giftCertificate.get().getId())).thenReturn(giftCertificate);
        when(giftCertificateRepository.delete(giftCertificate.get().getId())).thenReturn(true);
        assertEquals(true, giftCertificateService.delete(giftCertificate.get().getId()));
    }

    @Test
    public void shouldGetGiftCertificateDTOForFindByNameMethodCall() {
        Optional<GiftCertificate> giftCertificate = Optional.of(new GiftCertificate());
        giftCertificate.get().setId(1L);
        giftCertificate.get().setName("GiftCertificate1");
        giftCertificate.get().setDescription("description");
        giftCertificate.get().setDuration(1);
        giftCertificate.get().setPrice(new BigDecimal(10));
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setId(1L);
        giftCertificateDTO.setName("GiftCertificate1");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setDuration(1);
        giftCertificateDTO.setPrice(new BigDecimal(10));
        giftCertificateDTO.setTags(new ArrayList<>());
        when(giftCertificateRepository.findByName(giftCertificate.get().getName())).thenReturn(giftCertificate);
        when(giftCertificateConverter.toDTO(giftCertificate.get())).thenReturn(giftCertificateDTO);
        GiftCertificateDTO giftCertificateById = giftCertificateService.findByName(giftCertificateDTO.getName());
        assertEquals(giftCertificateDTO, giftCertificateById);
    }

    @Test(expected = DaoException.class)
    public void shouldThrowDaoExceptionWhenCatchingExceptionFromRepositoryForFindByNameMethodCall() {
        String name = "GiftCertificate1";
        when(giftCertificateRepository.findByName(name)).thenThrow(new DaoException("GiftCertificate not found with id"));
        giftCertificateService.findByName(name);
    }
}