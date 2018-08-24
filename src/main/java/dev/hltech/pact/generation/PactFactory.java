package dev.hltech.pact.generation;

import dev.hltech.pact.generation.model.Metadata;
import dev.hltech.pact.generation.model.Pact;
import dev.hltech.pact.generation.model.RequestProperties;
import dev.hltech.pact.generation.model.Service;
import dev.hltech.pact.generation.model.Interaction;
import dev.hltech.pact.generation.model.InteractionRequest;
import dev.hltech.pact.generation.model.Header;
import dev.hltech.pact.generation.model.InteractionResponse;
import dev.hltech.pact.generation.model.ResponseProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            .headers(Arrays
                .stream(requestProperties.getHeaders())
                .map(stringHeader -> stringHeader.split("="))
                .map(PactFactory::buildHeader)
                .collect(Collectors.toList()))
            .build();
    }

    private static InteractionResponse createInteractionResponse(Method feignClientMethod) {
        final ResponseProperties responseProperties = extractResponseProperties(feignClientMethod);
        
        return InteractionResponse.builder().build();
    }

    private static RequestProperties extractRequestProperties(Method feignClientMethod) {
        if (feignClientMethod.isAnnotationPresent(DeleteMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(DeleteMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(DeleteMapping.class).path()[0])
                .headers(feignClientMethod.getAnnotation(DeleteMapping.class).headers())
                .build();
        } else if (feignClientMethod.isAnnotationPresent(GetMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(GetMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(GetMapping.class).path()[0])
                .headers(feignClientMethod.getAnnotation(GetMapping.class).headers())
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PatchMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PatchMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PatchMapping.class).path()[0])
                .headers(feignClientMethod.getAnnotation(PatchMapping.class).headers())
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PostMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PostMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PostMapping.class).path()[0])
                .headers(feignClientMethod.getAnnotation(PostMapping.class).headers())
                .build();
        } else if (feignClientMethod.isAnnotationPresent(PutMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(PutMapping.class).annotationType()
                    .getAnnotation(RequestMapping.class).method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(PutMapping.class).path()[0])
                .headers(feignClientMethod.getAnnotation(PutMapping.class).headers())
                .build();
        } else if (feignClientMethod.isAnnotationPresent(RequestMapping.class)) {
            return RequestProperties.builder()
                .httpMethod(HttpMethod.resolve(feignClientMethod.getAnnotation(RequestMapping.class)
                    .method()[0].name().toUpperCase()))
                .path(feignClientMethod.getAnnotation(RequestMapping.class).path()[0])
                .headers(feignClientMethod.getAnnotation(RequestMapping.class).headers())
                .build();
        }

        throw new IllegalArgumentException("Unknown method");
    }

    private static ResponseProperties extractResponseProperties(Method feignClientMethod) {
        
        return ResponseProperties.builder().build();
    }

    private static Header buildHeader(String[] stringHeaderArray) {
        return Header.builder()
            .name(stringHeaderArray[0])
            .value(stringHeaderArray[1])
            .build();
    }
}
