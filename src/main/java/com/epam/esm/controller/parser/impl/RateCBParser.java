package com.epam.esm.controller.parser.impl;

import com.epam.esm.controller.parser.BaseDataParser;
import com.epam.esm.entity.RateCB;
import com.epam.esm.util.DataUtil;

import java.math.BigDecimal;
import java.util.List;

public class RateCBParser implements BaseDataParser<RateCB> {
    private final static String ROW_SEPARATOR = "\r\n";
    private final static String ELEMENT_SEPARATOR = ";";

    @Override
    public RateCB parse(String text) {
        List<String> elements = parsTextToRowList(text, ELEMENT_SEPARATOR);

        return new RateCB.Builder()
                .addСoming(Long.parseLong(elements.get(0)))
                .addSpending(Long.parseLong(elements.get(1)))
                .addLocalDateTime(DataUtil.getFormattedStringToLocalDateTime(elements.get(2)))
                .addSum(new BigDecimal(elements.get(3)))
                .addIsBack("1".equals(elements.get(4)))
                .build();
    }

    public List<String> parsTextToRowList(String text) {
        return BaseDataParser.super.parsTextToRowList(text, ROW_SEPARATOR);
    }
}
