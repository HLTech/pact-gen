package dev.hltech.pact.generation.domain.client.util;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

class ParameterTypeExtractor {

    static Class<?> extractParameterType(Parameter param) {
        if (param.getParameterizedType() instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)param.getParameterizedType();
            return (Class<?>)paramType.getActualTypeArguments()[0];
        }

        if (param.getType().isArray()) {
            return param.getType().getComponentType();
        }

        return param.getType();
    }
}
