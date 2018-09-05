package dev.hltech.pact.generation.domain.pact.annotation.handlers;

import dev.hltech.pact.generation.domain.client.model.Param;
import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.pact.annotation.handlers.util.PathParametersExtractor;
import dev.hltech.pact.generation.domain.pact.annotation.handlers.util.RawHeadersParser;
import dev.hltech.pact.generation.domain.pact.annotation.handlers.util.RequestBodyTypeFinder;
import dev.hltech.pact.generation.domain.pact.annotation.handlers.util.RequestHeaderParamsExtractor;
import dev.hltech.pact.generation.domain.pact.annotation.handlers.util.RequestParametersExtractor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RequestMappingMethodsHandler implements AnnotatedMethodHandler {

    @Override
    public boolean isSupported(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    @Override
    public RequestProperties handle(Method method) {
        return RequestProperties.builder()
            .httpMethod(HttpMethod.resolve(method.getAnnotation(RequestMapping.class)
                .method()[0].name().toUpperCase()))
            .path(method.getAnnotation(RequestMapping.class).path()[0])
            .headers(combineHeaders(
                method.getAnnotation(RequestMapping.class).headers(),
                RequestHeaderParamsExtractor.extractRequestHeaderParams(method)))
            .bodyType(RequestBodyTypeFinder.findRequestBodyType(method.getParameters()))
            .requestParameters(RequestParametersExtractor.extractRequestParameters(method))
            .pathParameters(PathParametersExtractor.extractPathParameters(method))
            .build();
    }

    private static List<Param> combineHeaders(String[] rawHeaders, List<Param> headers) {
        return Stream
            .concat(RawHeadersParser.parseHeaders(rawHeaders).stream(), headers.stream())
            .collect(Collectors.toList());
    }
}
