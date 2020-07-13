package com.hltech.pact.gen.domain.client.util;

import com.hltech.pact.gen.domain.client.model.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RequestParametersExtractor {

    private RequestParametersExtractor() {}

    public static List<Param> extractAll(Parameter[] parameters, String[] path) {
        return Stream.concat(extractParamsFromMethod(parameters), extractParamsFromAnnotation(path))
            .collect(Collectors.toList());
    }

    private static Stream<Param> extractParamsFromAnnotation(String[] path) {

        if (path.length < 1) {
            return Stream.empty();
        }

        String[] splitPath = path[0].split("\\?");

        if (splitPath.length < 2) {
            return Stream.empty();
        }

        return Arrays.stream(splitPath[1].split("&"))
            .map(paramString -> Param.builder()
                .name(paramString.split("=")[0])
                .defaultValue(paramString.split("=")[1])
                .build());
    }

    private static Stream<Param> extractParamsFromMethod(Parameter[] parameters) {
        return Arrays.stream(parameters)
            .filter(param -> param.getAnnotation(RequestParam.class) != null)
            .filter(param -> param.getType() != Map.class)
            .map(RequestParametersExtractor::extractParam);
    }

    private static Param extractParam(Parameter param) {
        Param.ParamBuilder builder = Param.builder();

        extractParamDefaultValue(param).ifPresent(builder::defaultValue);

        List<Class<?>> paramTypes = TypeExtractor.extractParameterTypesFromType(param.getParameterizedType());

        return builder
            .name(extractParamName(param))
            .type(param.getType())
            .genericArgumentType(paramTypes.isEmpty() ? null : paramTypes.get(0))
            .build();
    }

    private static Optional<Object> extractParamDefaultValue(Parameter param) {
        RequestParam annotation = param.getAnnotation(RequestParam.class);

        if (annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE) || annotation.defaultValue().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(annotation.defaultValue());
    }

    private static String extractParamName(Parameter param) {
        RequestParam annotation = param.getAnnotation(RequestParam.class);

        if (!annotation.name().isEmpty()) {
            return annotation.name();
        } else if (!annotation.value().isEmpty()) {
            return annotation.value();
        }

        return param.getName();
    }
}
