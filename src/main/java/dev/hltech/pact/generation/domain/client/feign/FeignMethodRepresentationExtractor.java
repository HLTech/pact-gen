package dev.hltech.pact.generation.domain.client.feign;

import dev.hltech.pact.generation.domain.client.ClientMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.Header;
import dev.hltech.pact.generation.domain.client.model.Param;
import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeignMethodRepresentationExtractor implements ClientMethodRepresentationExtractor {

    @Override
    public ClientMethodRepresentation extractClientMethodRepresentation(Method clientMethod) {
        return ClientMethodRepresentation.builder()
            .requestProperties(extractRequestProperties(clientMethod))
            .responseProperties(extractResponseProperties(clientMethod))
            .build();
    }

    private static RequestProperties extractRequestProperties(Method feignClientMethod) {
        if (feignClientMethod.isAnnotationPresent(DeleteMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(DeleteMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(DeleteMapping.class).path()[0])
                .headers(combineHeaders(
                    feignClientMethod.getAnnotation(DeleteMapping.class).headers(),
                    extractRequestHeaderParams(feignClientMethod)))
                .bodyType(findRequestBodyClass(feignClientMethod.getParameters()))
                .requestParameters(extractRequestParameters(feignClientMethod))
                .pathParameters(extractPathParameters(feignClientMethod))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(GetMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(GetMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(GetMapping.class).path()[0])
                .headers(combineHeaders(
                    feignClientMethod.getAnnotation(GetMapping.class).headers(),
                    extractRequestHeaderParams(feignClientMethod)))
                .bodyType(findRequestBodyClass(feignClientMethod.getParameters()))
                .requestParameters(extractRequestParameters(feignClientMethod))
                .pathParameters(extractPathParameters(feignClientMethod))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PatchMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PatchMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PatchMapping.class).path()[0])
                .headers(combineHeaders(
                    feignClientMethod.getAnnotation(PatchMapping.class).headers(),
                    extractRequestHeaderParams(feignClientMethod)))
                .bodyType(findRequestBodyClass(feignClientMethod.getParameters()))
                .requestParameters(extractRequestParameters(feignClientMethod))
                .pathParameters(extractPathParameters(feignClientMethod))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PostMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PostMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PostMapping.class).path()[0])
                .headers(combineHeaders(
                    feignClientMethod.getAnnotation(PostMapping.class).headers(),
                    extractRequestHeaderParams(feignClientMethod)))
                .bodyType(findRequestBodyClass(feignClientMethod.getParameters()))
                .requestParameters(extractRequestParameters(feignClientMethod))
                .pathParameters(extractPathParameters(feignClientMethod))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PutMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PutMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PutMapping.class).path()[0])
                .headers(combineHeaders(
                    feignClientMethod.getAnnotation(PutMapping.class).headers(),
                    extractRequestHeaderParams(feignClientMethod)))
                .bodyType(findRequestBodyClass(feignClientMethod.getParameters()))
                .requestParameters(extractRequestParameters(feignClientMethod))
                .pathParameters(extractPathParameters(feignClientMethod))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(RequestMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(RequestMapping.class)
                    .method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(RequestMapping.class).path()[0])
                .headers(combineHeaders(
                    feignClientMethod.getAnnotation(RequestMapping.class).headers(),
                    extractRequestHeaderParams(feignClientMethod)))
                .bodyType(findRequestBodyClass(feignClientMethod.getParameters()))
                .requestParameters(extractRequestParameters(feignClientMethod))
                .pathParameters(extractPathParameters(feignClientMethod))
                .build();
        }

        throw new IllegalArgumentException("Unknown method");
    }

    private static List<Param> extractPathParameters(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(PathVariable.class) != null)
            .filter(param -> param.getType() != Map.class)
            .map(FeignMethodRepresentationExtractor::extractPathParameter)
            .collect(Collectors.toList());
    }

    private static Param extractPathParameter(Parameter param) {
        PathVariable annotation = param.getAnnotation(PathVariable.class);

        return Param.builder()
            .name(annotation.name().isEmpty() ? annotation.value() : annotation.name())
            .paramType(param.getType())
            .build();
    }

    private static ResponseProperties extractResponseProperties(Method feignClientMethod) {
        return ResponseProperties.builder()
            .status(feignClientMethod.getAnnotation(ResponseInfo.class).status())
            .headers(parseHeaders(feignClientMethod.getAnnotation(ResponseInfo.class).headers()))
            .bodyType(feignClientMethod.getReturnType())
            .build();
    }

    private static Class<?> findRequestBodyClass(Parameter[] parameters) {
        return Arrays.stream(parameters)
            .filter(FeignMethodRepresentationExtractor::isRequestBody)
            .findFirst()
            .map(Parameter::getType)
            .orElse(null);
    }

    private static boolean isRequestBody(Parameter parameter) {
        return parameter.getAnnotations().length == 0
            || parameter.isAnnotationPresent(RequestBody.class);
    }

    private static List<Param> extractRequestParameters(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(RequestParam.class) != null)
            .filter(param -> param.getType() != Map.class)
            .map(FeignMethodRepresentationExtractor::extractRequestParameter)
            .collect(Collectors.toList());
    }

    private static Param extractRequestParameter(Parameter param) {
        Param.ParamBuilder builder = Param.builder();

        extractParamDefaultValue(param).ifPresent(builder::defaultValue);

        return builder
            .name(extractParamName(param))
            .paramType(param.getType())
            .build();
    }

    private static String extractParamName(Parameter param) {
        RequestParam annotation = param.getAnnotation(RequestParam.class);

        if (!annotation.name().isEmpty()) {
            return annotation.name();
        } else if (!annotation.value().isEmpty()) {
            return annotation.value();
        }

        return param.getName();
    }

    private static Optional<Object> extractParamDefaultValue(Parameter param) {
        RequestParam annotation = param.getAnnotation(RequestParam.class);

        if (annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE) || annotation.defaultValue().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(annotation.defaultValue());
    }

    private static List<Header> extractRequestHeaderParams(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(RequestHeader.class) != null)
            .filter(param -> param.getType() != Map.class
                && param.getType() != MultiValueMap.class
                && param.getType() != HttpHeaders.class)
            .map(FeignMethodRepresentationExtractor::extractRequestHeaderParam)
            .collect(Collectors.toList());
    }

    private static Header extractRequestHeaderParam(Parameter param) {
        Header.HeaderBuilder builder = Header.builder();

        extractHeaderDefaultValue(param).ifPresent(builder::defaultValue);

        return builder
            .name(extractHeaderName(param))
            .headerType(param.getType())
            .build();
    }

    private static String extractHeaderName(Parameter param) {
        RequestHeader annotation = param.getAnnotation(RequestHeader.class);

        if (!annotation.name().isEmpty()) {
            return annotation.name();
        } else if (!annotation.value().isEmpty()) {
            return annotation.value();
        }

        return param.getName();
    }

    private static Optional<Object> extractHeaderDefaultValue(Parameter param) {
        RequestHeader annotation = param.getAnnotation(RequestHeader.class);

        if (annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE) || annotation.defaultValue().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(annotation.defaultValue());
    }

    private static List<Header> combineHeaders(String[] rawHeaders, List<Header> headers) {
        return Stream
            .concat(parseHeaders(rawHeaders).stream(), headers.stream())
            .collect(Collectors.toList());
    }

    private static List<Header> parseHeaders(String[] stringHeaderArray) {
        return Arrays.stream(stringHeaderArray)
            .map(stringHeader -> stringHeader.split("="))
            .map(FeignMethodRepresentationExtractor::parseHeader)
            .collect(Collectors.toList());
    }

    private static Header parseHeader(String[] stringHeaderArray) {
        return Header.builder()
            .name(stringHeaderArray[0])
            .defaultValue(stringHeaderArray[1])
            .build();
    }
}
