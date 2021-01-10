package com.epam.esm;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.model.GiftCertificateDTO;

import java.util.List;

public interface GiftCertificateService {
    /**
     * Search GiftCertificateDTO by ID
     *
     * @param id - ID
     * @return GiftCertificateDTO
     * @throws ServiceException error during execution of logical blocks and actions
     */
    GiftCertificateDTO findById(Long id) throws ServiceException;

    /**
     * Create GiftCertificateDTO
     *
     * @param giftCertificateDTO - GiftCertificateDTO
     * @return true on successful createCheckEn
     * @throws ServiceException error during execution of logical blocks and actions
     */
    Long create(GiftCertificateDTO giftCertificateDTO) throws ServiceException;

    /**
     * Update GiftCertificateDTO
     *
     * @param giftCertificateDTO - GiftCertificateDTO
     * @return true on successful update
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean update(GiftCertificateDTO giftCertificateDTO) throws ServiceException;

    /**
     * Delete GiftCertificateDTO
     *
     * @param id - GiftCertificateDTO id
     * @return true on successful delete
     * @throws ServiceException error during execution of logical blocks and actions
     */
    boolean delete(Long id) throws ServiceException;

    /**
     * Get giftCertificateDTO List
     *
     * @return giftCertificateDTO List
     */
    List<GiftCertificateDTO> findAll() throws ServiceException;

    /**
     * Search GiftCertificateDTO by name
     *
     * @param name - GiftCertificate name
     * @return GiftCertificateDTO
     * @throws ServiceException error during execution of logical blocks and actions
     */
    GiftCertificateDTO findByName(String name) throws ServiceException;
}