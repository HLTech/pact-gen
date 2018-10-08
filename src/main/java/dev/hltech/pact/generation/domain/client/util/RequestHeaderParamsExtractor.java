package dev.hltech.pact.generation.domain.client.util;

import dev.hltech.pact.generation.domain.client.model.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RequestHeaderParamsExtractor {

    private RequestHeaderParamsExtractor() {}

    public static List<Param> extractAll(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(RequestHeader.class) != null)
            .filter(param -> param.getType() != Map.class
                && param.getType() != MultiValueMap.class
                && param.getType() != HttpHeaders.class)
            .map(RequestHeaderParamsExtractor::extract)
            .collect(Collectors.toList());
    }

    private static Param extract(Parameter param) {
        Param.ParamBuilder builder = Param.builder();

        extractHeaderDefaultValue(param).ifPresent(builder::defaultValue);

        return builder
            .name(extractHeaderName(param))
            .type(param.getType())
            .genericArgumentTypes(TypeExtractor.extractParameterTypesFromType(param.getParameterizedType()))
            .build();
    }

    private static Optional<Object> extractHeaderDefaultValue(Parameter param) {
        RequestHeader annotation = param.getAnnotation(RequestHeader.class);

        if (annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE) || annotation.defaultValue().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(annotation.defaultValue());
    }

    private static String extractHeaderName(Parameter param) {
        RequestHeader annotation = param.getAnnotation(RequestHeader.class);

        if (!annotation.name().isEmpty()) {
            return annotation.name();
        } else if (!annotation.value().isEmpty()) {
            return annotation.value();
        }

        return param.getName();
    }
}
