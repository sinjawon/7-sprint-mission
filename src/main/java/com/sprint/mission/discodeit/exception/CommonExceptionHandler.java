package com.sprint.mission.discodeit.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> noSuchException(Exception e) {
        log.error("INTERNAL_SERVER_ERROR: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    //DiscodeitException
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> customException(DiscodeitException e) {
        log.error("커스텀 예외: code={}, message={}", e.getErrorCode(), e.getMessage(), e);
        HttpStatus status = classifyErrorStatus(e);
        ErrorResponse response = new ErrorResponse(e, status.value());
        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        log.warn("INTERNAL_SERVER_ERROR: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }


    private HttpStatus classifyErrorStatus(DiscodeitException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND, BINARY_CONTENT_NOT_FOUND,
                 READ_STATUS_NOT_FOUND, USER_STATUS_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER, DUPLICATE_READ_STATUS, DUPLICATE_USER_STATUS -> HttpStatus.CONFLICT;
            case INVALID_USER_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
            case PRIVATE_CHANNEL_UPDATE, INVALID_REQUEST -> HttpStatus.BAD_REQUEST;
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
