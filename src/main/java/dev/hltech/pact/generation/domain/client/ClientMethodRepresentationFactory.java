package dev.hltech.pact.generation.domain.client;

import dev.hltech.pact.generation.domain.client.feign.FeignMethodRepresentationExtractor;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;

import java.lang.reflect.Method;

public class ClientMethodRepresentationFactory {

    public static ClientMethodRepresentation createClientRepresentation(Method method) {
        
        ClientMethodRepresentationExtractor clientMethodRepresentationExtractor = new FeignMethodRepresentationExtractor();
        return ClientMethodRepresentation.builder()
            .requestProperties(clientMethodRepresentationExtractor.extractRequestProperties(method))
            .responseProperties(clientMethodRepresentationExtractor.extractResponseProperties(method))
            .build();
    }
}
