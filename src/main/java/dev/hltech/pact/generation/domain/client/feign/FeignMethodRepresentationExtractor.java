package dev.hltech.pact.generation.domain.client.feign;

import dev.hltech.pact.generation.domain.client.annotation.handlers.AnnotatedMethodHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.DeleteMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.GetMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.PatchMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.PostMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.PutMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.RequestMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.ClientMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import dev.hltech.pact.generation.domain.client.util.RawHeadersParser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FeignMethodRepresentationExtractor implements ClientMethodRepresentationExtractor {

    private final Collection<AnnotatedMethodHandler> annotatedMethodHandlers;

    public FeignMethodRepresentationExtractor() {
        this.annotatedMethodHandlers = Arrays.asList(
            new DeleteMappingMethodsHandler(), new GetMappingMethodsHandler(), new PatchMappingMethodsHandler(),
            new PostMappingMethodsHandler(), new PutMappingMethodsHandler(), new RequestMappingMethodsHandler());
    }

    @Override
    public ClientMethodRepresentation extractClientMethodRepresentation(Method clientMethod) {
        return ClientMethodRepresentation.builder()
            .requestProperties(extractRequestProperties(clientMethod))
            .responsePropertiesList(extractResponseProperties(clientMethod))
            .build();
    }

    private RequestProperties extractRequestProperties(Method feignClientMethod) {
        return this.annotatedMethodHandlers.stream()
            .filter(annotationHandler -> annotationHandler.isSupported(feignClientMethod))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
            .handle(feignClientMethod);
    }

    private static List<ResponseProperties> extractResponseProperties(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getDeclaredAnnotationsByType(ResponseInfo.class))
            .map(annotation -> ResponseProperties.builder()
                .status(annotation.status())
                .headers(RawHeadersParser.parseAll(annotation.headers()))
                .bodyType(feignClientMethod.getReturnType())
                .build())
            .collect(Collectors.toList());
    }
}
