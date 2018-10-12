package com.hltech.pact.gen.domain.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hltech.pact.gen.PactGenerationException;
import com.hltech.pact.gen.domain.client.ClientMethodRepresentationExtractor;
import com.hltech.pact.gen.domain.client.feign.FeignMethodRepresentationExtractor;
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation;
import com.hltech.pact.gen.domain.client.model.Param;
import com.hltech.pact.gen.domain.client.model.RequestRepresentation;
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation;
import com.hltech.pact.gen.domain.pact.model.Interaction;
import com.hltech.pact.gen.domain.pact.model.InteractionRequest;
import com.hltech.pact.gen.domain.pact.model.InteractionResponse;
import com.hltech.pact.gen.domain.pact.model.Metadata;
import com.hltech.pact.gen.domain.pact.model.Pact;
import org.springframework.cloud.openfeign.FeignClient;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PactFactory {

    private static final PodamFactory podamFactory;

    static {
        podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(String.class, new EnumStringManufacturer());
        podamFactory.getStrategy().setDefaultNumberOfCollectionElements(1);
    }

    public Pact createFromFeignClient(Class<?> feignClient, String consumerName, ObjectMapper objectMapper) {

        ClientMethodRepresentationExtractor methodExtractor = new FeignMethodRepresentationExtractor();

        return Pact.builder()
            .provider(new Service(extractProviderName(feignClient)))
            .consumer(new Service(consumerName))
            .interactions(createInteractionsFromMethods(methodExtractor, feignClient.getMethods(), objectMapper))
            .metadata(new Metadata("1.0.0"))
            .build();
    }

    private String extractProviderName(Class<?> feignClient) {
        FeignClient feignClientAnnotation = feignClient.getAnnotation(FeignClient.class);
        return feignClientAnnotation.value().isEmpty() ? feignClientAnnotation.name() : feignClientAnnotation.value();
    }

    private static List<Interaction> createInteractionsFromMethods(
        ClientMethodRepresentationExtractor extractor, Method[] clientMethods, ObjectMapper objectMapper) {

        return Arrays.stream(clientMethods)
            .flatMap(clientMethod -> createInteractionsFromMethod(extractor, clientMethod, objectMapper).stream())
            .collect(Collectors.toList());
    }

    private static List<Interaction> createInteractionsFromMethod(
        ClientMethodRepresentationExtractor extractor, Method clientMethod, ObjectMapper objectMapper) {

        ClientMethodRepresentation methodRepresentation = extractor.extractClientMethodRepresentation(clientMethod);

        PojoValidator.validateAll(PojoExtractor.extractPojoTypes(methodRepresentation));

        return methodRepresentation.getResponseRepresentationList().stream()
            .map(interactionResponseRepresentation -> Interaction.builder()
                .description(createDescription(clientMethod.getName(), interactionResponseRepresentation))
                .request(createInteractionRequest(methodRepresentation.getRequestRepresentation(), objectMapper))
                .response(createInteractionResponse(interactionResponseRepresentation, objectMapper))
                .build())
            .collect(Collectors.toList());
    }

    private static String createDescription(String feignMethodName, ResponseRepresentation response) {
        if (response.getDescription().isEmpty()) {
            return String.format("%s request; %s response", feignMethodName, response.getStatus());
        }

        return response.getDescription();
    }

    private static InteractionRequest createInteractionRequest(
        RequestRepresentation requestRepresentation, ObjectMapper objectMapper) {

        return InteractionRequest.builder()
            .method(requestRepresentation.getHttpMethod().name())
            .path(parsePath(requestRepresentation.getPath(), requestRepresentation.getPathParameters()))
            .headers(mapHeaders(requestRepresentation.getHeaders()))
            .query(parseParametersToQuery(requestRepresentation.getRequestParameters()))
            .body(BodySerializer.serializeBody(requestRepresentation.getBody(), objectMapper, podamFactory))
            .build();
    }

    private static String parsePath(String path, List<Param> pathParameters) {
        String resultPath = path;
        for (Param param : pathParameters) {
            Object paramValue = getParamValue(param);

            resultPath = resultPath.replace("{" + param.getName() + "}", String.valueOf(paramValue));
        }
        return resultPath;
    }

    private static Object getParamValue(Param param) {
        if (param.getDefaultValue() != null) {
            return param.getDefaultValue();
        }

        if (param.getGenericArgumentType() != null) {
            return manufacturePojo(param.getGenericArgumentType());
        }

        return manufacturePojo(param.getType());
    }

    private static Object manufacturePojo(Class<?> type) {
        Object manufacturedPojo = podamFactory.manufacturePojo(type);

        if (manufacturedPojo == null) {
            throw new PactGenerationException("Podam manufacturing failed");
        }

        return manufacturedPojo;
    }

    private static String parseParametersToQuery(List<Param> requestParameters) {
        StringBuilder queryBuilder = new StringBuilder();

        requestParameters
            .forEach(param -> queryBuilder
                .append(param.getName())
                .append("=")
                .append(String.valueOf(getParamValue(param)))
                .append("&"));

        if (queryBuilder.length() != 0) {
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        }

        return queryBuilder.toString();
    }

    private static InteractionResponse createInteractionResponse(
        ResponseRepresentation responseRepresentation,
        ObjectMapper objectMapper) {

        return InteractionResponse.builder()
                .status(responseRepresentation.getStatus().toString())
                .headers(mapHeaders(responseRepresentation.getHeaders()))
                .body(BodySerializer.serializeBody(responseRepresentation.getBody(), objectMapper, podamFactory))
                .build();
    }

    private static Map<String, String> mapHeaders(List<Param> headers) {
        return headers.stream()
            .collect(Collectors.toMap(Param::getName, header -> String.valueOf(getParamValue(header))));
    }
}
