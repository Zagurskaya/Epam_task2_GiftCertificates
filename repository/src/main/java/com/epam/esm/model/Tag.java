package com.epam.esm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Tag with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
public class Tag {
    /**
     * Identifier
     */
    private Long id;
    /**
     * Name
     */
    private String name;
}