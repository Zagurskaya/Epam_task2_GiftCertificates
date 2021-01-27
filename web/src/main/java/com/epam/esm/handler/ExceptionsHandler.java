package com.epam.esm.handler;

import com.epam.esm.exception.*;
import com.epam.esm.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> notFoundError(EntityNotFoundException exc) {
        ErrorResponse error = new ErrorResponse(StatusCode.NOT_FOUND.getLabel(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> alreadyExistError(EntityAlreadyExistException exc) {
        ErrorResponse error = new ErrorResponse(StatusCode.CONFLICT.getLabel(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> alreadyExistError(EmptyFieldException exc) {
        ErrorResponse error = new ErrorResponse(StatusCode.CONFLICT.getLabel(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> validatorError(ValidationException exc) {
        ErrorResponse error = new ErrorResponse(StatusCode.BAD_REQUEST.getLabel(), exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
        ErrorResponse error = new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR.getLabel(), "что-то пошло не так");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}