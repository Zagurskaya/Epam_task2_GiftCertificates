package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.request.FilterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = "/certificates")
    public List<GiftCertificateDTO> getGiftCertificates() throws ServiceException {
        return giftCertificateService.findAll();
    }

    @GetMapping(value = "/certificate/{id}")
    public GiftCertificateDTO getGiftCertificateById(@PathVariable("id") long id) throws ServiceException {
        return giftCertificateService.findById(id);
    }

    @PostMapping(value = "/certificate")
    public ResponseEntity<Long> createGiftCertificate(@RequestBody GiftCertificateDTO giftCertificate) throws ServiceException {
        GiftCertificateDTO giftCertificateByName = giftCertificateService.findByName(giftCertificate.getName());
        if (giftCertificateByName != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Long id = giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/certificate/{id}")
    public ResponseEntity updateGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) throws ServiceException {
        GiftCertificateDTO giftCertificateDTO = giftCertificateService.findById(id);
        if (giftCertificateDTO == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        updateGiftCertificate.setId(id);
        giftCertificateService.update(updateGiftCertificate);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/certificate/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable("id") long id) throws ServiceException {
        GiftCertificateDTO giftCertificate = giftCertificateService.findById(id);
        if (giftCertificate == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        giftCertificateService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "/certificate/filter")
    public ResponseEntity<List<GiftCertificateDTO>> findAllGiftCertificatesByTagName(@RequestBody FilterForm form) throws ServiceException {
        if (form.getTagName() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        List<GiftCertificateDTO> listGiftCertificateDTOByTagName = giftCertificateService.findAllGiftCertificateListByTagName(form.getTagName());
        return new ResponseEntity<>(listGiftCertificateDTOByTagName, HttpStatus.OK);
    }
}