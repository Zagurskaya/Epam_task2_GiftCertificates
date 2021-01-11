package com.epam.esm;

import com.epam.esm.exception.DaoException;
import com.epam.esm.model.GiftCertificate;

import java.sql.Connection;
import java.util.List;

public interface GiftCertificateRepository {
    /**
     * Get a list of GiftCertificates
     *
     * @param connection - Connection to BD
     * @return list of GiftCertificates
     * @throws DaoException database access error or other errors.
     */
    List<GiftCertificate> findAll(Connection connection) throws DaoException;

    /**
     * GiftCertificate search by ID
     *
     * @param connection - Connection to BD
     * @param id         - ID
     * @return object
     * @throws DaoException database access error or other errors.
     */
    GiftCertificate findById(Connection connection, Long id) throws DaoException;

    /**
     * Create GiftCertificate
     *
     * @param connection      - Connection to BD
     * @param giftCertificate - GiftCertificate
     * @return true on successful creation
     * @throws DaoException database access error or other errors
     */
    Long create(Connection connection, GiftCertificate giftCertificate) throws DaoException;

    /**
     * Update GiftCertificate
     *
     * @param connection      - Connection to BD
     * @param giftCertificate - GiftCertificate
     * @return true on successful change
     * @throws DaoException database access error or other errors
     */
    boolean update(Connection connection, GiftCertificate giftCertificate) throws DaoException;

    /**
     * Delete giftCertificate
     *
     * @param connection - Connection to BD
     * @param id         - giftCertificate id
     * @return true on successful delete
     * @throws DaoException database access error or other errors.
     */
    boolean delete(Connection connection, Long id) throws DaoException;


    /**
     * GiftCertificate search by name
     *
     * @param connection - Connection to BD
     * @param name       - giftCertificate name
     * @return giftCertificate
     * @throws DaoException database access error or other errors.
     */
    GiftCertificate findByName(Connection connection, String name) throws DaoException;
    /**
     * GiftCertificates search by Tag name
     *
     * @param connection - Connection to BD
     * @param tagName       - Tag name
     * @return giftCertificate
     * @throws DaoException database access error or other errors.
     */
    List<GiftCertificate> findAllByTagName(Connection connection, String tagName);
}