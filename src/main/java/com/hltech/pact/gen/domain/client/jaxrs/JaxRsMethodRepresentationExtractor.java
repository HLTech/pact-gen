package com.hltech.pact.gen.domain.client.jaxrs;

import com.hltech.pact.gen.domain.client.ClientMethodRepresentationExtractor;
import com.hltech.pact.gen.domain.client.annotation.handlers.AnnotatedMethodHandler;
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation;
import com.hltech.pact.gen.domain.client.model.RequestRepresentation;

import java.lang.reflect.Method;
import java.util.Collection;

public class JaxRsMethodRepresentationExtractor implements ClientMethodRepresentationExtractor {

    private final Collection<AnnotatedMethodHandler> annotatedMethodHandlers;

    public JaxRsMethodRepresentationExtractor(Collection<AnnotatedMethodHandler> annotatedMethodHandlers) {
        this.annotatedMethodHandlers = annotatedMethodHandlers;
    }

    //TODO add response extraction
    @Override
    public ClientMethodRepresentation extractClientMethodRepresentation(Method clientMethod) {
        return ClientMethodRepresentation.builder()
                .requestRepresentation(extractRequestProperties(clientMethod))
                .responseRepresentationList(null)
                .build();
    }

    private RequestRepresentation extractRequestProperties(Method clientMethod) {
        return this.annotatedMethodHandlers.stream()
                .filter(annotationHandler -> annotationHandler.isSupported(clientMethod))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
                .handleRequest(clientMethod);
    }
}
