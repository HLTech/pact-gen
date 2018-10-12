package com.hltech.pact.gen.domain.client.util;

import com.hltech.pact.gen.domain.client.model.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PathParametersExtractor {

    private PathParametersExtractor() {}

    public static List<Param> extractAll(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(PathVariable.class) != null)
            .filter(param -> param.getType() != Map.class)
            .map(PathParametersExtractor::extract)
            .collect(Collectors.toList());
    }

    private static Param extract(Parameter param) {
        PathVariable annotation = param.getAnnotation(PathVariable.class);

        List<Class<?>> paramTypes = TypeExtractor.extractParameterTypesFromType(param.getParameterizedType());

        return Param.builder()
            .name(annotation.name().isEmpty() ? annotation.value() : annotation.name())
            .type(param.getType())
            .genericArgumentType(paramTypes.isEmpty() ? null : paramTypes.get(0))
            .build();
    }
}
