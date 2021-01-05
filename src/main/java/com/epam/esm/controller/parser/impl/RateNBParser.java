package com.epam.esm.controller.parser.impl;

import com.epam.esm.controller.parser.BaseDataParser;
import com.epam.esm.entity.RateNB;
import com.epam.esm.util.DataUtil;

import java.math.BigDecimal;
import java.util.List;

public class RateNBParser implements BaseDataParser<RateNB> {
    private final static String ROW_SEPARATOR = "\r\n";
    private final static String ELEMENT_SEPARATOR = ";";

    @Override
    public RateNB parse(String text) {
        List<String> elements = parsTextToRowList(text, ELEMENT_SEPARATOR);
        return new RateNB.Builder()
                .addCurrencyId(Long.parseLong(elements.get(1)))
                .addDate(DataUtil.getFormattedStringToLocalDate(elements.get(0)))
                .addSum(new BigDecimal(elements.get(2)))
                .build();
    }

    public List<String> parsTextToRowList(String text) {
        return BaseDataParser.super.parsTextToRowList(text, ROW_SEPARATOR);
    }
}
