package com.epam.esm.exception;

public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable e) {
        super(message, e);
    }
}
