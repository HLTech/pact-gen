package com.hltech.pact.gen.domain;

import com.google.common.collect.Sets;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

public class RestInspector {

    private static Set<Class<? extends Annotation>> supportedHttpMethods = newHashSet(GET.class, PUT.class);

    public static ClientRepresentation inspectRestClient(Class<?> restClient) {

        String basePath = restClient.isAnnotationPresent(Path.class) ? restClient.getAnnotation(Path.class).value() : "";


        return new ClientRepresentation(Arrays.stream(restClient.getMethods())
            .filter(method -> !Sets.intersection(
                Arrays.stream(method.getDeclaredAnnotations())
                    .map(Annotation::annotationType)
                    .collect(Collectors.toSet()),
                supportedHttpMethods
            ).isEmpty())
            .map(method -> buildInteraction(method, basePath))
            .collect(Collectors.toList()));
    }

    private static Interaction buildInteraction(Method method, String basePath) {
        Map<String, Class<?>> queryParameters = Arrays.stream(method.getParameters())
            .filter(parameter -> parameter.isAnnotationPresent(QueryParam.class))
            .collect(Collectors.toMap(
                parameter -> parameter.getAnnotation(QueryParam.class).value(),
                Parameter::getType
            ));

        Map<String, Class<?>> pathParameters = Arrays.stream(method.getParameters())
            .filter(parameter -> parameter.isAnnotationPresent(PathParam.class))
            .collect(Collectors.toMap(
                parameter -> parameter.getAnnotation(PathParam.class).value(),
                Parameter::getType
            ));

        Class<?> body = Arrays.stream(method.getParameters())
            .filter(param -> param.getAnnotations().length == 0)
            .findAny()
            .map(Parameter::getType)
            .orElse(null);

        return new Interaction(
            Sets.intersection(
                Arrays.stream(method.getDeclaredAnnotations())
                    .map(Annotation::annotationType)
                    .collect(Collectors.toSet()),
                supportedHttpMethods
            ).iterator().next().getSimpleName(),
            basePath + (method.isAnnotationPresent(Path.class) ? method.getAnnotation(Path.class).value() : ""),
            queryParameters,
            pathParameters,
            body);

    }

}
