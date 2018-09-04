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

    
    public Pact createFromFeignClient(Class<?> feignClient, String consumerName) {
        ClientMethodRepresentationExtractor methodExtractor = new FeignMethodRepresentationExtractor();

        return Pact.builder()
            .provider(new Service(feignClient.getAnnotation(FeignClient.class).value()))
            .consumer(new Service(consumerName))
            .interactions(createInteractions(methodExtractor, feignClient.getMethods()))
            .metadata(new Metadata("1.0.0"))
            .build();
    }

    private static List<Interaction> createInteractions(
        ClientMethodRepresentationExtractor extractor, Method[] clientMethods) {
        return Arrays.stream(clientMethods)
            .map(clientMethod -> createInteraction(extractor, clientMethod))
            .collect(Collectors.toList());
    }

    private static Interaction createInteraction(ClientMethodRepresentationExtractor extractor, Method clientMethod) {
        ClientMethodRepresentation methodRepresentation = extractor.extractClientMethodRepresentation(clientMethod);
        return Interaction.builder()
            .description(clientMethod.getName())
            .request(createInteractionRequest(methodRepresentation.getRequestProperties()))
            .response(createInteractionResponse(methodRepresentation.getResponseProperties()))
            .build();
    }

    private static InteractionRequest createInteractionRequest(RequestProperties requestProperties) {

        return InteractionRequest.builder()
            .method(requestProperties.getHttpMethod().name())
            .path(requestProperties.getPath())
            .headers(mapHeaders(requestProperties.getHeaders()))
            .query(parseParametersToQuery(requestProperties.getParameters()))
            .body(BodySerializer.serializeBody(requestProperties.getBodyType(), new ObjectMapper()))
            .build();
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

    private static InteractionResponse createInteractionResponse(ResponseProperties responseProperties) {
        return InteractionResponse.builder()
            .status(responseProperties.getStatus().toString())
            .headers(mapHeaders(responseProperties.getHeaders()))
            .body(BodySerializer.serializeBody(
                responseProperties.getBodyType(), new ObjectMapper()))
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
