package com.hltech.pact.gen.domain.pact;

import com.hltech.pact.gen.domain.client.model.Body;
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

final class PojoExtractor {

    private PojoExtractor() {}

    static Set<Class<?>> extractPojoTypes(ClientMethodRepresentation methodRepresentation) {
        Set<Class<?>> pojoClasses = new HashSet<>();

        pojoClasses.addAll(extractPojosFromRequestProperties(methodRepresentation));
        pojoClasses.addAll(extractPojosFromResponseProperties(methodRepresentation));

        return pojoClasses.stream()
            .filter(clazz -> clazz != void.class)
            .collect(Collectors.toSet());
    }

    private static Set<Class<?>> extractPojosFromRequestProperties(ClientMethodRepresentation methodRepresentation) {
        return new HashSet<>(
            extractTypesFromBody(methodRepresentation.getRequestRepresentation().getBody()));
    }

    private static Set<Class<?>> extractPojosFromResponseProperties(ClientMethodRepresentation methodRepresentation) {
        Set<Class<?>> pojoClasses = new HashSet<>();

        methodRepresentation.getResponseRepresentationList().forEach(responseProperties ->
            pojoClasses.addAll(extractTypesFromBody(responseProperties.getBody())));

        return pojoClasses;
    }

    private static Set<Class<?>> extractTypesFromBody(Body body) {
        Set<Class<?>> typesFromBody = new HashSet<>();

        if (body.getType() != null) {
            typesFromBody.add(body.getType());
        }
        if (!CollectionUtils.isEmpty(body.getGenericArgumentTypes())) {
            typesFromBody.addAll(body.getGenericArgumentTypes());
        }

        return extractNestedTypes(typesFromBody);
    }

    private static Set<Class<?>> extractNestedTypes(Collection<Class<?>> classes) {
        return classes.stream()
            .map(PojoExtractor::extractNestedTypes)
            .flatMap(Collection::stream)
            .map(clazz -> isArrayOfNonprimitives(clazz) ? clazz.getComponentType() : clazz)
            .filter(clazz -> !isArrayOfPrimitives(clazz))
            .filter(PojoExtractor::isNotBasicJavaType)
            .filter(PojoExtractor::isNotEnum)
            .collect(Collectors.toSet());
    }

    private static Set<Class<?>> extractNestedTypes(Class<?> clazz) {
        Class<?> baseClass = clazz;
        Set<Class<?>> nestedClasses = new HashSet<>();

        if (isArrayOfPrimitives(baseClass)) {
            return nestedClasses;
        }

        if (isArrayOfNonprimitives(baseClass)) {
            baseClass = clazz.getComponentType();
        }

        if (isNotBasicJavaType(baseClass) && isNotEnum(baseClass)) {
            nestedClasses.add(baseClass);
            Set<Class<?>> typesOfFields = getTypesOfFields(baseClass);
            nestedClasses.addAll(typesOfFields);
            nestedClasses.addAll(collectNestedTypes(typesOfFields));
        }

        return nestedClasses;
    }

    private static Set<Class<?>> getTypesOfFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .map(Field::getType)
            .collect(Collectors.toSet());
    }

    private static Set<Class<?>> collectNestedTypes(Collection<Class<?>> classes) {
        return classes.stream()
            .map(PojoExtractor::extractNestedTypes)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    private static boolean isNotBasicJavaType(Class<?> clazz) {
        return !isPrimitive(clazz) && !isFromJdk(clazz);
    }

    private static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    private static boolean isFromJdk(Class<?> clazz) {
        return clazz.getPackage().getName().startsWith("java");
    }

    private static boolean isNotEnum(Class<?> clazz) {
        return !clazz.isEnum();
    }

    private static boolean isArrayOfPrimitives(Class<?> clazz) {
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    private static boolean isArrayOfNonprimitives(Class<?> clazz) {
        return clazz.isArray() && !clazz.getComponentType().isPrimitive();
    }
}
