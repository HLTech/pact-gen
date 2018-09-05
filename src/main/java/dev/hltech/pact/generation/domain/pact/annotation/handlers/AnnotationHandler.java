package dev.hltech.pact.generation.domain.pact.annotation.handlers;

import dev.hltech.pact.generation.domain.client.model.RequestProperties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationHandler {

    boolean isSupported(Annotation annotation);

    RequestProperties handle(Method method);
}
