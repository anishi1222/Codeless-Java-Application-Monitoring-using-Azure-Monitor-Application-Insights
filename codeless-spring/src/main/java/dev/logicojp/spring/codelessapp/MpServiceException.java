package dev.logicojp.spring.codelessapp;

public class MpServiceException extends RuntimeException {

    public MpServiceException(String message) {
        super(message);
    }

    public MpServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
