package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.request.FilterForm;
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
    public ResponseEntity<List<GiftCertificateDTO>> getGiftCertificates(FilterForm filterForm) throws ServiceException {
        List<GiftCertificateDTO> certificateDTOS = null;
        Map<String, String> filterMap = new HashMap<>();
        if (filterForm.getTagName() != null) filterMap.put("tagName", filterForm.getTagName());
        if (filterForm.getPartName() != null) filterMap.put("partName", filterForm.getPartName());
        if (filterForm.getPartDescription() != null) filterMap.put("partDescription", filterForm.getPartDescription());
        if (filterForm.getOrderBy() != null) filterMap.put("orderBy", filterForm.getOrderBy());
        if (filterForm.getOrderValue() != null) filterMap.put("orderValue", filterForm.getOrderValue());
        if (filterMap.isEmpty()) {
            certificateDTOS = giftCertificateService.findAll();
        } else {
            certificateDTOS = giftCertificateService.findAllByFilter(filterMap);
        }
        return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/certificates/{id}")
    public ResponseEntity<GiftCertificateDTO> getGiftCertificateById(@PathVariable("id") long id) {
        GiftCertificateDTO giftCertificateDTO = giftCertificateService.findById(id);
        return new ResponseEntity<>(giftCertificateDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/certificates")
    public ResponseEntity<String> createGiftCertificate(@RequestBody GiftCertificateDTO giftCertificate) {
        giftCertificateValidator.createValidate(giftCertificate);
        Long id = giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>("id=" + id, HttpStatus.OK);
    }

    @PutMapping(value = "/certificates/{id}")
    public ResponseEntity updateGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) {

        giftCertificateValidator.updateValidate(updateGiftCertificate);
        updateGiftCertificate.setId(id);
        return giftCertificateService.update(updateGiftCertificate) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(value = "/certificates/{id}")
    public ResponseEntity updatePartGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) {
        giftCertificateValidator.updatePartValidate(updateGiftCertificate);
        updateGiftCertificate.setId(id);
        return giftCertificateService.updatePart(updateGiftCertificate) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/certificates/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable("id") long id) {
        GiftCertificateDTO giftCertificate = giftCertificateService.findById(id);
        if (giftCertificate == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return giftCertificateService.delete(id) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}