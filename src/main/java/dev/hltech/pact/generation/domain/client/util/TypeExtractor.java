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
        if (type == null) {
            return null;
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)type;
            return Arrays.stream(paramType.getActualTypeArguments())
                .map(a -> (Class<?>)a)
                .collect(Collectors.toList());
        }

        if (type.getClass().isArray()) {
            return Lists.newArrayList((Class<?>)type);
        }

        if (type.getClass().getDeclaredFields().length > 0) {
            return Arrays.stream(((Class<?>)type).getDeclaredFields())
                .filter(field -> field.getGenericType() instanceof ParameterizedType)
                .flatMap(fld -> (Arrays.stream(((ParameterizedType) fld.getGenericType()).getActualTypeArguments())))
                .map(a -> (Class<?>)a)
                .collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

    static Class<?> extractTypesFromParameter(Parameter param) {
        if (param == null) {
            return null;
        }
        if (param.getParameterizedType() instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)param.getParameterizedType();
            return Arrays.stream(paramType.getActualTypeArguments())
                .map(type -> (Class<?>)type)
                .collect(Collectors.toList())
                .get(0);
        }

        if (param.getType().isArray()) {
            return param.getType().getComponentType();
        }

        return param.getType();
    }
}
