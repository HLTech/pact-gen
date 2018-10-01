package dev.hltech.pact.generation.domain.pact;

import dev.hltech.pact.generation.domain.client.model.Body;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
        Set<Class<?>> pojoClasses = new HashSet<>();

        final Body requestBody = methodRepresentation.getRequestProperties().getBody();

        if (requestBody.getBodyType() != null) {
            pojoClasses.add(requestBody.getBodyType());
        }
        if (!CollectionUtils.isEmpty(requestBody.getGenericArgumentTypes())) {
            pojoClasses.addAll(requestBody.getGenericArgumentTypes());
        }

        pojoClasses.addAll(extractNestedTypes(pojoClasses));

        return pojoClasses;
    }

    private static Set<Class<?>> extractPojosFromResponseProperties(ClientMethodRepresentation methodRepresentation) {
        Set<Class<?>> pojoClasses = new HashSet<>();

        final List<ResponseProperties> responsePropertiesList = methodRepresentation.getResponsePropertiesList();

        responsePropertiesList.forEach(responseProperties -> {
            final Body responsePropertyBody = responseProperties.getBody();

            if (responsePropertyBody.getBodyType() != null) {
                pojoClasses.add(responsePropertyBody.getBodyType());
            }
            if (!CollectionUtils.isEmpty(responsePropertyBody.getGenericArgumentTypes())) {
                pojoClasses.addAll(responsePropertyBody.getGenericArgumentTypes());
            }
        });

        return pojoClasses;
    }

    private static Set<Class<?>> extractNestedTypes(Collection<Class<?>> classes) {
        return classes.stream()
            .map(PojoExtractor::extractNestedTypes)
            .flatMap(Collection::stream)
            .filter(PojoExtractor::isNotBasicJavaType)
            .collect(Collectors.toSet());
    }

    private static Set<Class<?>> extractNestedTypes(Class<?> clazz) {
        Set<Class<?>> nestedClasses = new HashSet<>();

        if (isNotBasicJavaType(clazz)) {
            nestedClasses.addAll(Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .map(Field::getType)
                .collect(Collectors.toSet()));
            nestedClasses.addAll(Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .map(Field::getType)
                .map(PojoExtractor::extractNestedTypes)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
        }

        return nestedClasses;
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
}
