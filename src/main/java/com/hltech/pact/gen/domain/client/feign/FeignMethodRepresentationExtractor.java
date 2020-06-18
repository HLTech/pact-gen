package com.hltech.pact.gen.domain.client.feign;

import com.google.common.collect.Lists;
import com.hltech.pact.gen.domain.client.ClientMethodRepresentationExtractor;
import com.hltech.pact.gen.domain.client.annotation.handlers.AnnotatedMethodHandler;
import com.hltech.pact.gen.domain.client.model.Body;
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation;
import com.hltech.pact.gen.domain.client.model.Param;
import com.hltech.pact.gen.domain.client.model.RequestRepresentation;
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation;
import com.hltech.pact.gen.domain.client.util.RawHeadersParser;
import com.hltech.pact.gen.domain.client.util.TypeExtractor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FeignMethodRepresentationExtractor implements ClientMethodRepresentationExtractor {

    private final Collection<AnnotatedMethodHandler> annotatedMethodHandlers;

    public FeignMethodRepresentationExtractor(Collection<AnnotatedMethodHandler> annotatedMethodHandlers) {
        this.annotatedMethodHandlers = annotatedMethodHandlers;
    }

    @Override
    public ClientMethodRepresentation extractClientMethodRepresentation(Method method) {
        return ClientMethodRepresentation.builder()
            .requestRepresentation(extractRequestProperties(method))
            .responseRepresentationList(extractResponseProperties(method))
            .build();
    }

    private RequestRepresentation extractRequestProperties(Method method) {
        return this.annotatedMethodHandlers.stream()
            .filter(annotationHandler -> annotationHandler.isSupported(method))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
            .handleRequest(method);
    }

    private List<ResponseRepresentation> extractResponseProperties(Method method) {
        String[] responseHeaders = this.annotatedMethodHandlers.stream()
            .filter(annotationHandler -> annotationHandler.isSupported(method))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
            .getResponseMediaHeaders(method);

        List<ResponseRepresentation> results = Arrays
            .stream(method.getDeclaredAnnotationsByType(InteractionInfo.class))
            .map(annotation -> populateResponse(
                annotation.responseStatus(),
                RawHeadersParser.parseAll(ArrayUtils.addAll(annotation.responseHeaders(), responseHeaders)),
                method,
                annotation.description(),
                annotation.emptyBodyExpected()))
            .collect(Collectors.toList());

        return !results.isEmpty()
            ? results
            : Lists.newArrayList(
                populateResponse(HttpStatus.OK, RawHeadersParser.parseAll(responseHeaders), method, "", false));
    }

    private static ResponseRepresentation populateResponse(HttpStatus status,
                                                           List<Param> headers,
                                                           Method feignClientMethod,
                                                           String description,
                                                           boolean isEmptyBodyExpected) {

        return ResponseRepresentation.builder()
            .status(status)
            .headers(headers)
            .body(Body.builder()
                .type(feignClientMethod.getReturnType())
                .genericArgumentTypes(
                    TypeExtractor.extractParameterTypesFromType(feignClientMethod.getGenericReturnType()))
                .build())
            .description(description)
            .emptyBodyExpected(isEmptyBodyExpected)
            .build();
    }
}
