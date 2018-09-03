package dev.hltech.pact.generation.domain.client;

import dev.hltech.pact.generation.domain.client.model.RequestProperties;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;

import java.lang.reflect.Method;

public interface ClientRepresentationExtractor {

    RequestProperties extractRequestProperties(Method clientMethod);

    ResponseProperties extractResponseProperties(Method clientMethod);
}
