package com.epam.esm.exception;

/**
 * Dao constraint violation exception
 */
public class DaoConstraintViolationException extends Exception {

    /**
     * Constructor
     *
     * @param message - message
     * @param cause   - cause
     */
    public DaoConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

}
