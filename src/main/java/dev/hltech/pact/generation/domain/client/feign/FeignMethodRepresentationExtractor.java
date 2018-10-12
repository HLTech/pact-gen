package dev.hltech.pact.generation.domain.client.feign;

import com.google.common.collect.Lists;
import dev.hltech.pact.generation.domain.client.annotation.handlers.AnnotatedMethodHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.DeleteMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.GetMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.PatchMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.PostMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.PutMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.annotation.handlers.RequestMappingMethodsHandler;
import dev.hltech.pact.generation.domain.client.ClientMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.Body;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.Param;
import dev.hltech.pact.generation.domain.client.model.RequestRepresentation;
import dev.hltech.pact.generation.domain.client.model.ResponseRepresentation;
import dev.hltech.pact.generation.domain.client.util.RawHeadersParser;
import dev.hltech.pact.generation.domain.client.util.TypeExtractor;
import org.springframework.http.HttpStatus;

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
            .requestRepresentation(extractRequestProperties(clientMethod))
            .responseRepresentationList(extractResponseProperties(clientMethod))
            .build();
    }

    private RequestRepresentation extractRequestProperties(Method feignClientMethod) {
        return this.annotatedMethodHandlers.stream()
            .filter(annotationHandler -> annotationHandler.isSupported(feignClientMethod))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown HTTP method"))
            .handle(feignClientMethod);
    }

    private static List<ResponseRepresentation> extractResponseProperties(Method feignClientMethod) {
        feignClientMethod.getGenericReturnType();

        List<ResponseRepresentation> results = Arrays
            .stream(feignClientMethod.getDeclaredAnnotationsByType(InteractionInfo.class))
            .map(annotation -> populateResponse(
                annotation.responseStatus(),
                RawHeadersParser.parseAll(annotation.responseHeaders()),
                feignClientMethod,
                annotation.description()))
            .collect(Collectors.toList());

        return results.size() > 0 ? results :
            Lists.newArrayList(populateResponse(HttpStatus.OK, Lists.newArrayList(), feignClientMethod, ""));
    }

    private static ResponseRepresentation populateResponse(
        HttpStatus status, List<Param> headers, Method feignClientMethod, String description) {

        return ResponseRepresentation.builder()
            .status(status)
            .headers(headers)
            .body(Body.builder()
                .type(feignClientMethod.getReturnType())
                .genericArgumentTypes(
                    TypeExtractor.extractParameterTypesFromType(feignClientMethod.getGenericReturnType()))
                .build())
            .description(description)
            .build();
    }

}
