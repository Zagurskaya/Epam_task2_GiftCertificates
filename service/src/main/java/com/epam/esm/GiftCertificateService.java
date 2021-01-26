package com.epam.esm;

import com.epam.esm.model.GiftCertificateDTO;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    /**
     * Search GiftCertificateDTO by ID
     *
     * @param id - ID
     * @return GiftCertificateDTO
     */
    GiftCertificateDTO findById(Long id);

    /**
     * Create GiftCertificateDTO
     *
     * @param giftCertificateDTO - GiftCertificateDTO
     * @return true on successful createCheckEn
     */
    Long create(GiftCertificateDTO giftCertificateDTO);

    /**
     * Update GiftCertificateDTO
     *
     * @param giftCertificateDTO - GiftCertificateDTO
     * @return true on successful update
     */
    boolean update(GiftCertificateDTO giftCertificateDTO);

    /**
     * Delete GiftCertificateDTO
     *
     * @param id - GiftCertificateDTO id
     * @return true on successful delete
     */
    boolean delete(Long id);

    /**
     * Get giftCertificateDTO List
     *
     * @return giftCertificateDTO List
     */
    List<GiftCertificateDTO> findAll();

    /**
     * Update part GiftCertificateDTO
     *
     * @param giftCertificateDTO - GiftCertificateDTO
     * @return true on successful update
     */
    boolean updatePart(GiftCertificateDTO giftCertificateDTO);

    /**
     * Get giftCertificateDTO List by filter
     *
     * @return giftCertificateDTO List
     */
    List<GiftCertificateDTO> findAllByFilter(Map<String, String> filter);
}