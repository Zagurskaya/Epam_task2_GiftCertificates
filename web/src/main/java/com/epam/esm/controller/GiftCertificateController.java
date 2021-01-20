package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.form.Filter;
import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateValidator giftCertificateValidator;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateValidator giftCertificateValidator) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @GetMapping(value = "/certificates")
    public List<GiftCertificateDTO> getGiftCertificates(Filter filter) throws ServiceException {
        List<GiftCertificateDTO> certificateDTOS = null;
        Map<String, String> filterMap = new HashMap<>();
        if (filter.getTagName() != null) filterMap.put("tagName", filter.getTagName());
        if (filter.getPartName() != null) filterMap.put("partName", filter.getPartName());
        if (filter.getPartDescription() != null) filterMap.put("partDescription", filter.getPartDescription());
        if (filterMap.isEmpty()) {
            certificateDTOS = giftCertificateService.findAll();
        } else {
            certificateDTOS = giftCertificateService.findAllByFilter(filterMap);
        }
        return certificateDTOS;
    }

    @GetMapping(value = "/certificate/{id}")
    public GiftCertificateDTO getGiftCertificateById(@PathVariable("id") long id) {
        return giftCertificateService.findById(id);
    }

    @PostMapping(value = "/certificate")
    public ResponseEntity<Long> createGiftCertificate(@RequestBody GiftCertificateDTO giftCertificate) {
        giftCertificateValidator.createValidate(giftCertificate);
        Long id = giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/certificate/{id}")
    public ResponseEntity updateGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) {

        giftCertificateValidator.updateValidate(updateGiftCertificate);
        updateGiftCertificate.setId(id);
        return giftCertificateService.update(updateGiftCertificate) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(value = "/certificate/{id}")
    public ResponseEntity updatePartGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) {
        giftCertificateValidator.updatePartValidate(updateGiftCertificate);
        updateGiftCertificate.setId(id);
        return giftCertificateService.updatePart(updateGiftCertificate) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/certificate/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable("id") long id) {
        GiftCertificateDTO giftCertificate = giftCertificateService.findById(id);
        if (giftCertificate == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return giftCertificateService.delete(id) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}