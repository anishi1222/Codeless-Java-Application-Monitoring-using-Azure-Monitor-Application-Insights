package dev.logicojp.spring.codelessapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> invalidJson(HttpMessageNotReadableException ex) {
        LOGGER.warn("Invalid JSON", ex);
        return ResponseEntity.badRequest().body(new ErrorMessage("Invalid JSON"));
    }

    @ExceptionHandler(InvalidMpResponseException.class)
    public ResponseEntity<ErrorMessage> invalidMpJson(InvalidMpResponseException ex) {
        LOGGER.warn("Invalid JSON response from MP service", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Invalid JSON"));
    }

    @ExceptionHandler(MpServiceException.class)
    public ResponseEntity<ErrorMessage> mpServiceFailure(MpServiceException ex) {
        LOGGER.warn("MP service request failed", ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorMessage("MP service request failed"));
    }
}
