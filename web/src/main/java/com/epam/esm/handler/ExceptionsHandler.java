package com.epam.esm.handler;

import com.epam.esm.exception.EntityAlreadyExistException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.response.ErrorResponse;
import com.epam.esm.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> serviceError(ServiceException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> notFoundError(EntityNotFoundException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> alreadyExistError(EntityAlreadyExistException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> validatorError(IllegalArgumentException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}