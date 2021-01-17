package com.epam.esm;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateRepository {
    /**
     * Get a list of GiftCertificates
     *
     * @return list of GiftCertificates
     */
    List<GiftCertificate> findAll();

    /**
     * GiftCertificate search by ID
     *
     * @param id - ID
     * @return object
     */
    GiftCertificate findById(Long id);

    /**
     * Create GiftCertificate
     *
     * @param giftCertificate - GiftCertificate
     * @return true on successful creation
     */
    Long create(GiftCertificate giftCertificate);

    /**
     * Update part GiftCertificate
     *
     * @param giftCertificate - GiftCertificate
     * @return true on successful change
     */
    boolean updatePart(GiftCertificate giftCertificate);

    /**
     * Update GiftCertificate
     *
     * @param giftCertificate - GiftCertificate
     * @return true on successful change
     */
    boolean update(GiftCertificate giftCertificate);

    /**
     * Delete giftCertificate
     *
     * @param id - giftCertificate id
     * @return true on successful delete
     */
    boolean delete(Long id);


    /**
     * GiftCertificate search by name
     *
     * @param name - giftCertificate name
     * @return giftCertificate
     */
    GiftCertificate findByName(String name);

    /**
     * GiftCertificates search by Tag name
     *
     * @param tagName - Tag name
     * @return giftCertificate
     */
    List<GiftCertificate> findAllByTagName(String tagName);
}