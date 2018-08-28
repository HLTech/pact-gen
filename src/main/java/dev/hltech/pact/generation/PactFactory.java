package dev.hltech.pact.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.model.Header;
import dev.hltech.pact.generation.model.Interaction;
import dev.hltech.pact.generation.model.InteractionRequest;
import dev.hltech.pact.generation.model.InteractionResponse;
import dev.hltech.pact.generation.model.Metadata;
import dev.hltech.pact.generation.model.Pact;
import dev.hltech.pact.generation.model.RawHeader;
import dev.hltech.pact.generation.model.RequestProperties;
import dev.hltech.pact.generation.model.ResponseProperties;
import dev.hltech.pact.generation.model.Service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ValueConstants;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PactFactory {

    public Pact create(Class<?> feignClient, String consumerName) {
        return Pact.builder()
            .provider(new Service(feignClient.getAnnotation(FeignClient.class).value()))
            .consumer(new Service(consumerName))
            .interactions(createInteractions(feignClient.getMethods()))
            .metadata(new Metadata("1.0.0"))
            .build();
    }

    private static List<Interaction> createInteractions(Method[] feignClientMethods) {
        return Arrays.stream(feignClientMethods)
            .map(PactFactory::createInteraction)
            .collect(Collectors.toList());
    }

    private static Interaction createInteraction(Method feignClientMethod) {
        return Interaction.builder()
            .description(feignClientMethod.getName())
            .request(createInteractionRequest(feignClientMethod))
            .response(createInteractionResponse(feignClientMethod))
            .build();
    }

    private static InteractionRequest createInteractionRequest(Method feignClientMethod) {
        final RequestProperties requestProperties = extractRequestProperties(feignClientMethod);

        return InteractionRequest.builder()
            .method(requestProperties.getHttpMethod().name())
            .path(requestProperties.getPath())
            .headers(combineHeaders(requestProperties))
            .body(BodySerializer.serializeBody(
                findRequestBodyClass(requestProperties.getParameters()), new ObjectMapper()))
            .build();
    }

    private static Class<?> findRequestBodyClass(List<Parameter> parameters) {
        return parameters.stream()
            .filter(PactFactory::isRequestBody)
            .findFirst()
            .map(Parameter::getType)
            .orElse(null);
    }

    private static boolean isRequestBody(Parameter parameter) {
        return parameter.getAnnotations().length == 0
            || parameter.isAnnotationPresent(RequestBody.class);
    }

    private static List<Header> combineHeaders(RequestProperties requestProperties) {
        return Stream
            .concat(
                parseHeaders(requestProperties.getRequestMappingHeaders()).stream(),
                mapHeaders(requestProperties.getRequestHeaderHeaders()).stream())
            .collect(Collectors.toList());
    }

    private static List<Header> mapHeaders(List<RawHeader> headers) {
        return headers.stream()
            .map(rawHeader -> Header.builder()
                .name(rawHeader.getName())
                .value(String.valueOf(rawHeader.getValue()))
                .build())
            .collect(Collectors.toList());
    }

    private static InteractionResponse createInteractionResponse(Method feignClientMethod) {
        final ResponseProperties responseProperties = extractResponseProperties(feignClientMethod);
        return InteractionResponse.builder()
            .status(responseProperties.getStatus().toString())
            .headers(parseHeaders(responseProperties.getHeaders()))
            .body(BodySerializer.serializeBody(
                responseProperties.getReturnType(), new ObjectMapper()))
            .build();
    }

    private static RequestProperties extractRequestProperties(Method feignClientMethod) {
        if (feignClientMethod.isAnnotationPresent(DeleteMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(DeleteMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(DeleteMapping.class).path()[0])
                .requestMappingHeaders(feignClientMethod.getAnnotation(DeleteMapping.class).headers())
                .requestHeaderHeaders(extractRequestHeaderParams(feignClientMethod))
                .parameters(Arrays.asList(feignClientMethod.getParameters()))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(GetMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(GetMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(GetMapping.class).path()[0])
                .requestMappingHeaders(feignClientMethod.getAnnotation(GetMapping.class).headers())
                .requestHeaderHeaders(extractRequestHeaderParams(feignClientMethod))
                .parameters(Arrays.asList(feignClientMethod.getParameters()))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PatchMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PatchMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PatchMapping.class).path()[0])
                .requestMappingHeaders(feignClientMethod.getAnnotation(PatchMapping.class).headers())
                .requestHeaderHeaders(extractRequestHeaderParams(feignClientMethod))
                .parameters(Arrays.asList(feignClientMethod.getParameters()))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PostMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PostMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PostMapping.class).path()[0])
                .requestMappingHeaders(feignClientMethod.getAnnotation(PostMapping.class).headers())
                .requestHeaderHeaders(extractRequestHeaderParams(feignClientMethod))
                .parameters(Arrays.asList(feignClientMethod.getParameters()))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PutMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PutMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PutMapping.class).path()[0])
                .requestMappingHeaders(feignClientMethod.getAnnotation(PutMapping.class).headers())
                .requestHeaderHeaders(extractRequestHeaderParams(feignClientMethod))
                .parameters(Arrays.asList(feignClientMethod.getParameters()))
                .build();
        } else if (feignClientMethod.isAnnotationPresent(RequestMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(RequestMapping.class)
                    .method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(RequestMapping.class).path()[0])
                .requestMappingHeaders(feignClientMethod.getAnnotation(RequestMapping.class).headers())
                .requestHeaderHeaders(extractRequestHeaderParams(feignClientMethod))
                .parameters(Arrays.asList(feignClientMethod.getParameters()))
                .build();
        }

        throw new IllegalArgumentException("Unknown method");
    }

    private static List<RawHeader> extractRequestHeaderParams(Method feignClientMethod) {
        return Arrays.stream(feignClientMethod.getParameters())
            .filter(param -> param.getAnnotation(RequestHeader.class) != null)
            .filter(param -> param.getType() != Map.class
                && param.getType() != MultiValueMap.class
                && param.getType() != HttpHeaders.class)
            .map(PactFactory::extractRequestHeaderParam)
            .collect(Collectors.toList());
    }

    private static RawHeader extractRequestHeaderParam(Parameter param) {
        return RawHeader.builder()
            .name(extractParamName(param))
            .value(extractParamValue(param))
            .build();
    }

    private static String extractParamName(Parameter param) {
        RequestHeader annotation = param.getAnnotation(RequestHeader.class);

        if (!annotation.name().isEmpty()) {
            return annotation.name();
        } else if (!annotation.value().isEmpty()) {
            return annotation.value();
        }

        return param.getName();
    }

    private static Object extractParamValue(Parameter param) {
        RequestHeader annotation = param.getAnnotation(RequestHeader.class);

        if (annotation.defaultValue().equals(ValueConstants.DEFAULT_NONE) || annotation.defaultValue().isEmpty()) {
            return new PodamFactoryImpl().manufacturePojo(param.getType());
        }

        return annotation.defaultValue();
    }

    private static ResponseProperties extractResponseProperties(Method feignClientMethod) {
        return ResponseProperties.builder()
            .status(feignClientMethod.getAnnotation(ResponseInfo.class).status())
            .headers(feignClientMethod.getAnnotation(ResponseInfo.class).headers())
            .returnType(feignClientMethod.getReturnType())
            .build();
    }

    private static List<Header> parseHeaders(String[] stringHeaderArray) {
        return Arrays.stream(stringHeaderArray)
            .map(stringHeader -> stringHeader.split("="))
            .map(PactFactory::parseHeader)
            .collect(Collectors.toList());
    }

    private static Header parseHeader(String[] stringHeaderArray) {
        return Header.builder()
            .name(stringHeaderArray[0])
            .value(stringHeaderArray[1])
            .build();
    }
}
