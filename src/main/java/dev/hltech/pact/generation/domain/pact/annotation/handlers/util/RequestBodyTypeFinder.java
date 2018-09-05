package dev.hltech.pact.generation.domain.pact.annotation.handlers.util;

import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public final class RequestBodyTypeFinder {

    private RequestBodyTypeFinder() {}

    public static Class<?> find(Parameter[]parameters) {
        return Arrays.stream(parameters)
            .filter(RequestBodyTypeFinder::isRequestBody)
            .findFirst()
            .map(Parameter::getType)
            .orElse(null);
    }

    private static boolean isRequestBody(Parameter parameter) {
        return parameter.getAnnotations().length == 0
            || parameter.isAnnotationPresent(RequestBody.class);
    }
}
