package dev.hltech.pact.generation.domain.client.annotation.handlers;

import dev.hltech.pact.generation.domain.client.model.Param;
import dev.hltech.pact.generation.domain.client.model.RequestRepresentation;
import dev.hltech.pact.generation.domain.client.util.PathParametersExtractor;
import dev.hltech.pact.generation.domain.client.util.RawHeadersParser;
import dev.hltech.pact.generation.domain.client.util.RequestBodyExtractor;
import dev.hltech.pact.generation.domain.client.util.RequestHeaderParamsExtractor;
import dev.hltech.pact.generation.domain.client.util.RequestParametersExtractor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteMappingMethodsHandler implements AnnotatedMethodHandler {

    @Override
    public boolean isSupported(Method method) {
        return method.isAnnotationPresent(DeleteMapping.class);
    }

    @Override
    public RequestRepresentation handle(Method method) {
        return RequestRepresentation.builder()
            .httpMethod(HttpMethod.DELETE)
            .path(getPathFromMethod(method))
            .headers(combineHeaders(
                method.getAnnotation(DeleteMapping.class).headers(),
                RequestHeaderParamsExtractor.extractAll(method)))
            .body(RequestBodyExtractor.extract(method.getParameters()))
            .requestParameters(RequestParametersExtractor.extractAll(method))
            .pathParameters(PathParametersExtractor.extractAll(method))
            .build();
    }

    private String getPathFromMethod(Method method) {
        DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
        return annotation.path().length == 1 ? annotation.path()[0] : annotation.value()[0];
    }

    private static List<Param> combineHeaders(String[] rawHeaders, List<Param> headers) {
        return Stream
            .concat(RawHeadersParser.parseAll(rawHeaders).stream(), headers.stream())
            .collect(Collectors.toList());
    }
}
