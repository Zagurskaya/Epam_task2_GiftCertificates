package com.epam.esm.model;

import lombok.*;

/**
 * Tag with characteristics <b>id</b>, <b>name</b>.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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