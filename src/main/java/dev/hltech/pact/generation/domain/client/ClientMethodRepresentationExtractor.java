package dev.hltech.pact.generation.domain.client;

import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;

import java.lang.reflect.Method;

public interface ClientMethodRepresentationExtractor {

    ClientMethodRepresentation extractClientMethodRepresentation(Method clientMethod);
}
