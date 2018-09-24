package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.client.ClientMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.feign.FeignMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.Param;
import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import dev.hltech.pact.generation.domain.pact.model.Interaction;
import dev.hltech.pact.generation.domain.pact.model.InteractionRequest;
import dev.hltech.pact.generation.domain.pact.model.InteractionResponse;
import dev.hltech.pact.generation.domain.pact.model.Metadata;
import dev.hltech.pact.generation.domain.pact.model.Pact;
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

        return createInteractionResponse(methodRepresentation.getResponsePropertiesList(), objectMapper).stream()
            .map(interactionResponse -> Interaction.builder()
                .description(clientMethod.getName())
                .request(createInteractionRequest(methodRepresentation.getRequestProperties(), objectMapper))
                .response(interactionResponse)
                .build())
            .collect(Collectors.toList());
    }

    private static InteractionRequest createInteractionRequest(
        RequestProperties requestProperties, ObjectMapper objectMapper) {

        return InteractionRequest.builder()
            .method(requestProperties.getHttpMethod().name())
            .path(parsePath(requestProperties.getPath(), requestProperties.getPathParameters()))
            .headers(mapHeaders(requestProperties.getHeaders()))
            .query(parseParametersToQuery(requestProperties.getRequestParameters()))
            .body(BodySerializer.serializeBody(requestProperties.getBody(), objectMapper, podamFactory))
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
        if (param.getDefaultValue() == null) {
            return podamFactory.manufacturePojo(param.getType());
        }

        return param.getDefaultValue();
    }

    private static Object getHeaderValue(Param header) {
        if (header.getDefaultValue() == null) {
            return podamFactory.manufacturePojo(header.getType());
        }

        return header.getDefaultValue();
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

    private static List<InteractionResponse> createInteractionResponse(
        List<ResponseProperties> responseProperties,
        ObjectMapper objectMapper) {

        return responseProperties.stream()
            .map(props -> InteractionResponse.builder()
                .status(props.getStatus().toString())
                .headers(mapHeaders(props.getHeaders()))
                .body(BodySerializer.serializeBody(props.getBody(), objectMapper, podamFactory))
                .build())
            .collect(Collectors.toList());
    }

    private static Map<String, String> mapHeaders(List<Param> headers) {
        return headers.stream()
            .collect(Collectors.toMap(Param::getName, header -> String.valueOf(getHeaderValue(header))));
    }
}
