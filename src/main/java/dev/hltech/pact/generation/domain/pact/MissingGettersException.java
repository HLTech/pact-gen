package dev.hltech.pact.generation.domain.pact;

public class MissingGettersException extends RuntimeException {

    private static final String MESSAGE_PLACEHOLDER = "Serialization won't be possible, %s class has no getters";

    public MissingGettersException(String className) {
        super(String.format(MESSAGE_PLACEHOLDER, className));
    }
}
