package com.epam.esm.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * GiftCertificate with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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