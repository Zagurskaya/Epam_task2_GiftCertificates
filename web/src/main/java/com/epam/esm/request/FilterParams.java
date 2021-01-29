package com.epam.esm.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilterParams {
    private String tagName;
    private String partName;
    private String partDescription;
    private String orderBy;
    private String orderValue;
}
