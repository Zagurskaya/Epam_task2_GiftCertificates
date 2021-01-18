package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.form.Filter;
import com.epam.esm.model.GiftCertificateDTO;
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

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
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
        Long id = giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/certificate/{id}")
    public ResponseEntity updateGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) {

        if (updateGiftCertificate.getDescription() == null ||
                updateGiftCertificate.getDuration() == null ||
                updateGiftCertificate.getPrice() == null ||
                updateGiftCertificate.getCreationDate() == null ||
                updateGiftCertificate.getLastUpdateDate() == null ||
                updateGiftCertificate.getTags() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        updateGiftCertificate.setId(id);
        return giftCertificateService.update(updateGiftCertificate) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping(value = "/certificate/{id}")
    public ResponseEntity updatePartGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) {
        if (updateGiftCertificate.getName() != null) {
            GiftCertificateDTO giftCertificateByName = giftCertificateService.findByName(updateGiftCertificate.getName());
            if (giftCertificateByName != null) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }
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