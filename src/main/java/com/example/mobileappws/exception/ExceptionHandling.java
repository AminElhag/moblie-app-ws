package com.example.mobileappws.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandling {
    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserExecution(UserServiceException exception, WebRequest webRequest) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleExecution(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
