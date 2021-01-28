package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GiftCertificateDTO with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
public class GiftCertificateDTO {
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
    private Long duration;
    /**
     * Creation date
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    /**
     * Last update date
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdateDate;
    /**
     * List tag
     */
    private List<TagDTO> tags;
}