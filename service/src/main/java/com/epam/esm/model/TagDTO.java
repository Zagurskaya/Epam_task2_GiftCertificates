package com.epam.esm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TagDTO with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
public class TagDTO {
    /**
     * Identifier
     */
    private Long id;
    /**
     * Name
     */
    private String name;

}