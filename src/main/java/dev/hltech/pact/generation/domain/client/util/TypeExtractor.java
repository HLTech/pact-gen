package dev.hltech.pact.generation.domain.client.util;

import com.google.common.collect.Lists;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TypeExtractor {

    public static List<Class<?>> extractGenericTypesFromType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)type;
            return Arrays.stream(paramType.getActualTypeArguments())
                .map(a -> (Class<?>)a)
                .collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    static List<Class<?>> extractTypesFromParameter(Parameter param) {
        if (param == null) {
            return null;
        }
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
