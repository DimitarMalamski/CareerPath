package com.careerpath.interfaces.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String KEY_STATUS = "status";
    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        KEY_STATUS, HttpStatus.NOT_FOUND.value(),
                        KEY_ERROR, HttpStatus.NOT_FOUND.getReasonPhrase(),
                        KEY_MESSAGE, ex.getMessage()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        KEY_STATUS, HttpStatus.BAD_REQUEST.value(),
                        KEY_ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        KEY_MESSAGE, ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleServerError(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        KEY_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        KEY_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        KEY_MESSAGE, "Something went wrong on our side."
                ));
    }
}
