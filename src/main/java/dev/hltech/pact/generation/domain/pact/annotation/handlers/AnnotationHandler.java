package dev.hltech.pact.generation.domain.pact.annotation.handlers;

import dev.hltech.pact.generation.domain.client.model.RequestProperties;

import java.lang.reflect.Method;

public interface AnnotationHandler {

    boolean isSupported(Method method);

    RequestProperties handle(Method method);
}
