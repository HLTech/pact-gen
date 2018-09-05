package dev.hltech.pact.generation.domain.client.feign;

import dev.hltech.pact.generation.domain.annotation.handlers.AnnotatedMethodHandler;
import dev.hltech.pact.generation.domain.annotation.handlers.DeleteMappingMethodsHandler;
import dev.hltech.pact.generation.domain.annotation.handlers.GetMappingMethodsHandler;
import dev.hltech.pact.generation.domain.annotation.handlers.PatchMappingMethodsHandler;
import dev.hltech.pact.generation.domain.annotation.handlers.PostMappingMethodsHandler;
import dev.hltech.pact.generation.domain.annotation.handlers.PutMappingMethodsHandler;
import dev.hltech.pact.generation.domain.annotation.handlers.RequestMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.ClientMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import dev.hltech.pact.generation.domain.util.RawHeadersParser;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

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
            .responseProperties(extractResponseProperties(clientMethod))
            .build();
    }

    private RequestProperties extractRequestProperties(Method feignClientMethod) {
        return this.annotatedMethodHandlers.stream()
            .filter(annotationHandler -> annotationHandler.isSupported(feignClientMethod))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
            .handle(feignClientMethod);
    }

    private static ResponseProperties extractResponseProperties(Method feignClientMethod) {
        return ResponseProperties.builder()
            .status(feignClientMethod.getAnnotation(ResponseInfo.class).status())
            .headers(RawHeadersParser.parseAll(feignClientMethod.getAnnotation(ResponseInfo.class).headers()))
            .bodyType(feignClientMethod.getReturnType())
            .build();
    }
}
