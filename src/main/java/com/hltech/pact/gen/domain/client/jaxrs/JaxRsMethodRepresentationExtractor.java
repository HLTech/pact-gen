package com.hltech.pact.gen.domain.client.jaxrs;

import com.google.common.collect.Lists;
import com.hltech.pact.gen.domain.client.ClientMethodRepresentationExtractor;
import com.hltech.pact.gen.domain.client.annotation.handlers.AnnotatedMethodHandler;
import com.hltech.pact.gen.domain.client.feign.InteractionInfo;
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation;
import com.hltech.pact.gen.domain.client.model.RequestRepresentation;
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation;
import com.hltech.pact.gen.domain.client.util.RawHeadersParser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JaxRsMethodRepresentationExtractor implements ClientMethodRepresentationExtractor {

    private final Collection<AnnotatedMethodHandler> annotatedMethodHandlers;

    public JaxRsMethodRepresentationExtractor(Collection<AnnotatedMethodHandler> annotatedMethodHandlers) {
        this.annotatedMethodHandlers = annotatedMethodHandlers;
    }

    @Override
    public ClientMethodRepresentation extractClientMethodRepresentation(Method clientMethod) {
        return ClientMethodRepresentation.builder()
                .requestRepresentation(extractRequestProperties(clientMethod))
                .responseRepresentationList(extractResponseProperties(clientMethod))
                .build();
    }

    private RequestRepresentation extractRequestProperties(Method clientMethod) {
        return this.annotatedMethodHandlers.stream()
                .filter(annotationHandler -> annotationHandler.isSupported(clientMethod))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
                .handleRequest(clientMethod);
    }

    private List<ResponseRepresentation> extractResponseProperties(Method method) {
        String[] responseHeaders = this.annotatedMethodHandlers.stream()
            .filter(annotationHandler -> annotationHandler.isSupported(method))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
            .getResponseMediaHeaders(method);

        List<ResponseRepresentation> results = Arrays
            .stream(method.getDeclaredAnnotationsByType(InteractionInfo.class))
            .map(annotation -> ResponseRepresentation.from(
                annotation.responseStatus(),
                RawHeadersParser.parseAll(annotation.responseHeaders()),
                method,
                annotation.description(),
                annotation.emptyBodyExpected()))
            .collect(Collectors.toList());

        return !results.isEmpty()
            ? results
            : Lists.newArrayList(ResponseRepresentation.getDefaultForMethod(method, responseHeaders));
    }
}
