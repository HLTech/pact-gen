package com.hltech.pact.gen;

public class PactGenerationException extends RuntimeException {

    public PactGenerationException(String message) {
        super(message);
    }

    public PactGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
