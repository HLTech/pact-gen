package dev.hltech.pact.generation.domain.client.util;

import dev.hltech.pact.generation.domain.client.model.Body;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public final class RequestBodyExtractor {

    private RequestBodyExtractor() {}

    public static Body extract(Parameter[]parameters) {
        Parameter responseParameter = Arrays.stream(parameters)
            .filter(RequestBodyExtractor::isRequestBody)
            .findFirst()
            .orElse(null);

        return Body.builder()
            .bodyType(responseParameter != null ? responseParameter.getType() : null)
            .genericArgumentTypes(TypeExtractor.extractTypesFromParameter(responseParameter))
            .build();
    }

    private static boolean isRequestBody(Parameter parameter) {
        return parameter.getAnnotations().length == 0
            || parameter.isAnnotationPresent(RequestBody.class);
    }
}
