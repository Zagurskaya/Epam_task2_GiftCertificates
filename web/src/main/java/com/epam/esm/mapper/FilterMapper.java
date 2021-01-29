package com.epam.esm.mapper;

import com.epam.esm.request.FilterParams;

import java.util.HashMap;
import java.util.Map;

public class FilterMapper {

    public static Map<String, String> toMap(FilterParams filterParams) {
        Map<String, String> filterMap = new HashMap<>();
        if (filterParams.getTagName() != null) filterMap.put("tagName", filterParams.getTagName());
        if (filterParams.getPartName() != null) filterMap.put("partName", filterParams.getPartName());
        if (filterParams.getPartDescription() != null)
            filterMap.put("partDescription", filterParams.getPartDescription());
        if (filterParams.getOrderBy() != null) filterMap.put("orderBy", filterParams.getOrderBy());
        if (filterParams.getOrderValue() != null) filterMap.put("orderValue", filterParams.getOrderValue());
        return filterMap;
    }
}
