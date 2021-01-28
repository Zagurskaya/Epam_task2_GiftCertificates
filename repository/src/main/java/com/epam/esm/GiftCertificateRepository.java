package com.epam.esm;

import com.epam.esm.model.GiftCertificate;

import java.util.List;
import java.util.Map;

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
     * Get a list of GiftCertificates by filter
     *
     * @return list of GiftCertificates
     */
    List<GiftCertificate> findAllByFilter(Map<String, String> filter);
}