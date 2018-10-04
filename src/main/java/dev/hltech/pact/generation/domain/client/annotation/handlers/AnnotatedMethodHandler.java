package dev.hltech.pact.generation.domain.client.annotation.handlers;

import dev.hltech.pact.generation.domain.client.model.RequestRepresentation;

import java.lang.reflect.Method;

public interface AnnotatedMethodHandler {

    boolean isSupported(Method method);

    RequestRepresentation handle(Method method);
}
