package com.hltech.pact.gen.domain.client.util;

import com.google.common.collect.Lists;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TypeExtractor {

    private TypeExtractor() {}

    public static List<Class<?>> extractParameterTypesFromType(Type type) {
        if (type == null) {
            return new ArrayList<>();
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType)type;
            return Arrays.stream(paramType.getActualTypeArguments())
                .map(a -> (Class<?>)a)
                .collect(Collectors.toList());
        }

        if (((Class<?>)type).isArray()) {
            return Lists.newArrayList(((Class<?>)type).getComponentType());
        }

        return Lists.newArrayList();
    }
}
