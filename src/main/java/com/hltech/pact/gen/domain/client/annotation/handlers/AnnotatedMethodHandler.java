package com.hltech.pact.gen.domain.client.annotation.handlers;

import com.hltech.pact.gen.domain.client.model.RequestRepresentation;

import java.lang.reflect.Method;

public interface AnnotatedMethodHandler {

    boolean isSupported(Method method);

    RequestRepresentation handleRequest(Method method);

    String[] getResponseMediaHeaders(Method method);
}
