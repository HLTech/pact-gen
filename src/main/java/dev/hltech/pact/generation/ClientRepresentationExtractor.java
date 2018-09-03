package dev.hltech.pact.generation;

import dev.hltech.pact.generation.model.RequestProperties;
import dev.hltech.pact.generation.model.ResponseProperties;

import java.lang.reflect.Method;

public interface ClientRepresentationExtractor {

    RequestProperties extractRequestProperties(Method clientMethod);

    ResponseProperties extractResponseProperties(Method clientMethod);
}
