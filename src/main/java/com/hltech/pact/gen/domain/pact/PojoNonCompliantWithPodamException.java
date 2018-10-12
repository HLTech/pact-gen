package com.hltech.pact.gen.domain.pact;

public class PojoNonCompliantWithPodamException extends RuntimeException {

    private static final String MESSAGE_PLACEHOLDER =
        "Unable to instantiate %s object with podam due to missing noArgsConstructor + setters / allArgsConstructor";

    public PojoNonCompliantWithPodamException(String className) {
        super(String.format(MESSAGE_PLACEHOLDER, className));
    }
}
