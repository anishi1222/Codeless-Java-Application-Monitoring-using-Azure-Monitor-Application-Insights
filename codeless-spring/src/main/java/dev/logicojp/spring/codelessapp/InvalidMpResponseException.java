package dev.logicojp.spring.codelessapp;

public class InvalidMpResponseException extends RuntimeException {

    public InvalidMpResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMpResponseException(String message) {
        super(message);
    }
}
