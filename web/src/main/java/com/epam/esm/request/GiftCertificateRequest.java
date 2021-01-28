package com.epam.esm.request;

import com.epam.esm.model.TagDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * GiftCertificateDTO with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
public class GiftCertificateRequest {
    /**
     * Identifier
     */
    private Long id;
    /**
     * Name
     */
    private String name;
    /**
     * Description
     */
    private String description;
    /**
     * Price
     */
    private String price;
    /**
     * Duration
     */
    private String duration;

    private String creationDate;
    /**
     * Last update date
     */
    private String lastUpdateDate;

    private List<TagDTO> tags;
}