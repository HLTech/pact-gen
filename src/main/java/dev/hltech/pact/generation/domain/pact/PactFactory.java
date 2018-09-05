package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.client.ClientMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.feign.FeignMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.Header;
import dev.hltech.pact.generation.domain.client.model.Param;
import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import dev.hltech.pact.generation.domain.pact.model.Interaction;
import dev.hltech.pact.generation.domain.pact.model.InteractionRequest;
import dev.hltech.pact.generation.domain.pact.model.InteractionResponse;
import dev.hltech.pact.generation.domain.pact.model.Metadata;
import dev.hltech.pact.generation.domain.pact.model.Pact;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PactFactory {

    
    public Pact createFromFeignClient(Class<?> feignClient, String consumerName, ObjectMapper objectMapper) {
        ClientMethodRepresentationExtractor methodExtractor = new FeignMethodRepresentationExtractor();

        return Pact.builder()
            .provider(new Service(feignClient.getAnnotation(FeignClient.class).value()))
            .consumer(new Service(consumerName))
            .interactions(createInteractions(methodExtractor, feignClient.getMethods(), objectMapper))
            .metadata(new Metadata("1.0.0"))
            .build();
    }

    private static List<Interaction> createInteractions(
        ClientMethodRepresentationExtractor extractor, Method[] clientMethods, ObjectMapper objectMapper) {

        return Arrays.stream(clientMethods)
            .map(clientMethod -> createInteraction(extractor, clientMethod, objectMapper))
            .collect(Collectors.toList());
    }

    private static Interaction createInteraction(
        ClientMethodRepresentationExtractor extractor, Method clientMethod, ObjectMapper objectMapper) {

        ClientMethodRepresentation methodRepresentation = extractor.extractClientMethodRepresentation(clientMethod);
        return Interaction.builder()
            .description(clientMethod.getName())
            .request(createInteractionRequest(methodRepresentation.getRequestProperties(), objectMapper))
            .response(createInteractionResponse(methodRepresentation.getResponseProperties(), objectMapper))
            .build();
    }

    private static InteractionRequest createInteractionRequest(
        RequestProperties requestProperties, ObjectMapper objectMapper) {

        return InteractionRequest.builder()
            .method(requestProperties.getHttpMethod().name())
            .path(parsePath(requestProperties.getPath(), requestProperties.getPathParameters()))
            .headers(mapHeaders(requestProperties.getHeaders()))
            .query(parseParametersToQuery(requestProperties.getRequestParameters()))
            .body(BodySerializer.serializeBody(requestProperties.getBodyType(), objectMapper))
            .build();
    }

    private static String parsePath(String path, List<Param> pathParameters) {
        String resultPath = path;
        for (Param param : pathParameters) {
            resultPath = path.replace("{" + param.getName() + "}", String.valueOf(param.getValue()));
        }
        return resultPath;
    }

    private static String parseParametersToQuery(List<Param> requestParameters) {
        StringBuilder queryBuilder = new StringBuilder();

        requestParameters
            .forEach(param -> queryBuilder
                .append(param.getName())
                .append("=")
                .append(String.valueOf(param.getValue()))
                .append("&"));

        if (queryBuilder.length() != 0) {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }

        return queryBuilder.toString();
    }

    private static InteractionResponse createInteractionResponse(
        ResponseProperties responseProperties, ObjectMapper objectMapper) {

        return InteractionResponse.builder()
            .status(responseProperties.getStatus().toString())
            .headers(mapHeaders(responseProperties.getHeaders()))
            .body(BodySerializer.serializeBody(
                responseProperties.getBodyType(), objectMapper))
            .build();
    }

    private static List<dev.hltech.pact.generation.domain.pact.model.Header> mapHeaders(List<Header> headers) {
        return headers.stream()
            .map(header -> dev.hltech.pact.generation.domain.pact.model.Header.builder()
                .name(header.getName())
                .value(String.valueOf(header.getValue()))
                .build())
            .collect(Collectors.toList());
    }
}
