package com.hltech.pact.gen.domain.client.annotation.handlers;

import com.hltech.pact.gen.domain.client.annotation.MappingMethodHandler;
import com.hltech.pact.gen.domain.client.model.Param;
import com.hltech.pact.gen.domain.client.model.RequestRepresentation;
import com.hltech.pact.gen.domain.client.util.PathParametersExtractor;
import com.hltech.pact.gen.domain.client.util.RawHeadersParser;
import com.hltech.pact.gen.domain.client.util.RequestBodyExtractor;
import com.hltech.pact.gen.domain.client.util.RequestHeaderParamsExtractor;
import com.hltech.pact.gen.domain.client.util.RequestParametersExtractor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@MappingMethodHandler
public class RequestMappingMethodsHandler implements AnnotatedMethodHandler {

    @Override
    public boolean isSupported(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    @Override
    public RequestRepresentation handleRequest(Method method) {
        return RequestRepresentation.builder()
            .httpMethod(HttpMethod.resolve(method.getAnnotation(RequestMapping.class)
                .method()[0].name().toUpperCase()))
            .path(getPathFromMethod(method))
            .headers(combineHeaders(
                ArrayUtils.addAll(method.getAnnotation(RequestMapping.class).headers(), getRequestMediaHeaders(method)),
                RequestHeaderParamsExtractor.extractAll(method)))
            .body(RequestBodyExtractor.extract(method.getParameters()))
            .requestParameters(RequestParametersExtractor.extractAll(
                method.getParameters(),
                method.getAnnotation(RequestMapping.class).path()))
            .pathParameters(PathParametersExtractor.extractAll(method))
            .build();
    }

    @Override
    public String[] getResponseMediaHeaders(Method method) {
        return Arrays.stream(method.getAnnotation(RequestMapping.class).produces())
            .map(header -> HttpHeaders.CONTENT_TYPE + "=" + header)
            .toArray(String[]::new);
    }

    private String[] getRequestMediaHeaders(Method method) {
        return ArrayUtils.addAll(
            Arrays.stream(method.getAnnotation(RequestMapping.class).consumes())
                .map(header -> HttpHeaders.CONTENT_TYPE + "=" + header)
                .toArray(String[]::new),
            Arrays.stream(method.getAnnotation(RequestMapping.class).produces())
                .map(header -> HttpHeaders.ACCEPT + "=" + header)
                .toArray(String[]::new));
    }

    private String getPathFromMethod(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return annotation.path().length == 1
            ? annotation.path()[0].split("\\?")[0]
            : annotation.value()[0].split("\\?")[0];
    }

    private static List<Param> combineHeaders(String[] rawHeaders, List<Param> headers) {
        return Stream
            .concat(RawHeadersParser.parseAll(rawHeaders).stream(), headers.stream())
            .collect(Collectors.toList());
    }
}
