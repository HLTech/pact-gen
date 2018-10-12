package com.hltech.pact.gen.domain.client;

import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation;

import java.lang.reflect.Method;

public interface ClientMethodRepresentationExtractor {

    ClientMethodRepresentation extractClientMethodRepresentation(Method clientMethod);
}
