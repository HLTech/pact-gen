package dev.hltech.pact.generation.domain.client.util;

import com.google.common.collect.Lists;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ParameterTypeExtractor {

    static List<Class<?>> extractParameterTypes(Parameter param) {
        if (param.getParameterizedType() instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)param.getParameterizedType();
            return Arrays.stream(paramType.getActualTypeArguments())
                .map(type -> (Class<?>)type)
                .collect(Collectors.toList());
        }

        if (param.getType().isArray()) {
            return Lists.newArrayList(param.getType().getComponentType());
        }

        return Lists.newArrayList(param.getType());
    }
}
