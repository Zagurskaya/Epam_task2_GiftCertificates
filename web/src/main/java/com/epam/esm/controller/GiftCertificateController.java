package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.mapper.FilterMapper;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.request.FilterRequest;
import com.epam.esm.model.GiftCertificateDTO;
import com.epam.esm.request.GiftCertificateRequest;
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
    private final GiftCertificateMapper giftCertificateMapper;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateValidator giftCertificateValidator, GiftCertificateMapper giftCertificateMapper) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
        this.giftCertificateMapper = giftCertificateMapper;
    }

    @GetMapping(value = "/certificates")
    public ResponseEntity<List<GiftCertificateDTO>> getGiftCertificates(FilterRequest filterRequest) {
        List<GiftCertificateDTO> certificateDTOS = null;
        Map<String, String> filterMap = FilterMapper.toMap(filterRequest);
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
    public ResponseEntity<IdResponse> createGiftCertificate(@RequestBody GiftCertificateRequest giftCertificate) {
        giftCertificateValidator.validateCreate(giftCertificate);
        Long id = giftCertificateService.create(giftCertificateMapper.toCreateDTO(giftCertificate));
        IdResponse idResponse = new IdResponse(id);
        return new ResponseEntity<>(idResponse, HttpStatus.CREATED);
    }

    @PutMapping(value = "/certificates")
    public ResponseEntity updateGiftCertificate(@RequestBody GiftCertificateRequest updateGiftCertificate) {

        giftCertificateValidator.validateUpdate(updateGiftCertificate);
        giftCertificateService.update(giftCertificateMapper.toUpdateDTO(updateGiftCertificate));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/certificates")
    public ResponseEntity updatePartGiftCertificate(@RequestBody GiftCertificateRequest updateGiftCertificate) {
        giftCertificateValidator.validateUpdatePart(updateGiftCertificate);
        giftCertificateService.updatePart(giftCertificateMapper.toUpdatePathDTO(updateGiftCertificate));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/certificates/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}