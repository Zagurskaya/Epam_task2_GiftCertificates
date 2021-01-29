package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.mapper.FilterMapper;
import com.epam.esm.request.FilterParams;
import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.response.IdResponse;
import com.epam.esm.validator.GiftCertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<GiftCertificateDTO>> getGiftCertificates(FilterParams filterParams) {
        List<GiftCertificateDTO> certificateDTOS = null;
        Map<String, String> filterMap = FilterMapper.toMap(filterParams);
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
    public ResponseEntity<IdResponse> createGiftCertificate(@RequestBody GiftCertificateDTO giftCertificate) {
        giftCertificateValidator.validate(giftCertificate);
        Long id = giftCertificateService.create(giftCertificate);
        IdResponse idResponse = new IdResponse(id);
        return new ResponseEntity<>(idResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/certificates")
    public ResponseEntity updateGiftCertificate(@RequestBody GiftCertificateDTO updateGiftCertificate) {

        giftCertificateValidator.validate(updateGiftCertificate);
        giftCertificateService.update(updateGiftCertificate);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/certificates")
    public ResponseEntity updatePartGiftCertificate(@RequestBody GiftCertificateDTO updateGiftCertificate) {
        giftCertificateValidator.validateUpdatePath(updateGiftCertificate);
        giftCertificateService.updatePart(updateGiftCertificate);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/certificates/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}