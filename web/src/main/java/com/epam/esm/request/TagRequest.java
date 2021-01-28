package com.epam.esm.request;

import lombok.*;

/**
 * TagDTO with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TagRequest {
    /**
     * Identifier
     */
    private Long id;
    /**
     * Name
     */
    private String name;

}