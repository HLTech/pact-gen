package dev.hltech.pact.generation.domain.pact;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
final class PojoValidator {

    private PojoValidator() {}

    static void validateAll(Collection<Class<?>> classes) {
        classes.forEach(PojoValidator::validate);
    }

    private static void validate(Class<?> clazz) {
        validateComplianceWithPodam(clazz);
        validateComplianceWithJackson(clazz);
    }

    private static void validateComplianceWithPodam(Class<?> clazz) {
        List<Constructor<?>> constructors = Arrays.asList(clazz.getConstructors());
        List<Method> methods = Arrays.asList(clazz.getMethods());
        int fieldsCount = Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .collect(Collectors.toList())
            .size();

        boolean hasNoArgsConstructor = constructors.stream()
            .anyMatch(constructor -> constructor.getParameterCount() == 0);
        boolean hasAllArgsConstructor = constructors.stream()
            .anyMatch(constructor -> constructor.getParameterCount() == fieldsCount);
        boolean hasSetterForEveryField = methods.stream()
            .filter(PojoValidator::isSetter)
            .collect(Collectors.toList()).size() >= fieldsCount;

        if (!(hasNoArgsConstructor && hasSetterForEveryField) && !(hasAllArgsConstructor)) {
            log.error("Validation failed for pojo {}", clazz.getSimpleName());
            throw new PojoNonCompliantWithPodamException(clazz.getSimpleName());
        }
    }

    private static void validateComplianceWithJackson(Class<?> clazz) {
        List<Method> methods = Arrays.asList(clazz.getMethods());
        int fieldsCount = Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .collect(Collectors.toList())
            .size();

        boolean hasGetterForEveryField = methods.stream()
            .filter(method -> !method.getName().contains("getClass"))
            .filter(PojoValidator::isGetter)
            .collect(Collectors.toList()).size() >= fieldsCount;

        if (!hasGetterForEveryField) {
            log.error("Validation failed for pojo {}", clazz.getSimpleName());
            throw new MissingGettersException(clazz.getSimpleName());
        }
    }

    private static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers())
            && method.getReturnType().equals(void.class)
            && method.getParameterTypes().length == 1
            && method.getName().matches("^set[A-Z].*");
    }

    private static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
            return (method.getName().matches("^get[A-Z].*") && !method.getReturnType().equals(void.class))
                || (method.getName().matches("^is[A-Z].*") && method.getReturnType().equals(boolean.class));
        }

        return false;
    }
}
