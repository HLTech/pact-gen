package dev.hltech.pact.generation.domain.pact.annotation.handlers.util;

import dev.hltech.pact.generation.domain.client.model.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PathParametersExtractor {

    private PathParametersExtractor() {}

    public static List<Param> extractPathParameters(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(PathVariable.class) != null)
            .filter(param -> param.getType() != Map.class)
            .map(PathParametersExtractor::extractPathParameter)
            .collect(Collectors.toList());
    }

    private static Param extractPathParameter(Parameter param) {
        PathVariable annotation = param.getAnnotation(PathVariable.class);

        return Param.builder()
            .name(annotation.name().isEmpty() ? annotation.value() : annotation.name())
            .type(param.getType())
            .build();
    }
}
