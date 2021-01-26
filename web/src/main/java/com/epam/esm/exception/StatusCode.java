package com.epam.esm.exception;

public enum StatusCode {
    BAD_REQUEST(40400),
    NOT_FOUND(40404),
    CONFLICT(40409),
    INTERNAL_SERVER_ERROR(50500);

    private Integer label;

    StatusCode(Integer label) {
        this.label = label;
    }

    public Integer getLabel() {
        return label;
    }
}