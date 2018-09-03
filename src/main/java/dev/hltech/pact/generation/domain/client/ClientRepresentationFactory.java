package dev.hltech.pact.generation.domain.client;

import dev.hltech.pact.generation.domain.client.feign.FeignRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientRepresentation;

import java.lang.reflect.Method;

public class ClientRepresentationFactory {

    public static ClientRepresentation createClientRepresentation(Method method) {
        
        ClientRepresentationExtractor clientRepresentationExtractor = new FeignRepresentationExtractor();
        return ClientRepresentation.builder()
            .requestProperties(clientRepresentationExtractor.extractRequestProperties(method))
            .responseProperties(clientRepresentationExtractor.extractResponseProperties(method))
            .build();
    }
}
