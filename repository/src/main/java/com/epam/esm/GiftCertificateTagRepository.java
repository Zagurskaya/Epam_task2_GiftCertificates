package com.epam.esm;

import java.util.List;

public interface GiftCertificateTagRepository {

    /**
     * Create Tag and connection with GiftCertificate
     *
     * @param tagId         - Tag id
     * @param CertificateId - GiftCertificate Id
     * @return relation id
     */
    Long createRelationBetweenTagAndGiftCertificate(Long tagId, Long CertificateId);

    /**
     * Delete Tag connection with GiftCertificate
     *
     * @param tagId         - Tag id
     * @param CertificateId - GiftCertificate Id
     * @return true on successful creation
     */
    boolean deleteRelationBetweenTagAndGiftCertificate(Long tagId, Long CertificateId);

    /**
     * Get a list of GiftCertificateId by Tag id
     *
     * @return list of Tags
     */
    List<Long> findListCertificateIdByTagId(Long tagId);
}