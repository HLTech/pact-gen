package dev.hltech.pact.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.model.ClientRepresentation;
import dev.hltech.pact.generation.model.ClientRepresentationFactory;
import dev.hltech.pact.generation.model.Header;
import dev.hltech.pact.generation.model.Interaction;
import dev.hltech.pact.generation.model.InteractionRequest;
import dev.hltech.pact.generation.model.InteractionResponse;
import dev.hltech.pact.generation.model.Metadata;
import dev.hltech.pact.generation.model.Pact;
import dev.hltech.pact.generation.model.RawHeader;
import dev.hltech.pact.generation.model.RawParam;
import dev.hltech.pact.generation.model.RequestProperties;
import dev.hltech.pact.generation.model.ResponseProperties;
import dev.hltech.pact.generation.model.Service;
import org.springframework.cloud.openfeign.FeignClient;

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
        ClientRepresentation clientRepresentation =
            ClientRepresentationFactory.createClientRepresentation(feignClientMethod);

        return Interaction.builder()
            .description(feignClientMethod.getName())
            .request(createInteractionRequest(clientRepresentation.getRequestProperties()))
            .response(createInteractionResponse(clientRepresentation.getResponseProperties()))
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

    private static String parseParametersToQuery(List<RawParam> requestParameters) {
        StringBuilder queryBuilder = new StringBuilder();

        requestParameters
            .forEach(rawParam -> queryBuilder
                .append(rawParam.getName())
                .append("=")
                .append(String.valueOf(rawParam.getValue()))
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

    private static List<Header> mapHeaders(List<RawHeader> headers) {
        return headers.stream()
            .map(rawHeader -> Header.builder()
                .name(rawHeader.getName())
                .value(String.valueOf(rawHeader.getValue()))
                .build())
            .collect(Collectors.toList());
    }
}
