package com.hltech.pact.gen.domain.client.annotation.handlers;

import com.hltech.pact.gen.domain.client.annotation.MappingMethodHandler;
import com.hltech.pact.gen.domain.client.model.Body;
import com.hltech.pact.gen.domain.client.model.Param;
import com.hltech.pact.gen.domain.client.model.RequestRepresentation;
import com.hltech.pact.gen.domain.client.util.TypeExtractor;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@MappingMethodHandler
public class JaxRsGetMappingMethodsHandler implements AnnotatedMethodHandler {

    //TODO Turn on scanning for JAX-RS related annotations when feature is complete
    @Override
    public boolean isSupported(Method method) {
        return false;
    }

    @Override
    public RequestRepresentation handleRequest(Method method) {
        return RequestRepresentation.builder()
                .httpMethod(HttpMethod.GET)
                .path(extractPath(method))
                .headers(extractHeaders(method))
                .body(extractBody(method))
                .requestParameters(extractRequestParameters(method))
                .pathParameters(extractPathParameters(method))
                .build();
    }

    //TODO Implement resolving media headers
    @Override
    public String[] getResponseMediaHeaders(Method method) {
        return new String[0];
    }

    private List<Param> extractPathParameters(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(param -> param.getAnnotation(PathParam.class) != null)
                .map(this::fromPathParam)
                .collect(Collectors.toList());
    }

    private Param fromPathParam(Parameter parameter) {
        return Param.builder()
                .name(parameter.getAnnotation(PathParam.class).value())
                .type(parameter.getType())
                .build();
    }

    private List<Param> extractRequestParameters(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(param -> param.getAnnotation(QueryParam.class) != null)
                .map(this::fromQueryParam)
                .collect(Collectors.toList());
    }

    private Param fromQueryParam(Parameter parameter) {
        return Param.builder()
                .name(parameter.getAnnotation(QueryParam.class).value())
                .type(parameter.getType())
                .build();
    }

    private Body extractBody(Method method) {
        Optional<Parameter> requestBody = Arrays.stream(method.getParameters())
                .filter(p -> p.getAnnotations().length == 0 || p.getAnnotation(Consumes.class) != null)
                .findFirst();

        Body.BodyBuilder builder = Body.builder();
        requestBody
                .map(Parameter::getType)
                .ifPresent(builder::type);
        requestBody
                .map(Parameter::getParameterizedType)
                .map(TypeExtractor::extractParameterTypesFromType)
                .ifPresent(builder::genericArgumentTypes);
        return builder.build();
    }

    //TODO this method supports only headers added via annotation @HeaderParam.class
    private List<Param> extractHeaders(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(param -> param.getAnnotation(HeaderParam.class) != null)
                .map(param -> param.getAnnotation(HeaderParam.class))
                .map(this::fromHeaderParam)
                .collect(Collectors.toList());
    }

    private Param fromHeaderParam(HeaderParam headerParam) {
        return Param.builder()
                .name(headerParam.value())
                .build();
    }

    private String extractPath(Method method) {
        Path annotation = method.getAnnotation(Path.class) != null
                ? method.getAnnotation(Path.class)
                : method.getDeclaringClass().getAnnotation(Path.class);
        Assert.notNull(annotation, () -> String.format("Cannot find annotation %s on %s or %s",
                Path.class.getName(), ElementType.TYPE, ElementType.METHOD));
        return annotation.value();
    }
}
