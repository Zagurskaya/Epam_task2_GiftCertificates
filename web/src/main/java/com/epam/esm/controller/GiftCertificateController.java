package com.epam.esm.controller;

import com.epam.esm.GiftCertificateService;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.GiftCertificateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GiftCertificateController {

    private GiftCertificateService giftCertificateService;

    @Autowired
    public void setGiftCertificateService(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = "/giftCertificates")
    public List<GiftCertificateDTO> getGiftCertificates() throws ServiceException {
        return giftCertificateService.findAll();
    }

    @GetMapping(value = "/giftCertificate/{id}")
    public GiftCertificateDTO getGiftCertificateById(@PathVariable("id") long id) throws ServiceException {
        return giftCertificateService.findById(id);
    }

    @PostMapping(value = "/giftCertificate")
    public ResponseEntity<Long> createGiftCertificate(@RequestBody GiftCertificateDTO giftCertificate) throws ServiceException {
        GiftCertificateDTO giftCertificateByName = giftCertificateService.findByName(giftCertificate.getName());
        if (giftCertificateByName != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Long id = giftCertificateService.create(giftCertificate);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/giftCertificate/{id}")
    public ResponseEntity updateGiftCertificate(@PathVariable("id") long id, @RequestBody GiftCertificateDTO updateGiftCertificate) throws ServiceException {
        GiftCertificateDTO giftCertificateDTO = giftCertificateService.findById(id);
        if (giftCertificateDTO == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        updateGiftCertificate.setId(id);
        giftCertificateService.update(updateGiftCertificate);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/giftCertificate/{id}")
    public ResponseEntity deleteGiftCertificate(@PathVariable("id") long id) throws ServiceException {
        GiftCertificateDTO giftCertificate = giftCertificateService.findById(id);
        if (giftCertificate == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        giftCertificateService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}