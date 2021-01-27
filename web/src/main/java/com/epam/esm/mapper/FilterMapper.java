package com.epam.esm.mapper;

import com.epam.esm.request.FilterRequest;

import java.util.HashMap;
import java.util.Map;

public class FilterMapper {

    public static Map<String, String> toMap(FilterRequest filterRequest) {
        Map<String, String> filterMap = new HashMap<>();
        if (filterRequest.getTagName() != null) filterMap.put("tagName", filterRequest.getTagName());
        if (filterRequest.getPartName() != null) filterMap.put("partName", filterRequest.getPartName());
        if (filterRequest.getPartDescription() != null)
            filterMap.put("partDescription", filterRequest.getPartDescription());
        if (filterRequest.getOrderBy() != null) filterMap.put("orderBy", filterRequest.getOrderBy());
        if (filterRequest.getOrderValue() != null) filterMap.put("orderValue", filterRequest.getOrderValue());
        return filterMap;
    }
}
