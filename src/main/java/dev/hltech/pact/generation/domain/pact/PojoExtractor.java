package dev.hltech.pact.generation.domain.pact;

import dev.hltech.pact.generation.domain.client.model.Body;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
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
            extractTypesFromBody(methodRepresentation.getRequestProperties().getBody()));
    }

    private static Set<Class<?>> extractPojosFromResponseProperties(ClientMethodRepresentation methodRepresentation) {
        Set<Class<?>> pojoClasses = new HashSet<>();

        methodRepresentation.getResponsePropertiesList().forEach(responseProperties ->
            pojoClasses.addAll(extractTypesFromBody(responseProperties.getBody())));

        return pojoClasses;
    }

    private static Set<Class<?>> extractTypesFromBody(Body body) {
        Set<Class<?>> typesFromBody = new HashSet<>();

        if (body.getBodyType() != null) {
            typesFromBody.add(body.getBodyType());
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
            .filter(PojoExtractor::isNotBasicJavaType)
            .filter(PojoExtractor::isNotEnum)
            .collect(Collectors.toSet());
    }

    private static Set<Class<?>> extractNestedTypes(Class<?> clazz) {
        Set<Class<?>> nestedClasses = new HashSet<>();

        if (isNotBasicJavaType(clazz) && isNotEnum(clazz)) {
            nestedClasses.add(clazz);
            Set<Class<?>> typesOfFields = getTypesOfFields(clazz);
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
}
