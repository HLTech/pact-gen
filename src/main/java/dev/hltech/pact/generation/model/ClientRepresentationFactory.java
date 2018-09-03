package dev.hltech.pact.generation.model;

import dev.hltech.pact.generation.ClientRepresentationExtractor;
import dev.hltech.pact.generation.FeignRepresentationExtractor;

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
