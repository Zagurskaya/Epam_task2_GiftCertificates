package com.epam.esm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * GiftCertificate with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
public class GiftCertificate {
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
    private BigDecimal price;
    /**
     * Duration
     */
    private Integer duration;
    /**
     * Creation date
     */
    private LocalDateTime creationDate;
    /**
     * Last update date
     */
    private LocalDateTime lastUpdateDate;
}