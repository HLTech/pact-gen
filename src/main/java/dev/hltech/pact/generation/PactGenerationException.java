package dev.hltech.pact.generation;

public class PactGenerationException extends RuntimeException {

    public PactGenerationException(String message) {
        super(message);
    }

    public PactGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
